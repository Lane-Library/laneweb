package edu.stanford.irt.laneweb.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sql.DataSource;

import edu.stanford.irt.laneweb.JdbcUtils;

public class ProxyHostManager {

    @SuppressWarnings("serial")
    private static class DatabaseProxyHostSet extends HashSet<String> {

        private static final String SQL =
            "with urls as ( "
            + "select url from link, version "
            + "where link.version_id = version.version_id "
            + "and proxy = 'T' "
            + "and url like 'http%' "
            + "and url not like '%.stanford.edu%' "
            + "union "
            + "select url from h_link, h_version "
            + "where h_link.version_id = h_version.version_id "
            + "and proxy = 'T' "
            + "and url like 'http%' "
            + "and url not like '%.stanford.edu%' "
            + ") "
            + "select substr(url, 9, instr(url,'/',1,3) - 9) as server from urls "
            + "where url like 'https://%' and instr(url,'/',1,3) > 0 "
            + "union "
            + "select substr(url, 9) as server from urls "
            + "where url like 'https://%' and instr(url,'/',1,3) = 0 "
            + "union "
            + "select substr(url, 8, instr(url,'/',1,3) - 8) as server from urls "
            + "where url like 'http://%' and instr(url,'/',1,3) > 0 "
            + "union "
            + "select substr(url, 8) as server from urls "
            + "where url like 'http://%' and instr(url,'/',1,3) = 0 ";

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
                add("jensen.stanford.edu");
                add("socrates.stanford.edu");
                add("library.stanford.edu");
            } catch (SQLException e) {
                throw new IllegalStateException(e);
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

    private Set<String> proxyHosts;

    public ProxyHostManager() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(
                "ezproxy-servers.txt")));
        this.proxyHosts = new HashSet<String>();
        String proxyHost = null;
        try {
            while ((proxyHost = reader.readLine()) != null) {
                this.proxyHosts.add(proxyHost);
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void destroy() {
        this.executor.shutdownNow();
    }

    public boolean isProxyableHost(final String host) {
        if (null == host) {
            throw new IllegalArgumentException("null host");
        }
        updateIfNecessary();
        return this.proxyHosts.contains(host);
    }

    public boolean isProxyableLink(final String link) {
        if (null == link) {
            throw new IllegalArgumentException("null link");
        }
        try {
            URL url = new URL(link);
            return isProxyableHost(url.getHost());
        } catch (MalformedURLException e) {
            return false;
        }
    }

    public void setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private synchronized void updateIfNecessary() {
        long now = System.currentTimeMillis();
        if (now > this.lastUpdate + UPDATE_INTERVAL) {
            this.lastUpdate = now;
            this.executor.execute(new Runnable() {

                public void run() {
                    Set<String> newSet = new DatabaseProxyHostSet(ProxyHostManager.this.dataSource);
                    ProxyHostManager.this.proxyHosts = newSet;
                }
            });
        }
    }
}
