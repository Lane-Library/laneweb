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

public class ExtensionsSuggestReader implements Reader {

    private DataSource dataSource;

    private ThreadLocal<OutputStream> outputStream = new ThreadLocal<OutputStream>();

    private ThreadLocal<String> query = new ThreadLocal<String>();

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
        String q = this.query.get();
        try {
            conn = this.dataSource.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(this.sql_1 + q + this.sql_2);
            out.write(("[\"" + q + "\", [").getBytes());
            String maybeComma = "\"";
            while (rs.next()) {
                out.write((maybeComma + rs.getString(1) + '"').getBytes());
                maybeComma = ",\"";
            }
            out.write("]]".getBytes());

        } catch (SQLException e) {
            throw new ProcessingException(e);
        } finally {
            this.outputStream.set(null);
            this.query.set(null);
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

    public void setup(final SourceResolver arg0, final Map arg1, final String arg2, final Parameters params) throws ProcessingException,
            SAXException, IOException {
        String q = params.getParameter("query", null);
        if (null == this.query) {
            throw new ProcessingException("null query");
        }
        // remove quotes and apostrophes
        if ((q.indexOf('\'') > -1) || (q.indexOf('"') > -1)) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < q.length(); i++) {
                char c = q.charAt(i);
                if (('\'' != c) && ('"' != c)) {
                    sb.append(c);
                }
            }
            q = sb.toString();
        }
        this.query.set(q);
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

    private final String sql_1 = "select title from eresource where lower(title) like lower('%";

    private final String sql_2 = "%') and rownum < 20 order by title";

}
