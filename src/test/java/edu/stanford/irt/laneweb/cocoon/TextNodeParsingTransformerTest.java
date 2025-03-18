package edu.stanford.irt.laneweb.cocoon;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.cache.validity.AlwaysValid;
import edu.stanford.irt.cocoon.source.Source;
import edu.stanford.irt.cocoon.xml.SAXParser;
import edu.stanford.irt.cocoon.xml.XMLConsumer;

public class TextNodeParsingTransformerTest {

    private Attributes attributes;

    private Map<String, String> parameters;

    private SAXParser saxParser;

    private TextNodeParsingTransformer transformer;

    private XMLConsumer xmlConsumer;

    @BeforeEach
    public void setUp() throws Exception {
        this.saxParser = mock(SAXParser.class);
        this.transformer = new TextNodeParsingTransformer("type", this.saxParser);
        this.parameters = mock(Map.class);
        expect(this.parameters.containsKey("elementNames")).andReturn(true);
        expect(this.parameters.get("elementNames")).andReturn("element");
        replay(this.parameters);
        this.transformer.setParameters(this.parameters);
        verify(this.parameters);
        this.xmlConsumer = mock(XMLConsumer.class);
        this.transformer.setXMLConsumer(this.xmlConsumer);
    }

    @Test
    public void testCharacters() throws SAXException {
        this.xmlConsumer.characters(null, 0, 0);
        replay(this.saxParser, this.xmlConsumer);
        this.transformer.characters(null, 0, 0);
        verify(this.saxParser, this.xmlConsumer);
    }

    @Test
    public void testEndElement() throws SAXException {
        this.xmlConsumer.endElement("uri", "localName", "qName");
        replay(this.saxParser, this.xmlConsumer);
        this.transformer.endElement("uri", "localName", "qName");
        verify(this.saxParser, this.xmlConsumer);
    }

    @Test
    public void testGetKey() {
        assertEquals("type", this.transformer.getKey());
    }

    @Test
    public void testGetType() {
        assertEquals("type", this.transformer.getType());
    }

    @Test
    public void testGetValidity() {
        assertEquals(AlwaysValid.SHARED_INSTANCE, this.transformer.getValidity());
    }

    @Test
    public void testStartElement() throws SAXException {
        this.xmlConsumer.startElement("uri", "localName", "qName", this.attributes);
        replay(this.saxParser, this.xmlConsumer);
        this.transformer.startElement("uri", "localName", "qName", this.attributes);
        verify(this.saxParser, this.xmlConsumer);
    }

    @Test
    public void testStartParsedElement() throws SAXException {
        this.xmlConsumer.startElement("uri", "element", "element", this.attributes);
        this.saxParser.parse(isA(Source.class), isA(XMLConsumer.class));
        this.xmlConsumer.endElement("uri", "element", "element");
        this.xmlConsumer.characters(aryEq("<foo>bar</foo>".toCharArray()), eq(0), eq(14));
        replay(this.saxParser, this.xmlConsumer);
        this.transformer.startElement("uri", "element", "element", this.attributes);
        this.transformer.characters("<foo>bar</foo>".toCharArray(), 0, 14);
        this.transformer.endElement("uri", "element", "element");
        this.transformer.characters("<foo>bar</foo>".toCharArray(), 0, 14);
        verify(this.saxParser, this.xmlConsumer);
    }
}
