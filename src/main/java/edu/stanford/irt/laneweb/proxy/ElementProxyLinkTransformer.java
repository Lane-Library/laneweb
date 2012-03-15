package edu.stanford.irt.laneweb.proxy;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ElementProxyLinkTransformer extends AbstractProxyLinkTransformer {

    private StringBuilder builder = new StringBuilder(256);

    private boolean building = false;

    private String elementName;

    @Override
    public void characters(final char[] chars, final int start, final int length) throws SAXException {
        if (this.building) {
            this.builder.append(chars, start, length);
        } else {
            getXMLConsumer().characters(chars, start, length);
        }
    }

    @Override
    public void endElement(final String uri, final String localName, final String qName) throws SAXException {
        if (proxyLinks() && this.elementName.equals(localName)) {
            String proxyURL = createProxyLink(this.builder.toString());
            getXMLConsumer().characters(proxyURL.toCharArray(), 0, proxyURL.length());
            this.building = false;
        }
        getXMLConsumer().endElement(uri, localName, qName);
    }

    public void setElementName(final String elementName) {
        this.elementName = elementName;
    }

    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes atts)
            throws SAXException {
        if (proxyLinks() && this.elementName.equals(localName)) {
            this.builder.setLength(0);
            this.building = true;
        }
        getXMLConsumer().startElement(uri, localName, qName, atts);
    }
}
