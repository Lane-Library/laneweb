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
import java.util.Properties;
import java.util.Set;

import javax.sql.DataSource;

import edu.stanford.irt.laneweb.LanewebException;

public class JDBCProxyServersService implements ProxyServersService {

    private static final byte[] HJ = { 'H', 'J', ' ' };

    private static final String SQL =
            "SELECT DISTINCT URL_HOST AS HOST "
            + "FROM LMLDB.ELINK_INDEX "
            + "WHERE LINK_SUBTYPE         IN ('HTTP','HTTPS') "
            + "AND ELINK_INDEX.RECORD_TYPE = 'A' "
            + "AND URL_HOST NOT LIKE '%.stanford.edu' "
            + "UNION "
            + "SELECT DISTINCT URL_HOST AS HOST "
            + "FROM LMLDB.ELINK_INDEX, "
            + "  LMLDB.MFHD_MASTER, "
            + "  LMLDB.BIB_MFHD, "
            + "  LMLDB.BIB_MASTER "
            + "WHERE ELINK_INDEX.RECORD_ID    = MFHD_MASTER.MFHD_ID "
            + "AND MFHD_MASTER.MFHD_ID = BIB_MFHD.MFHD_ID "
            + "AND BIB_MFHD.BIB_ID = BIB_MASTER.BIB_ID "
            + "AND LINK_SUBTYPE              IN ('HTTP','HTTPS') "
            + "AND ELINK_INDEX.RECORD_TYPE    = 'M' "
            + "AND ELINK_INDEX.RECORD_ID NOT IN "
            + "  (SELECT MFHD_ID "
            + "  FROM LMLDB.MFHD_DATA "
            + "  WHERE LOWER(RECORD_SEGMENT) LIKE '%, noproxy%' "
            + "  ) "
            + "AND URL_HOST NOT LIKE '%.stanford.edu' "
            + "AND MFHD_MASTER.SUPPRESS_IN_OPAC != 'Y' "
            + "AND BIB_MASTER.SUPPRESS_IN_OPAC != 'Y'";

    private static final byte[] SUL;

    private static final byte[] T = { 'T', ' ' };

    private static final byte[] U = { 'U', ' ' };
    static {
        SUL = "T bodoni.stanford.edu\nU http://bodoni.stanford.edu\nHJ bodoni.stanford.edu\n\nT library.stanford.edu\nU http://library.stanford.edu\nHJ library.stanford.edu\n\nT searchworks.stanford.edu\nU http://searchworks.stanford.edu\nHJ searchworks.stanford.edu"
                .getBytes(StandardCharsets.UTF_8);
    }

    private DataSource dataSource;

    private String sql;

    public JDBCProxyServersService(final DataSource dataSource, final Properties sql) {
        this.dataSource = dataSource;
        this.sql = sql.getProperty("ezproxy-servers.query");
    }

    @Override
    public Set<String> getHosts() {
        Set<String> hosts = new HashSet<>();
        try (Connection conn = this.dataSource.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(SQL)) {
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
        } catch (SQLException | IOException e) {
            throw new LanewebException(e);
        }
    }
}
