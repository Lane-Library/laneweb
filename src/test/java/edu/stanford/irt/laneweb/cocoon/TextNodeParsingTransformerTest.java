package edu.stanford.irt.laneweb.cocoon;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.source.NOPValidity;
import edu.stanford.irt.cocoon.xml.SAXParser;
import edu.stanford.irt.cocoon.xml.XMLConsumer;


public class TextNodeParsingTransformerTest {

    private Attributes attributes;

    private Map<String, String> parameters;

    private SAXParser saxParser;

    private TextNodeParsingTransformer transformer;

    private XMLConsumer xmlConsumer;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.saxParser = createMock(SAXParser.class);
        this.transformer = new TextNodeParsingTransformer("type", this.saxParser);
        this.parameters = createMock(Map.class);
        expect(this.parameters.get("elementName")).andReturn("element");
        replay(this.parameters);
        this.transformer.setParameters(this.parameters);
        verify(this.parameters);
        this.xmlConsumer = createMock(XMLConsumer.class);
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
        assertEquals("textNodeParsing", this.transformer.getKey());
    }

    @Test
    public void testGetValidity() {
        assertEquals(NOPValidity.SHARED_INSTANCE, this.transformer.getValidity());
    }

    @Test
    public void testStartElement() throws SAXException {
        this.xmlConsumer.startElement("uri", "localName", "qName", this.attributes);
        replay(this.saxParser, this.xmlConsumer);
        this.transformer.startElement("uri", "localName", "qName", this.attributes);
        verify(this.saxParser, this.xmlConsumer);
    }
}
