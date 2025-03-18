package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.resource.Resource;

public class QueryHighlightingTransformerTest {

    private static final char[] CHARS = "some characters with query inside of it".toCharArray();

    private Map<String, Object> model;

    private QueryHighlightingTransformer transformer;

    private XMLConsumer xmlConsumer;

    @BeforeEach
    public void setUp() throws Exception {
        this.model = new HashMap<>();
        this.xmlConsumer = mock(XMLConsumer.class);
        this.transformer = new QueryHighlightingTransformer();
        this.transformer.setXMLConsumer(this.xmlConsumer);
    }

    @Test
    public void testCharacters() throws SAXException {
        this.model.put(Model.QUERY, "query");
        this.xmlConsumer.startElement(Resource.NAMESPACE, Resource.TITLE, Resource.TITLE, null);
        this.xmlConsumer.characters(isA(char[].class), eq(0), eq(21));
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.KEYWORD), eq(Resource.KEYWORD),
                isA(Attributes.class));
        this.xmlConsumer.characters(isA(char[].class), eq(0), eq(5));
        this.xmlConsumer.endElement(eq(Resource.NAMESPACE), eq(Resource.KEYWORD), eq(Resource.KEYWORD));
        this.xmlConsumer.characters(isA(char[].class), eq(26), eq(13));
        this.xmlConsumer.endElement(Resource.NAMESPACE, Resource.TITLE, Resource.TITLE);
        replay(this.xmlConsumer);
        this.transformer.setModel(this.model);
        this.transformer.startElement(Resource.NAMESPACE, Resource.TITLE, Resource.TITLE, null);
        this.transformer.characters(CHARS, 0, CHARS.length);
        this.transformer.endElement(Resource.NAMESPACE, Resource.TITLE, Resource.TITLE);
        verify(this.xmlConsumer);
    }

    @Test
    public void testEndElement() throws SAXException {
        this.model.put(Model.QUERY, "query");
        this.xmlConsumer.endElement(null, null, null);
        replay(this.xmlConsumer);
        this.transformer.setModel(this.model);
        this.transformer.endElement(null, null, null);
        verify(this.xmlConsumer);
    }

    @Test
    public void testNullQuery() throws SAXException {
        this.transformer.startElement(Resource.NAMESPACE, Resource.TITLE, Resource.TITLE, null);
        this.transformer.characters(CHARS, 0, CHARS.length);
        this.transformer.endElement(Resource.NAMESPACE, Resource.TITLE, Resource.TITLE);
        replay(this.xmlConsumer);
        this.transformer.setModel(this.model);
        this.transformer.startElement(Resource.NAMESPACE, Resource.TITLE, Resource.TITLE, null);
        this.transformer.characters(CHARS, 0, CHARS.length);
        this.transformer.endElement(Resource.NAMESPACE, Resource.TITLE, Resource.TITLE);
        verify(this.xmlConsumer);
    }

    @Test
    public void testStartElement() throws SAXException {
        this.model.put(Model.QUERY, "query");
        this.xmlConsumer.startElement(null, null, null, null);
        replay(this.xmlConsumer);
        this.transformer.setModel(this.model);
        this.transformer.startElement(null, null, null, null);
        verify(this.xmlConsumer);
    }
}
