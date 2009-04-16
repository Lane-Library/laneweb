package edu.stanford.irt.laneweb.eresources;

import java.io.IOException;
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

public class LinkScanGenerator implements Generator {

    private static final String XHTML_NS = "http://www.w3.org/1999/xhtml";

    private DataSource dataSource;

    private static final String sql = "select url, record_type, record_id, title " + "from link, eresource " + "where eresource.eresource_id = link.eresource_id";

    private ThreadLocal<XMLConsumer> xmlConsumer = new ThreadLocal<XMLConsumer>();

    public void generate() throws IOException, SAXException, ProcessingException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        XMLConsumer consumer = this.xmlConsumer.get();
        try {
            conn = this.dataSource.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            consumer.startDocument();
            XMLUtils.startElement(consumer, XHTML_NS, "ul");
            int p = 1;
            String position, url, id, title;
            while (rs.next()) {
                position = " #" + p++ + ' ';
                url = rs.getString(1);
                id = rs.getString(2) + '-' + rs.getString(3);
                title = rs.getString(4);
                XMLUtils.startElement(consumer, XHTML_NS, "li");
                XMLUtils.data(consumer, position);
                XMLUtils.startElement(consumer, XHTML_NS, "ul");
                XMLUtils.startElement(consumer, XHTML_NS, "li");
                AttributesImpl atts = new AttributesImpl();
                atts.addAttribute("", "href", "href", "CDATA", url);
                XMLUtils.startElement(consumer, XHTML_NS, "a", atts);
                XMLUtils.data(consumer, " id: " + id + " title: " + title);
                XMLUtils.endElement(consumer, XHTML_NS, "a");
                XMLUtils.endElement(consumer, XHTML_NS, "li");
                XMLUtils.endElement(consumer, XHTML_NS, "ul");
                XMLUtils.endElement(consumer, XHTML_NS, "li");
            }
            XMLUtils.endElement(consumer, XHTML_NS, "ul");
            consumer.endDocument();
        } catch (SQLException e) {
            throw new ProcessingException(e);
        } finally {
            this.xmlConsumer.set(null);
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

    public void setConsumer(final XMLConsumer xmlConsumer) {
        if (null == xmlConsumer) {
            throw new IllegalArgumentException("null xmlConsumer");
        }
        this.xmlConsumer.set(xmlConsumer);
    }

    public void setDataSource(final DataSource dataSource) {
        if (null == dataSource) {
            throw new IllegalArgumentException("null dataSource");
        }
        this.dataSource = dataSource;
    }

    @SuppressWarnings("unchecked")
    public void setup(final SourceResolver arg0, final Map arg1, final String arg2, final Parameters arg3) throws ProcessingException, SAXException,
            IOException {
    }
}
