package edu.stanford.irt.laneweb.cocoon;

import org.apache.cocoon.xml.XMLConsumer;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public abstract class AbstractTransformer extends AbstractSitemapModelComponent implements LanewebTransformer {

    protected XMLConsumer xmlConsumer;

    public void characters(final char[] ch, final int start, final int length) throws SAXException {
        this.xmlConsumer.characters(ch, start, length);
    }

    public void comment(final char[] ch, final int start, final int length) throws SAXException {
        this.xmlConsumer.comment(ch, start, length);
    }

    public void endCDATA() throws SAXException {
        this.xmlConsumer.endCDATA();
    }

    public void endDocument() throws SAXException {
        this.xmlConsumer.endDocument();
    }

    public void endDTD() throws SAXException {
        this.xmlConsumer.endDTD();
    }

    public void endElement(final String uri, final String localName, final String qName) throws SAXException {
        this.xmlConsumer.endElement(uri, localName, qName);
    }

    public void endEntity(final String name) throws SAXException {
        this.xmlConsumer.endEntity(name);
    }

    public void endPrefixMapping(final String prefix) throws SAXException {
        this.xmlConsumer.endPrefixMapping(prefix);
    }

    public void ignorableWhitespace(final char[] ch, final int start, final int length) throws SAXException {
        this.xmlConsumer.ignorableWhitespace(ch, start, length);
    }

    public void processingInstruction(final String target, final String data) throws SAXException {
        this.xmlConsumer.processingInstruction(target, data);
    }

    public void setConsumer(final XMLConsumer xmlConsumer) {
        this.xmlConsumer = xmlConsumer;
    }

    public void setDocumentLocator(final Locator locator) {
        this.xmlConsumer.setDocumentLocator(locator);
    }

    public void skippedEntity(final String name) throws SAXException {
        this.xmlConsumer.skippedEntity(name);
    }

    public void startCDATA() throws SAXException {
        this.xmlConsumer.startCDATA();
    }

    public void startDocument() throws SAXException {
        this.xmlConsumer.startDocument();
    }

    public void startDTD(final String name, final String publicId, final String systemId) throws SAXException {
        this.xmlConsumer.startDTD(name, publicId, systemId);
    }

    public void startElement(final String uri, final String localName, final String qName, final Attributes atts)
            throws SAXException {
        this.xmlConsumer.startElement(uri, localName, qName, atts);
    }

    public void startEntity(final String name) throws SAXException {
        this.xmlConsumer.startEntity(name);
    }

    public void startPrefixMapping(final String prefix, final String uri) throws SAXException {
        this.xmlConsumer.startPrefixMapping(prefix, uri);
    }
}
