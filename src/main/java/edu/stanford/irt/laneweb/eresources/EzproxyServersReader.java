package edu.stanford.irt.laneweb.eresources;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.reading.Reader;
import org.xml.sax.SAXException;

public class EzproxyServersReader implements Reader {

    private DataSource dataSource;

    private ThreadLocal<OutputStream> outputStream = new ThreadLocal<OutputStream>();

    public void setDataSource(final DataSource dataSource) {
        if (null == dataSource) {
            throw new IllegalArgumentException("null dataSource");
        }
        this.dataSource = dataSource;
    }

    public void generate() throws IOException, SAXException, ProcessingException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        OutputStream out = this.outputStream.get();
        try {
            conn = this.dataSource.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(this.sql);
            while (rs.next()) {
                out.write(this.hj);
                out.write(rs.getString(1).getBytes());
                out.write('\n');
            }

        } catch (SQLException e) {
            throw new ProcessingException(e);
        } finally {
            this.outputStream.set(null);
            if (null != rs) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    // ?
                }
            }
            if (null != stmt) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    // ?
                }
            }
            if (null != conn) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    // ?
                }
            }
        }
    }

    public long getLastModified() {
        return 0;
    }

    @SuppressWarnings("unchecked")
    public void setup(final SourceResolver arg0, final Map arg1, final String arg2, final Parameters arg3) throws ProcessingException,
            SAXException, IOException {
    }

    public String getMimeType() {
        return "text/plain";
    }

    public void setOutputStream(final OutputStream outputStream) throws IOException {
        if (null == outputStream) {
            throw new IllegalArgumentException("null outputStream");
        }
        this.outputStream.set(outputStream);
    }

    public boolean shouldSetContentLength() {
        return false;
    }

    private final byte[] hj = "HJ ".getBytes();

    private final String sql = "with urls as ( " + "select url from link, version " + "where link.version_id = version.version_id "
            + "and proxy = 'T' " + "and url like 'http%' " + "union " + "select url from h_link, h_version "
            + "where h_link.version_id = h_version.version_id " + "and proxy = 'T' " + "and url like 'http%' " + ") "
            + "select substr(url, 9, instr(url,'/',1,3) - 9) as server from urls "
            + "where url like 'https://%' and instr(url,'/',1,3) > 0 " + "union " + "select substr(url, 9) as server from urls "
            + "where url like 'https://%' and instr(url,'/',1,3) = 0 " + "union "
            + "select substr(url, 8, instr(url,'/',1,3) - 8) as server from urls "
            + "where url like 'http://%' and instr(url,'/',1,3) > 0 " + "union " + "select substr(url, 8) as server from urls "
            + "where url like 'http://%' and instr(url,'/',1,3) = 0 ";

}
