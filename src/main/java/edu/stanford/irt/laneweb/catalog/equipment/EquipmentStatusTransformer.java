package edu.stanford.irt.laneweb.catalog.equipment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.pipeline.Transformer;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class EquipmentStatusTransformer implements Transformer {

    private static final String AVAILABLE_QUERY = "SELECT bi.bib_id, COUNT(*) FROM lmldb.bib_item bi, "
            + "  lmldb.item_status item_status_1 LEFT OUTER JOIN lmldb.item_status item_status_2 "
            + "ON (item_status_1.item_id          = item_status_2.item_id "
            + "AND item_status_1.item_status_date < item_status_2.item_status_date) "
            + "WHERE item_status_2.item_id       IS NULL "
            + "AND bi.item_id                     = item_status_1.item_id " + "AND item_status_1.item_status      = 1 "
            + "AND bi.bib_id in (select regexp_substr(?,'[^,]+', 1, level) from dual connect by regexp_substr(?, '[^,]+', 1, level) is not null) "
            + "GROUP BY bi.bib_id";

    private static final int BIB_ID = 1;

    private static final int COUNT = 2;

    private static final String DIV = "div";

    private static final String SPAN = "span";

    private static final String XHTML_NAMESPACE = "http://www.w3.org/1999/xhtml";

    private DataSource dataSource;

    private StringBuilder ids = new StringBuilder();

    private XMLConsumer xmlConsumer;

    public EquipmentStatusTransformer(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void characters(final char[] ch, final int start, final int length) throws SAXException {
        this.xmlConsumer.characters(ch, start, length);
    }

    @Override
    public void comment(final char[] ch, final int start, final int length) throws SAXException {
        this.xmlConsumer.comment(ch, start, length);
    }

    @Override
    public void endCDATA() throws SAXException {
        this.xmlConsumer.endCDATA();
    }

    @Override
    public void endDocument() throws SAXException {
        this.xmlConsumer.endDocument();
    }

    @Override
    public void endDTD() throws SAXException {
        this.xmlConsumer.endDTD();
    }

    @Override
    public void endElement(final String uri, final String localName, final String qName) throws SAXException {
        if ("body".equals(localName)) {
            generateStatus();
        }
        this.xmlConsumer.endElement(uri, localName, qName);
    }

    @Override
    public void endEntity(final String name) throws SAXException {
        this.xmlConsumer.endEntity(name);
    }

    @Override
    public void endPrefixMapping(final String prefix) throws SAXException {
        this.xmlConsumer.endPrefixMapping(prefix);
    }

    @Override
    public void ignorableWhitespace(final char[] ch, final int start, final int length) throws SAXException {
        this.xmlConsumer.ignorableWhitespace(ch, start, length);
    }

    @Override
    public void processingInstruction(final String target, final String data) throws SAXException {
        this.xmlConsumer.processingInstruction(target, data);
    }

    @Override
    public void setDocumentLocator(final Locator locator) {
        this.xmlConsumer.setDocumentLocator(locator);
    }

    @Override
    public void setXMLConsumer(final XMLConsumer xmlConsumer) {
        this.xmlConsumer = xmlConsumer;
    }

    @Override
    public void skippedEntity(final String name) throws SAXException {
        this.xmlConsumer.skippedEntity(name);
    }

    @Override
    public void startCDATA() throws SAXException {
        this.xmlConsumer.startCDATA();
    }

    @Override
    public void startDocument() throws SAXException {
        this.xmlConsumer.startDocument();
    }

    @Override
    public void startDTD(final String name, final String publicId, final String systemId) throws SAXException {
        this.xmlConsumer.startDTD(name, publicId, systemId);
    }

    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes atts)
            throws SAXException {
        if ("li".equals(localName)) {
            this.ids.append(',').append(atts.getValue("data-bibid"));
        }
        this.xmlConsumer.startElement(uri, localName, qName, atts);
    }

    @Override
    public void startEntity(final String name) throws SAXException {
        this.xmlConsumer.startEntity(name);
    }

    @Override
    public void startPrefixMapping(final String prefix, final String uri) throws SAXException {
        this.xmlConsumer.startPrefixMapping(prefix, uri);
    }

    private void generateStatus() throws SAXException {
        try (Connection conn = this.dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(AVAILABLE_QUERY)) {
            // remove leading comma
            this.ids.deleteCharAt(0);
            String idList = this.ids.toString();
            stmt.setString(1, idList);
            stmt.setString(2, idList);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    XMLUtils.startElement(this.xmlConsumer, XHTML_NAMESPACE, DIV);
                    XMLUtils.createElementNS(this.xmlConsumer, XHTML_NAMESPACE, SPAN, rs.getString(BIB_ID));
                    XMLUtils.createElementNS(this.xmlConsumer, XHTML_NAMESPACE, SPAN, rs.getString(COUNT));
                    XMLUtils.endElement(this.xmlConsumer, XHTML_NAMESPACE, DIV);
                }
            }
        } catch (SQLException e) {
            throw new LanewebException(e);
        }
    }
}
