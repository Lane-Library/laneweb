package edu.stanford.irt.laneweb.eresources;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.Generator;
import org.apache.cocoon.xml.XMLConsumer;
import org.apache.cocoon.xml.XMLUtils;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.laneweb.JdbcUtils;

public class LinkScanGenerator implements Generator {

    private static final String SQL = "select url, record_type, record_id, title " + "from link, eresource "
            + "where eresource.eresource_id = link.eresource_id";

    private static final String XHTML_NS = "http://www.w3.org/1999/xhtml";

    private XMLConsumer consumer;

    private DataSource dataSource;

    public void generate() throws SAXException, ProcessingException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = this.dataSource.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(SQL);
            this.consumer.startDocument();
            XMLUtils.startElement(this.consumer, XHTML_NS, "ul");
            int p = 1;
            String position, url, id, title;
            while (rs.next()) {
                position = " #" + p++ + ' ';
                url = rs.getString(1);
                id = rs.getString(2) + '-' + rs.getString(3);
                title = rs.getString(4);
                XMLUtils.startElement(this.consumer, XHTML_NS, "li");
                XMLUtils.data(this.consumer, position);
                XMLUtils.startElement(this.consumer, XHTML_NS, "ul");
                XMLUtils.startElement(this.consumer, XHTML_NS, "li");
                AttributesImpl atts = new AttributesImpl();
                atts.addAttribute("", "href", "href", "CDATA", url);
                XMLUtils.startElement(this.consumer, XHTML_NS, "a", atts);
                XMLUtils.data(this.consumer, " id: " + id + " title: " + title);
                XMLUtils.endElement(this.consumer, XHTML_NS, "a");
                XMLUtils.endElement(this.consumer, XHTML_NS, "li");
                XMLUtils.endElement(this.consumer, XHTML_NS, "ul");
                XMLUtils.endElement(this.consumer, XHTML_NS, "li");
            }
            XMLUtils.endElement(this.consumer, XHTML_NS, "ul");
            this.consumer.endDocument();
        } catch (SQLException e) {
            throw new ProcessingException(e);
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(stmt);
            JdbcUtils.closeConnection(conn);
        }
    }

    public void setConsumer(final XMLConsumer consumer) {
        if (null == consumer) {
            throw new IllegalArgumentException("null consumer");
        }
        this.consumer = consumer;
    }

    public void setDataSource(final DataSource dataSource) {
        if (null == dataSource) {
            throw new IllegalArgumentException("null dataSource");
        }
        this.dataSource = dataSource;
    }

    @SuppressWarnings("unchecked")
    public void setup(final SourceResolver arg0, final Map arg1, final String arg2, final Parameters arg3) {
    }
}
