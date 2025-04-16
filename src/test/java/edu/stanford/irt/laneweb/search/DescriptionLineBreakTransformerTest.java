package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.resource.Resource;

public class DescriptionLineBreakTransformerTest {

    private static final char[] CHARS = "test <br/> test<br>test".toCharArray();

    private DescriptionLineBreakTransformer transformer;

    private XMLConsumer xmlConsumer;

    @BeforeEach
    public void setUp() throws Exception {
        this.xmlConsumer = mock(XMLConsumer.class);
        this.transformer = new DescriptionLineBreakTransformer();
        this.transformer.setXMLConsumer(this.xmlConsumer);
    }

    @Test
    public void testCharacters() throws SAXException {
        this.xmlConsumer.startElement(Resource.NAMESPACE, Resource.DESCRIPTION, Resource.DESCRIPTION, null);
        this.xmlConsumer.characters(isA(char[].class), eq(0), eq(5));
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.DESCRIPTION_LINEBREAK),
                eq(Resource.DESCRIPTION_LINEBREAK), isA(Attributes.class));
        this.xmlConsumer.characters(isA(char[].class), eq(0), eq(5));
        this.xmlConsumer.endElement(eq(Resource.NAMESPACE), eq(Resource.DESCRIPTION_LINEBREAK),
                eq(Resource.DESCRIPTION_LINEBREAK));
        this.xmlConsumer.characters(isA(char[].class), eq(10), eq(5));
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.DESCRIPTION_LINEBREAK),
                eq(Resource.DESCRIPTION_LINEBREAK), isA(Attributes.class));
        this.xmlConsumer.characters(isA(char[].class), eq(0), eq(4));
        this.xmlConsumer.endElement(eq(Resource.NAMESPACE), eq(Resource.DESCRIPTION_LINEBREAK),
                eq(Resource.DESCRIPTION_LINEBREAK));
        this.xmlConsumer.characters(isA(char[].class), eq(19), eq(4));
        this.xmlConsumer.endElement(Resource.NAMESPACE, Resource.DESCRIPTION, Resource.DESCRIPTION);
        replay(this.xmlConsumer);
        this.transformer.startElement(Resource.NAMESPACE, Resource.DESCRIPTION, Resource.DESCRIPTION, null);
        this.transformer.characters(CHARS, 0, CHARS.length);
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
