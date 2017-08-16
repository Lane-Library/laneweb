package edu.stanford.irt.laneweb.proxy;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import javax.sql.DataSource;

import edu.stanford.irt.laneweb.LanewebException;

public class JDBCProxyServersService implements ProxyServersService {

    private static final byte[] COLON_443 = { ':', '4', '4', '3' };

    private static final byte[] HJ = { 'H', 'J', ' ' };

    private static final byte[] T = { 'T', ' ' };

    private static final byte[] U_HTTPS = { 'U', ' ', 'h', 't', 't', 'p', 's', ':', '/', '/' };

    private DataSource dataSource;

    private String proxyHostsSQL;

    public JDBCProxyServersService(final DataSource dataSource, final String proxyHostsSQL) {
        this.dataSource = dataSource;
        this.proxyHostsSQL = proxyHostsSQL;
    }

    @Override
    public Set<String> getHosts() {
        Set<String> hosts = new LinkedHashSet<>();
        try (Connection conn = this.dataSource.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(this.proxyHostsSQL)) {
            while (rs.next()) {
                hosts.add(rs.getString(1));
            }
            hosts.add("bodoni.stanford.edu");
            hosts.add("library.stanford.edu");
            hosts.add("searchworks.stanford.edu");
        } catch (SQLException e) {
            throw new LanewebException(e);
        }
        return hosts;
    }

    @Override
    public void write(final OutputStream outputStream) {
        Set<String> hosts = getHosts();
        Objects.requireNonNull(outputStream, "null outputStream");
        try (OutputStream out = outputStream) {
            for (String host : hosts) {
                byte[] hostBytes = host.getBytes(StandardCharsets.UTF_8);
                out.write(T);
                out.write(hostBytes);
                out.write('\n');
                out.write(U_HTTPS);
                out.write(hostBytes);
                out.write('\n');
                out.write(HJ);
                out.write(hostBytes);
                out.write('\n');
                out.write(HJ);
                out.write(hostBytes);
                out.write(COLON_443);
                out.write('\n');
                out.write('\n');
            }
        } catch (IOException e) {
            throw new LanewebException(e);
        }
    }
}
