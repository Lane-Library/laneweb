package edu.stanford.irt.laneweb.proxy;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import edu.stanford.irt.laneweb.util.JdbcUtils;

public class EzproxyServersWriter {

    private static final byte[] HJ = "HJ ".getBytes();

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
        + "select substr(url, 0, instr(url,'/',1,3)) as server from urls "
        + "where url like 'https://%' and instr(url,'/',1,3) > 0 "
        + "union "
        + "select url as server from urls "
        + "where url like 'https://%' and instr(url,'/',1,3) = 0 "
        + "union "
        + "select substr(url, 0, instr(url,'/',1,3)) as server from urls "
        + "where url like 'http://%' and instr(url,'/',1,3) > 0 "
        + "union "
        + "select url as server from urls "
        + "where url like 'http://%' and instr(url,'/',1,3) = 0 ";

    private static final byte[] SUL = "HJ jenson.stanford.edu\nHJ socrates.stanford.edu\nHJ library.stanford.edu\nHJ searchworks.stanford.edu"
            .getBytes();

    private DataSource dataSource;
    
    public void write(OutputStream outputStream) throws IOException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = this.dataSource.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                String host = rs.getString(1);
                outputStream.write(HJ);
                outputStream.write(host.getBytes());
                outputStream.write('\n');
            }
            outputStream.write(SUL);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(stmt);
            JdbcUtils.closeConnection(conn);
            outputStream.close();
        }
    }

    public void setDataSource(final DataSource dataSource) {
        if (null == dataSource) {
            throw new IllegalArgumentException("null dataSource");
        }
        this.dataSource = dataSource;
    }
}
