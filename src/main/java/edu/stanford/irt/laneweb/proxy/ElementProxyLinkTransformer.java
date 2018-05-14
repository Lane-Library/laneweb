package edu.stanford.irt.laneweb.proxy;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;

public class ElementProxyLinkTransformer extends AbstractProxyLinkTransformer {

    private static final int BUILDER_CAPACITY = 256;

    private StringBuilder builder = new StringBuilder(BUILDER_CAPACITY);

    private boolean building;

    private String elementName;

    private XMLConsumer xmlConsumer;

    public ElementProxyLinkTransformer(final String elementName) {
        this.elementName = elementName;
    }

    @Override
    public void characters(final char[] chars, final int start, final int length) throws SAXException {
        if (this.building) {
            this.builder.append(chars, start, length);
        } else {
            this.xmlConsumer.characters(chars, start, length);
        }
    }

    @Override
    public void endElement(final String uri, final String localName, final String qName) throws SAXException {
        if (this.elementName.equals(localName)) {
            String proxyURL = createProxyLink(this.builder.toString());
            this.xmlConsumer.characters(proxyURL.toCharArray(), 0, proxyURL.length());
            this.building = false;
        }
        this.xmlConsumer.endElement(uri, localName, qName);
    }

    @Override
    public void setXMLConsumer(final XMLConsumer xmlConsumer) {
        this.xmlConsumer = xmlConsumer;
        super.setXMLConsumer(xmlConsumer);
    }

    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes atts)
            throws SAXException {
        if (this.elementName.equals(localName)) {
            this.builder.setLength(0);
            this.building = true;
        }
        this.xmlConsumer.startElement(uri, localName, qName, atts);
    }
}
