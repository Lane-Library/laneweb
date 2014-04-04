package edu.stanford.irt.laneweb.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
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

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.JdbcUtils;

public class ProxyHostManager {

    private static class DatabaseProxyHostSet extends HashSet<String> {

        private static final long serialVersionUID = 1L;

        private static final String SQL =
                "WITH HOSTS AS "
                + "  ( SELECT DISTINCT URL_HOST AS HOST "
                + "  FROM LMLDB.ELINK_INDEX "
                + "  WHERE ELINK_INDEX.RECORD_TYPE = 'A' "
                + "  UNION "
                + "  SELECT DISTINCT URL_HOST AS HOST "
                + "  FROM LMLDB.ELINK_INDEX, "
                + "    LMLDB.MFHD_MASTER "
                + "  WHERE ELINK_INDEX.RECORD_ID  = MFHD_MASTER.MFHD_ID "
                + "  AND ELINK_INDEX.RECORD_TYPE  = 'M' "
                + "  AND MFHD_MASTER.MFHD_ID NOT IN "
                + "    (SELECT MFHD_ID "
                + "    FROM LMLDB.MFHD_DATA "
                + "    WHERE LOWER(RECORD_SEGMENT) LIKE '%, noproxy%' "
                + "    ) "
                + "  ) "
                + "SELECT HOST FROM HOSTS WHERE HOST NOT LIKE '%.stanford.edu'";

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

    // update every 2 hours
    private static final long UPDATE_INTERVAL = 1000 * 60 * 60 * 2;

    private DataSource dataSource;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private long lastUpdate = 0;

    private Logger log;

    private Set<String> proxyHosts;

    public ProxyHostManager(final DataSource dataSource, final Logger log) throws UnsupportedEncodingException {
        this.dataSource = dataSource;
        this.log = log;
        BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(
                "ezproxy-servers.txt"), Charset.forName("UTF-8")));
        this.proxyHosts = new HashSet<String>();
        String proxyHost = null;
        try {
            while ((proxyHost = reader.readLine()) != null) {
                this.proxyHosts.add(proxyHost);
            }
        } catch (IOException e) {
            throw new LanewebException(e);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                throw new LanewebException(e);
            }
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

                public void run() {
                    try {
                        Set<String> newSet = new DatabaseProxyHostSet(ProxyHostManager.this.dataSource);
                        ProxyHostManager.this.proxyHosts = newSet;
                    } catch (LanewebException e) {
                        ProxyHostManager.this.log.error("proxy hosts not updated", e);
                    }
                }
            });
        }
    }
}
