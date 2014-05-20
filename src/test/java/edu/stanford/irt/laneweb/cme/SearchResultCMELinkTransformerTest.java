package edu.stanford.irt.laneweb.cme;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;

public class SearchResultCMELinkTransformerTest {

    private Attributes attributes;

    private SearchResultCMELinkTransformer transformer;

    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() throws Exception {
        this.transformer = new SearchResultCMELinkTransformer();
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.transformer.setXMLConsumer(this.xmlConsumer);
    }

    @Test
    public void testCharacters() throws SAXException {
        this.xmlConsumer.startElement("uri", "url", "qName", this.attributes);
        this.xmlConsumer.endElement("uri", "url", "qName");
        replay(this.xmlConsumer);
        this.transformer.startElement("uri", "url", "qName", this.attributes);
        this.transformer.characters("/foo".toCharArray(), 0, 4);
        this.transformer.endElement("uri", "url", "qName");
        verify(this.xmlConsumer);
    }

    @Test
    public void testCmeCharacters() throws SAXException {
        this.xmlConsumer.startElement("uri", "url", "qName", this.attributes);
        this.xmlConsumer.characters(aryEq("null/redirect/cme?url=http://www.uptodate.com/online".toCharArray()), eq(0),
                eq(52));
        this.xmlConsumer.endElement("uri", "url", "qName");
        replay(this.xmlConsumer);
        this.transformer.startElement("uri", "url", "qName", this.attributes);
        this.transformer.characters("http://www.uptodate.com/online".toCharArray(), 0, 30);
        this.transformer.endElement("uri", "url", "qName");
        verify(this.xmlConsumer);
    }

    @Test
    public void testEndElement() throws SAXException {
        this.xmlConsumer.endElement("uri", "url", "qName");
        replay(this.xmlConsumer);
        this.transformer.endElement("uri", "url", "qName");
        verify(this.xmlConsumer);
    }

    @Test
    public void testStartElement() throws SAXException {
        this.xmlConsumer.startElement("uri", "url", "qName", this.attributes);
        replay(this.xmlConsumer);
        this.transformer.startElement("uri", "url", "qName", this.attributes);
        verify(this.xmlConsumer);
    }
}
