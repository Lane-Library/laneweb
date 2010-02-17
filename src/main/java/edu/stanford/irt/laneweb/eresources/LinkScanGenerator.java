package edu.stanford.irt.laneweb.eresources;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.cocoon.xml.XMLUtils;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.laneweb.JdbcUtils;
import edu.stanford.irt.laneweb.cocoon.AbstractGenerator;

public class LinkScanGenerator extends AbstractGenerator {

    private static final String SQL = "select url, record_type, record_id, title " + "from link, eresource "
            + "where eresource.eresource_id = link.eresource_id";

    private static final String XHTML_NS = "http://www.w3.org/1999/xhtml";

    private DataSource dataSource;

    public void generate() throws SAXException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = this.dataSource.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(SQL);
            this.xmlConsumer.startDocument();
            XMLUtils.startElement(this.xmlConsumer, XHTML_NS, "ul");
            int p = 1;
            String position, url, id, title;
            while (rs.next()) {
                position = " #" + p++ + ' ';
                url = rs.getString(1);
                id = rs.getString(2) + '-' + rs.getString(3);
                title = rs.getString(4);
                XMLUtils.startElement(this.xmlConsumer, XHTML_NS, "li");
                XMLUtils.data(this.xmlConsumer, position);
                XMLUtils.startElement(this.xmlConsumer, XHTML_NS, "ul");
                XMLUtils.startElement(this.xmlConsumer, XHTML_NS, "li");
                AttributesImpl atts = new AttributesImpl();
                atts.addAttribute("", "href", "href", "CDATA", url);
                XMLUtils.startElement(this.xmlConsumer, XHTML_NS, "a", atts);
                XMLUtils.data(this.xmlConsumer, " id: " + id + " title: " + title);
                XMLUtils.endElement(this.xmlConsumer, XHTML_NS, "a");
                XMLUtils.endElement(this.xmlConsumer, XHTML_NS, "li");
                XMLUtils.endElement(this.xmlConsumer, XHTML_NS, "ul");
                XMLUtils.endElement(this.xmlConsumer, XHTML_NS, "li");
            }
            XMLUtils.endElement(this.xmlConsumer, XHTML_NS, "ul");
            this.xmlConsumer.endDocument();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(stmt);
            JdbcUtils.closeConnection(conn);
        }
    }

    public void setDataSource(final DataSource dataSource) {
        if (null == dataSource) {
            throw new IllegalArgumentException("null dataSource");
        }
        this.dataSource = dataSource;
    }
}
