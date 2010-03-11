package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.fail;

import org.apache.cocoon.xml.XMLConsumer;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.Resource;
import edu.stanford.irt.laneweb.model.Model;

// $Id$
public class QueryHighlightingTransformerTest {

    private static final char[] CHARS = "some characters with query inside of it".toCharArray();

    private Model model;

    private QueryHighlightingTransformer transformer;

    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() throws Exception {
        this.model = createMock(Model.class);
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.transformer = new QueryHighlightingTransformer();
        this.transformer.setModel(this.model);
        this.transformer.setConsumer(this.xmlConsumer);
    }

    @Test
    public void testCharacters() throws SAXException {
        expect(this.model.getString(Model.QUERY)).andReturn("query");
        this.xmlConsumer.startElement(Resource.NAMESPACE, Resource.TITLE, Resource.TITLE, null);
        this.xmlConsumer.characters(isA(char[].class), eq(0), eq(21));
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.KEYWORD),
                eq(Resource.KEYWORD), isA(Attributes.class));
        this.xmlConsumer.characters(isA(char[].class), eq(0), eq(5));
        this.xmlConsumer.endElement(eq(Resource.NAMESPACE), eq(Resource.KEYWORD),
                eq(Resource.KEYWORD));
        this.xmlConsumer.characters(isA(char[].class), eq(26), eq(13));
        this.xmlConsumer.endElement(Resource.NAMESPACE, Resource.TITLE, Resource.TITLE);
        replayMocks();
        this.transformer.initialize();
        this.transformer.startElement(Resource.NAMESPACE, Resource.TITLE, Resource.TITLE, null);
        this.transformer.characters(CHARS, 0, CHARS.length);
        this.transformer.endElement(Resource.NAMESPACE, Resource.TITLE, Resource.TITLE);
        verifyMocks();
    }

    @Test
    public void testEndElement() throws SAXException {
        expect(this.model.getString(Model.QUERY)).andReturn("query");
        this.xmlConsumer.endElement(null, null, null);
        replayMocks();
        this.transformer.initialize();
        this.transformer.endElement(null, null, null);
        verifyMocks();
    }

    @Test
    public void testInitialize() {
        expect(this.model.getString(Model.QUERY)).andReturn(null);
        replayMocks();
        try {
            this.transformer.initialize();
            fail();
        } catch (IllegalArgumentException e) {
        }
        verifyMocks();
    }

    @Test
    public void testStartElement() throws SAXException {
        expect(this.model.getString(Model.QUERY)).andReturn("query");
        this.xmlConsumer.startElement(null, null, null, null);
        replayMocks();
        this.transformer.initialize();
        this.transformer.startElement(null, null, null, null);
        verifyMocks();
    }

    private void replayMocks() {
        replay(this.xmlConsumer);
        replay(this.model);
    }

    private void verifyMocks() {
        verify(this.xmlConsumer);
        verify(this.model);
    }
}
