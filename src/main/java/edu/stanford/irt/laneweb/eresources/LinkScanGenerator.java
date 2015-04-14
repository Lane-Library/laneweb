package edu.stanford.irt.laneweb.eresources;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.JdbcUtils;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class LinkScanGenerator extends AbstractGenerator {

    private static final int ID = 3;

    private static final String SQL = "select url, record_type, record_id, title " + "from link, eresource "
            + "where eresource.eresource_id = link.eresource_id";

    private static final int TITLE = 4;

    private static final int TYPE = 2;

    private static final int URL = 1;

    private static final String XHTML_NS = "http://www.w3.org/1999/xhtml";

    private DataSource dataSource;

    public LinkScanGenerator(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = this.dataSource.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(SQL);
            xmlConsumer.startDocument();
            XMLUtils.startElement(xmlConsumer, XHTML_NS, "ul");
            int p = 1;
            String position, url, id, title;
            while (rs.next()) {
                position = " #" + p++ + ' ';
                url = rs.getString(URL);
                if (url == null) {
                    url = "NULL URL";
                }
                id = rs.getString(TYPE) + '-' + rs.getString(ID);
                title = rs.getString(TITLE);
                if (title == null) {
                    title = "NULL TITLE";
                }
                XMLUtils.startElement(xmlConsumer, XHTML_NS, "li");
                XMLUtils.data(xmlConsumer, position);
                XMLUtils.startElement(xmlConsumer, XHTML_NS, "ul");
                XMLUtils.startElement(xmlConsumer, XHTML_NS, "li");
                AttributesImpl atts = new AttributesImpl();
                atts.addAttribute("", "href", "href", "CDATA", url);
                XMLUtils.startElement(xmlConsumer, XHTML_NS, "a", atts);
                XMLUtils.data(xmlConsumer, " id: " + id + " title: " + title);
                XMLUtils.endElement(xmlConsumer, XHTML_NS, "a");
                XMLUtils.endElement(xmlConsumer, XHTML_NS, "li");
                XMLUtils.endElement(xmlConsumer, XHTML_NS, "ul");
                XMLUtils.endElement(xmlConsumer, XHTML_NS, "li");
            }
            XMLUtils.endElement(xmlConsumer, XHTML_NS, "ul");
            xmlConsumer.endDocument();
        } catch (SQLException e) {
            throw new LanewebException(e);
        } catch (SAXException e) {
            throw new LanewebException(e);
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(stmt);
            JdbcUtils.closeConnection(conn);
        }
    }
}
