package edu.stanford.irt.laneweb.eresources;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.reading.Reader;

import edu.stanford.irt.laneweb.JdbcUtils;

public class EzproxyServersReader implements Reader {

    private static final byte[] HJ = "HJ ".getBytes();

    private static final String SQL =
        "with urls as ( "
        + "select url from link, version "
        + "where link.version_id = version.version_id "
        + "and proxy = 'T' "
        + "and url like 'http%' "
        + "union "
        + "select url from h_link, h_version "
        + "where h_link.version_id = h_version.version_id "
        + "and proxy = 'T' "
        + "and url like 'http%' "
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

    private DataSource dataSource;

    private Set<String> excludedHosts;

    private OutputStream outputStream;

    public void generate() throws IOException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = this.dataSource.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                String host = rs.getString(1);
                if (!this.excludedHosts.contains(host)) {
                    this.outputStream.write(HJ);
                    this.outputStream.write(host.getBytes());
                    this.outputStream.write('\n');
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(stmt);
            JdbcUtils.closeConnection(conn);
        }
    }

    public long getLastModified() {
        return 0;
    }

    public String getMimeType() {
        return "text/plain";
    }

    public void setDataSource(final DataSource dataSource) {
        if (null == dataSource) {
            throw new IllegalArgumentException("null dataSource");
        }
        this.dataSource = dataSource;
    }

    public void setExcludedHosts(final Set<String> excludedHosts) {
        if (null == excludedHosts) {
            throw new IllegalArgumentException("null excludedHosts");
        }
        this.excludedHosts = excludedHosts;
    }

    public void setOutputStream(final OutputStream outputStream) {
        if (null == outputStream) {
            throw new IllegalArgumentException("null outputStream");
        }
        this.outputStream = outputStream;
    }

    @SuppressWarnings("unchecked")
    public void setup(final SourceResolver arg0, final Map arg1, final String arg2, final Parameters arg3) {
    }

    public boolean shouldSetContentLength() {
        return false;
    }
}
