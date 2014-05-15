package edu.stanford.irt.laneweb.cme;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;

/**
 * Transformer to rewrite data when a LDAPData.EMRID value is present. Elements to rewrite include: - url element of
 * metasearch results when value is known CME host
 * 
 * @author ryanmax
 */
public class SearchResultCMELinkTransformer extends AbstractCMELinkTransformer {

    private static final String URL = "url";

    private StringBuilder characters = new StringBuilder();

    private boolean isSearchUrlElement = false;

    private XMLConsumer xmlConsumer;

    @Override
    public void characters(final char[] ch, final int start, final int length) throws SAXException {
        if (this.isSearchUrlElement) {
            this.characters.append(ch, start, length);
        } else {
            this.xmlConsumer.characters(ch, start, length);
        }
    }

    @Override
    public void endElement(final String uri, final String localName, final String qName) throws SAXException {
        if (this.isSearchUrlElement) {
            String value = this.characters.toString();
            if (isCMEHost(value)) {
                StringBuilder link = new StringBuilder();
                link.append(getBasePath());
                link.append(CME_REDIRECT);
                link.append(value);
                this.xmlConsumer.characters(link.toString().toCharArray(), 0, link.length());
            }
            this.isSearchUrlElement = false;
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
        if (URL.equals(localName)) {
            this.isSearchUrlElement = true;
            this.characters.setLength(0);
        }
        this.xmlConsumer.startElement(uri, localName, qName, atts);
    }
}
