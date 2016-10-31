package edu.stanford.irt.laneweb.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.JdbcUtils;

public class ProxyHostManager {

    private static class DatabaseProxyHostSet extends HashSet<String> {

        private static final long serialVersionUID = 1L;

        private static final String SQL =
                "SELECT DISTINCT URL_HOST AS HOST " +
                "FROM LMLDB.ELINK_INDEX " +
                "WHERE LINK_SUBTYPE         IN ('HTTP','HTTPS') " +
                "AND ELINK_INDEX.RECORD_TYPE = 'A' " +
                "AND URL_HOST NOT LIKE '%.stanford.edu' " +
                "UNION " +
                "SELECT DISTINCT URL_HOST AS HOST " +
                "FROM LMLDB.ELINK_INDEX, " +
                "  LMLDB.MFHD_MASTER, " +
                "  LMLDB.BIB_MFHD, " +
                "  LMLDB.BIB_MASTER " +
                "WHERE ELINK_INDEX.RECORD_ID    = MFHD_MASTER.MFHD_ID " +
                "AND MFHD_MASTER.MFHD_ID = BIB_MFHD.MFHD_ID " +
                "AND BIB_MFHD.BIB_ID = BIB_MASTER.BIB_ID " +
                "AND LINK_SUBTYPE              IN ('HTTP','HTTPS') " +
                "AND ELINK_INDEX.RECORD_TYPE    = 'M' " +
                "AND ELINK_INDEX.RECORD_ID NOT IN " +
                "  (SELECT MFHD_ID " +
                "  FROM LMLDB.MFHD_DATA " +
                "  WHERE LOWER(RECORD_SEGMENT) LIKE '%, noproxy%' " +
                "  ) " +
                "AND URL_HOST NOT LIKE '%.stanford.edu' " +
                "AND MFHD_MASTER.SUPPRESS_IN_OPAC != 'Y' " +
                "AND BIB_MASTER.SUPPRESS_IN_OPAC != 'Y'";

        DatabaseProxyHostSet(final DataSource dataSource) {
            Connection conn = null;
            Statement stmt = null;
            ResultSet rs = null;
            try {
                conn = dataSource.getConnection();
                stmt = conn.createStatement();
                rs = stmt.executeQuery(SQL);
                while (rs.next()) {
                    add(rs.getString(1));
                }
                add("bodoni.stanford.edu");
                add("library.stanford.edu");
                add("searchworks.stanford.edu");
            } catch (SQLException e) {
                throw new LanewebException(e);
            } finally {
                JdbcUtils.closeResultSet(rs);
                JdbcUtils.closeStatement(stmt);
                JdbcUtils.closeConnection(conn);
            }
        }
    }

    private static final Logger LOG = LoggerFactory.getLogger("error handler");

    // update every 2 hours
    private static final long UPDATE_INTERVAL = 1000L * 60L * 60L * 2L;

    private DataSource dataSource;

    private ExecutorService executor;

    private long lastUpdate = 0;

    private Set<String> proxyHosts;

    public ProxyHostManager(final DataSource dataSource) {
        this(dataSource, Executors.newSingleThreadExecutor());
    }

    public ProxyHostManager(final DataSource dataSource, final ExecutorService executor) {
        this.dataSource = dataSource;
        this.executor = executor;
        this.proxyHosts = new HashSet<>();
        String proxyHost = null;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("ezproxy-servers.txt"), StandardCharsets.UTF_8))) {
            while ((proxyHost = reader.readLine()) != null) {
                this.proxyHosts.add(proxyHost);
            }
        } catch (IOException e) {
            throw new LanewebException(e);
        }
    }

    public void destroy() {
        this.executor.shutdownNow();
    }

    public boolean isProxyableHost(final String host) {
        if (host == null) {
            return false;
        }
        updateIfNecessary();
        return this.proxyHosts.contains(host);
    }

    public boolean isProxyableLink(final String link) {
        if (link == null) {
            return false;
        }
        try {
            URL url = new URL(link);
            return isProxyableHost(url.getHost());
        } catch (MalformedURLException e) {
            return false;
        }
    }

    private synchronized void updateIfNecessary() {
        long now = System.currentTimeMillis();
        if (now > this.lastUpdate + UPDATE_INTERVAL) {
            this.lastUpdate = now;
            this.executor.execute(new Runnable() {

                @Override
                public void run() {
                    try {
                        Set<String> newSet = new DatabaseProxyHostSet(ProxyHostManager.this.dataSource);
                        ProxyHostManager.this.proxyHosts = newSet;
                    } catch (LanewebException e) {
                        LOG.error("proxy hosts not updated", e);
                    }
                }
            });
        }
    }
}
