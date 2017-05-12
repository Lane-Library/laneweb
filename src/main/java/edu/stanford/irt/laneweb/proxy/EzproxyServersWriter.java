package edu.stanford.irt.laneweb.proxy;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Properties;

import javax.sql.DataSource;

import edu.stanford.irt.laneweb.LanewebException;

public class EzproxyServersWriter {

    private static final byte[] HJ = { 'H', 'J', ' ' };

    private static final byte[] SUL;

    private static final byte[] T = { 'T', ' ' };

    private static final byte[] U = { 'U', ' ' };

    static {
        SUL = "T bodoni.stanford.edu\nU http://bodoni.stanford.edu\nHJ bodoni.stanford.edu\n\nT library.stanford.edu\nU http://library.stanford.edu\nHJ library.stanford.edu\n\nT searchworks.stanford.edu\nU http://searchworks.stanford.edu\nHJ searchworks.stanford.edu"
                .getBytes(StandardCharsets.UTF_8);
    }

    private DataSource dataSource;

    private String sql;

    public EzproxyServersWriter(final DataSource dataSource, final Properties sql) {
        this.dataSource = dataSource;
        this.sql = sql.getProperty("ezproxy-servers.query");
    }

    public void write(final OutputStream outputStream) throws IOException {
        Objects.requireNonNull(outputStream, "null outputStream");
        try (Connection conn = this.dataSource.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(this.sql);
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
        } catch (SQLException e) {
            throw new LanewebException(e);
        }
    }
}
