package edu.stanford.irt.laneweb.util;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class XMLUtilsTest {

    private AttributesImpl atts;

    private ContentHandler contentHandler;

    @BeforeEach
    public void setUp() {
        this.contentHandler = mock(ContentHandler.class);
        this.atts = mock(AttributesImpl.class);
    }

    @Test
    public void testCreateElement() throws SAXException {
        this.contentHandler.startElement("ns", "name", "name", this.atts);
        this.contentHandler.characters(aryEq("text".toCharArray()), eq(0), eq("text".length()));
        this.contentHandler.endElement("ns", "name", "name");
        replay(this.contentHandler, this.atts);
        XMLUtils.createElement(this.contentHandler, "ns", "name", this.atts, "text");
        verify(this.contentHandler, this.atts);
    }

    @Test
    public void testCreateElementNS() throws SAXException {
        this.contentHandler.startElement("ns", "name", "name", XMLUtils.EMPTY_ATTRIBUTES);
        this.contentHandler.characters(aryEq("text".toCharArray()), eq(0), eq("text".length()));
        this.contentHandler.endElement("ns", "name", "name");
        replay(this.contentHandler, this.atts);
        XMLUtils.createElementNS(this.contentHandler, "ns", "name", "text");
        verify(this.contentHandler, this.atts);
    }

    @Test
    public void testData() throws SAXException {
        this.contentHandler.characters(aryEq("data".toCharArray()), eq(0), eq("data".length()));
        replay(this.contentHandler, this.atts);
        XMLUtils.data(this.contentHandler, "data");
        verify(this.contentHandler, this.atts);
    }

    @Test
    public void testEndElement() throws SAXException {
        this.contentHandler.endElement("ns", "name", "name");
        replay(this.contentHandler, this.atts);
        XMLUtils.endElement(this.contentHandler, "ns", "name");
        verify(this.contentHandler, this.atts);
    }

    @Test
    public void testMaybeCreateElement() throws SAXException {
        this.contentHandler.startElement("ns", "name", "name", XMLUtils.EMPTY_ATTRIBUTES);
        this.contentHandler.characters(aryEq("value".toCharArray()), eq(0), eq("value".length()));
        this.contentHandler.endElement("ns", "name", "name");
        replay(this.contentHandler, this.atts);
        XMLUtils.maybeCreateElement(this.contentHandler, "ns", "name", "value");
        verify(this.contentHandler, this.atts);
    }

    @Test
    public void testMaybeCreateElementEmptyValue() throws SAXException {
        replay(this.contentHandler, this.atts);
        XMLUtils.maybeCreateElement(this.contentHandler, "ns", "name", "");
        verify(this.contentHandler, this.atts);
    }

    @Test
    public void testMaybeCreateElementNullValue() throws SAXException {
        replay(this.contentHandler, this.atts);
        XMLUtils.maybeCreateElement(this.contentHandler, "ns", "name", null);
        verify(this.contentHandler, this.atts);
    }

    @Test
    public void testStartElementXMLConsumerStringString() throws SAXException {
        this.contentHandler.startElement("ns", "name", "name", XMLUtils.EMPTY_ATTRIBUTES);
        replay(this.contentHandler, this.atts);
        XMLUtils.startElement(this.contentHandler, "ns", "name");
        verify(this.contentHandler, this.atts);
    }

    @Test
    public void testStartElementXMLConsumerStringStringAttributes() throws SAXException {
        this.contentHandler.startElement("ns", "name", "name", this.atts);
        replay(this.contentHandler, this.atts);
        XMLUtils.startElement(this.contentHandler, "ns", "name", this.atts);
        verify(this.contentHandler, this.atts);
    }
}
