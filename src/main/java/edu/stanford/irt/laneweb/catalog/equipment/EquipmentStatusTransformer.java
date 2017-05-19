package edu.stanford.irt.laneweb.catalog.equipment;

import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.pipeline.Transformer;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class EquipmentStatusTransformer implements Transformer {

    private static final String DIV = "div";

    private static final String SPAN = "span";

    private static final String XHTML_NAMESPACE = "http://www.w3.org/1999/xhtml";

    private StringBuilder ids = new StringBuilder();

    private EquipmentService service;

    private XMLConsumer xmlConsumer;

    public EquipmentStatusTransformer(final EquipmentService service) {
        this.service = service;
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

    private void generateStatus() {
        // remove leading comma
        this.ids.deleteCharAt(0);
        List<EquipmentStatus> status = this.service.getStatus(this.ids.toString());
        status.stream().forEach(s -> statusToSAX(s));
    }

    private void statusToSAX(final EquipmentStatus status) {
        try {
            XMLUtils.startElement(this.xmlConsumer, XHTML_NAMESPACE, DIV);
            XMLUtils.createElementNS(this.xmlConsumer, XHTML_NAMESPACE, SPAN, status.getBibID());
            XMLUtils.createElementNS(this.xmlConsumer, XHTML_NAMESPACE, SPAN, status.getCount());
            XMLUtils.endElement(this.xmlConsumer, XHTML_NAMESPACE, DIV);
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
