package edu.stanford.irt.laneweb.proxy;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.sql.DataSource;

import edu.stanford.irt.laneweb.LanewebException;

public class JDBCProxyServersService implements ProxyServersService {

    private static final byte[] HJ = { 'H', 'J', ' ' };

    private static final byte[] SUL;

    private static final byte[] T = { 'T', ' ' };

    private static final byte[] U = { 'U', ' ' };
    static {
        SUL = "T bodoni.stanford.edu\nU http://bodoni.stanford.edu\nHJ bodoni.stanford.edu\n\nT library.stanford.edu\nU http://library.stanford.edu\nHJ library.stanford.edu\n\nT searchworks.stanford.edu\nU http://searchworks.stanford.edu\nHJ searchworks.stanford.edu"
                .getBytes(StandardCharsets.UTF_8);
    }

    private DataSource dataSource;

    private String ezproxyServersSQL;

    private String proxyHostsSQL;

    public JDBCProxyServersService(final DataSource dataSource, final String proxyHostsSQL,
            final String ezproxyServersSQL) {
        this.dataSource = dataSource;
        this.proxyHostsSQL = proxyHostsSQL;
        this.ezproxyServersSQL = ezproxyServersSQL;
    }

    @Override
    public Set<String> getHosts() {
        Set<String> hosts = new HashSet<>();
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
        Objects.requireNonNull(outputStream, "null outputStream");
        try (Connection conn = this.dataSource.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(this.ezproxyServersSQL);
                OutputStream out = outputStream) {
            while (rs.next()) {
                String host = rs.getString(1);
                out.write(T);
                out.write(host.getBytes(StandardCharsets.UTF_8));
                out.write('\n');
                out.write(U);
                out.write(host.getBytes(StandardCharsets.UTF_8));
                out.write('\n');
                out.write(HJ);
                out.write(host.getBytes(StandardCharsets.UTF_8));
                out.write('\n');
                out.write('\n');
            }
            out.write(SUL);
        } catch (SQLException | IOException e) {
            throw new LanewebException(e);
        }
    }
}
