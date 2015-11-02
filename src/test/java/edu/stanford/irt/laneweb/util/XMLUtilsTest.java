package edu.stanford.irt.laneweb.util;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.XMLConsumer;

public class XMLUtilsTest {

    private AttributesImpl atts;

    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() {
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.atts = createMock(AttributesImpl.class);
    }

    @Test
    public void testCreateElement() throws SAXException {
        this.xmlConsumer.startElement("ns", "name", "name", this.atts);
        this.xmlConsumer.characters(aryEq("text".toCharArray()), eq(0), eq("text".length()));
        this.xmlConsumer.endElement("ns", "name", "name");
        replay(this.xmlConsumer, this.atts);
        XMLUtils.createElement(this.xmlConsumer, "ns", "name", this.atts, "text");
        verify(this.xmlConsumer, this.atts);
    }

    @Test
    public void testCreateElementNS() throws SAXException {
        this.xmlConsumer.startElement("ns", "name", "name", XMLUtils.EMPTY_ATTRIBUTES);
        this.xmlConsumer.characters(aryEq("text".toCharArray()), eq(0), eq("text".length()));
        this.xmlConsumer.endElement("ns", "name", "name");
        replay(this.xmlConsumer, this.atts);
        XMLUtils.createElementNS(this.xmlConsumer, "ns", "name", "text");
        verify(this.xmlConsumer, this.atts);
    }

    @Test
    public void testData() throws SAXException {
        this.xmlConsumer.characters(aryEq("data".toCharArray()), eq(0), eq("data".length()));
        replay(this.xmlConsumer, this.atts);
        XMLUtils.data(this.xmlConsumer, "data");
        verify(this.xmlConsumer, this.atts);
    }

    @Test
    public void testEndElement() throws SAXException {
        this.xmlConsumer.endElement("ns", "name", "name");
        replay(this.xmlConsumer, this.atts);
        XMLUtils.endElement(this.xmlConsumer, "ns", "name");
        verify(this.xmlConsumer, this.atts);
    }

    @Test
    public void testMaybeCreateElement() throws SAXException {
        this.xmlConsumer.startElement("ns", "name", "name", XMLUtils.EMPTY_ATTRIBUTES);
        this.xmlConsumer.characters(aryEq("value".toCharArray()), eq(0), eq("value".length()));
        this.xmlConsumer.endElement("ns", "name", "name");
        replay(this.xmlConsumer, this.atts);
        XMLUtils.maybeCreateElement(this.xmlConsumer, "ns", "name", "value");
        verify(this.xmlConsumer, this.atts);
    }

    @Test
    public void testMaybeCreateElementEmptyValue() throws SAXException {
        replay(this.xmlConsumer, this.atts);
        XMLUtils.maybeCreateElement(this.xmlConsumer, "ns", "name", "");
        verify(this.xmlConsumer, this.atts);
    }

    @Test
    public void testMaybeCreateElementNullValue() throws SAXException {
        replay(this.xmlConsumer, this.atts);
        XMLUtils.maybeCreateElement(this.xmlConsumer, "ns", "name", null);
        verify(this.xmlConsumer, this.atts);
    }

    @Test
    public void testStartElementXMLConsumerStringString() throws SAXException {
        this.xmlConsumer.startElement("ns", "name", "name", XMLUtils.EMPTY_ATTRIBUTES);
        replay(this.xmlConsumer, this.atts);
        XMLUtils.startElement(this.xmlConsumer, "ns", "name");
        verify(this.xmlConsumer, this.atts);
    }

    @Test
    public void testStartElementXMLConsumerStringStringAttributes() throws SAXException {
        this.xmlConsumer.startElement("ns", "name", "name", this.atts);
        replay(this.xmlConsumer, this.atts);
        XMLUtils.startElement(this.xmlConsumer, "ns", "name", this.atts);
        verify(this.xmlConsumer, this.atts);
    }
}
