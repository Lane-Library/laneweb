package edu.stanford.irt.laneweb.proxy;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.JdbcUtils;

public class EzproxyServersWriter {

    private static final byte[] HJ = { 'H', 'J', ' ' };

    private static final String UNION = "union ";

    private static final String SQL =
        "with urls as ( "
        + "select url from link, version "
        + "where link.version_id = version.version_id "
        + "and proxy = 'T' "
        + "and url like 'http%' "
        + "and url not like '%.stanford.edu%' "
        + UNION
        + "select url from h_link, h_version "
        + "where h_link.version_id = h_version.version_id "
        + "and proxy = 'T' "
        + "and url like 'http%' "
        + "and url not like '%.stanford.edu%' "
        + ") "
        + "select substr(url, 0, instr(url,'/',1,3)-1) as server from urls "
        + "where url like 'https://%' and instr(url,'/',1,3) > 0 "
        + UNION
        + "select url as server from urls "
        + "where url like 'https://%' and instr(url,'/',1,3) = 0 "
        + UNION
        + "select substr(url, 0, instr(url,'/',1,3)-1) as server from urls "
        + "where url like 'http://%' and instr(url,'/',1,3) > 0 "
        + UNION 
        + "select url as server from urls "
        + "where url like 'http://%' and instr(url,'/',1,3) = 0 ";

    private static final byte[] SUL;

    private static final byte[] T = { 'T', ' ' };

    private static final byte[] U = { 'U', ' ' };

    private static final String UTF8 = "UTF-8";

    static {
        try {
            SUL = "T jenson.stanford.edu\nU http://jenson.stanford.edu\nHJ jenson.stanford.edu\n\nT socrates.stanford.edu\nU http://socrates.stanford.edu\nHJ socrates.stanford.edu\n\nT library.stanford.edu\nU http://library.stanford.edu\nHJ library.stanford.edu\n\nT searchworks.stanford.edu\nU http://searchworks.stanford.edu\nHJ searchworks.stanford.edu"
                    .getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private DataSource dataSource;

    public EzproxyServersWriter(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void write(final OutputStream outputStream) throws IOException {
        if (outputStream == null) {
            throw new LanewebException("null outputStream");
        }
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = this.dataSource.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                String host = rs.getString(1);
                outputStream.write(T);
                outputStream.write(host.getBytes(UTF8));
                outputStream.write('\n');
                outputStream.write(U);
                outputStream.write(host.getBytes(UTF8));
                outputStream.write('\n');
                outputStream.write(HJ);
                outputStream.write(host.getBytes(UTF8));
                outputStream.write('\n');
                outputStream.write('\n');
            }
            outputStream.write(SUL);
        } catch (SQLException e) {
            throw new LanewebException(e);
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(stmt);
            JdbcUtils.closeConnection(conn);
            outputStream.close();
        }
    }
}
