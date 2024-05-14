package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.resource.Resource;

public class SolrQueryHighlightingTransformerTest {

    public static final String END = ":::";

    public static final String START = "___";

    private static final String SAMPLE = "data ___keyword::: data ___3:::___D::: printing: ___this _ text::: has an underscore but needs highlighting";

    private static final char[] SAMPLE_CHARS = SAMPLE.toCharArray();

    private SolrQueryHighlightingTransformer transformer;

    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() throws Exception {
        this.xmlConsumer = mock(XMLConsumer.class);
        this.transformer = new SolrQueryHighlightingTransformer();
        this.transformer.setXMLConsumer(this.xmlConsumer);
    }

    @Test
    public void testCharacters() throws SAXException {
        this.xmlConsumer.startElement(Resource.NAMESPACE, Resource.DESCRIPTION, Resource.DESCRIPTION, null);
        this.xmlConsumer.characters(isA(char[].class), eq(0), eq(5));
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.KEYWORD), eq(Resource.KEYWORD),
                isA(Attributes.class));
        this.xmlConsumer.characters(isA(char[].class), eq(0), eq(7));
        this.xmlConsumer.endElement(eq(Resource.NAMESPACE), eq(Resource.KEYWORD), eq(Resource.KEYWORD));
        this.xmlConsumer.characters(isA(char[].class), eq(18), eq(6));
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.KEYWORD), eq(Resource.KEYWORD),
                isA(Attributes.class));
        this.xmlConsumer.characters(isA(char[].class), eq(0), eq(1));
        this.xmlConsumer.endElement(eq(Resource.NAMESPACE), eq(Resource.KEYWORD), eq(Resource.KEYWORD));
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.KEYWORD), eq(Resource.KEYWORD),
                isA(Attributes.class));
        this.xmlConsumer.characters(isA(char[].class), eq(0), eq(1));
        this.xmlConsumer.endElement(eq(Resource.NAMESPACE), eq(Resource.KEYWORD), eq(Resource.KEYWORD));
        this.xmlConsumer.characters(isA(char[].class), eq(38), eq(11));
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.KEYWORD), eq(Resource.KEYWORD),
                isA(Attributes.class));
        this.xmlConsumer.characters(isA(char[].class), eq(0), eq(11));
        this.xmlConsumer.endElement(eq(Resource.NAMESPACE), eq(Resource.KEYWORD), eq(Resource.KEYWORD));
        this.xmlConsumer.characters(isA(char[].class), eq(66), eq(41));
        this.xmlConsumer.endElement(Resource.NAMESPACE, Resource.DESCRIPTION, Resource.DESCRIPTION);
        replay(this.xmlConsumer);
        this.transformer.startElement(Resource.NAMESPACE, Resource.DESCRIPTION, Resource.DESCRIPTION, null);
        this.transformer.characters(SAMPLE_CHARS, 0, SAMPLE_CHARS.length);
        this.transformer.endElement(Resource.NAMESPACE, Resource.DESCRIPTION, Resource.DESCRIPTION);
        verify(this.xmlConsumer);
    }

    @Test
    public void testEndElement() throws SAXException {
        this.xmlConsumer.endElement(null, null, null);
        replay(this.xmlConsumer);
        this.transformer.endElement(null, null, null);
        verify(this.xmlConsumer);
    }

    @Test
    public void testStartElement() throws SAXException {
        this.xmlConsumer.startElement(null, null, null, null);
        replay(this.xmlConsumer);
        this.transformer.startElement(null, null, null, null);
        verify(this.xmlConsumer);
    }
}
