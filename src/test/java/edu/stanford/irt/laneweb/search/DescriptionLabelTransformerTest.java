package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.HashMap;
import java.util.Map;

import org.apache.cocoon.xml.XMLConsumer;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.resource.Resource;

public class DescriptionLabelTransformerTest {

    private static final char[] CHARS = "some characters with ::DESCRIPTION LABEL## inside of it".toCharArray();

    private Map<String, Object> model;

    private DescriptionLabelTransformer transformer;

    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() throws Exception {
        this.model = new HashMap<String, Object>();
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.transformer = new DescriptionLabelTransformer();
        this.transformer.setConsumer(this.xmlConsumer);
    }

    @Test
    public void testCharacters() throws SAXException {
        this.xmlConsumer.startElement(Resource.NAMESPACE, Resource.DESCRIPTION, Resource.DESCRIPTION, null);
        this.xmlConsumer.characters(isA(char[].class), eq(0), eq(21));
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.DESCRIPTION_LABEL), eq(Resource.DESCRIPTION_LABEL), isA(Attributes.class));
        this.xmlConsumer.characters(isA(char[].class), eq(0), eq(17));
        this.xmlConsumer.endElement(eq(Resource.NAMESPACE), eq(Resource.DESCRIPTION_LABEL), eq(Resource.DESCRIPTION_LABEL));
        this.xmlConsumer.characters(isA(char[].class), eq(42), eq(13));
        this.xmlConsumer.endElement(Resource.NAMESPACE, Resource.DESCRIPTION, Resource.DESCRIPTION);
        replayMocks();
        this.transformer.setup(null, this.model, null, null);
        this.transformer.startElement(Resource.NAMESPACE, Resource.DESCRIPTION, Resource.DESCRIPTION, null);
        this.transformer.characters(CHARS, 0, CHARS.length);
        this.transformer.endElement(Resource.NAMESPACE, Resource.DESCRIPTION, Resource.DESCRIPTION);
        verifyMocks();
    }

    @Test
    public void testEndElement() throws SAXException {
        this.xmlConsumer.endElement(null, null, null);
        replayMocks();
        this.transformer.setup(null, this.model, null, null);
        this.transformer.endElement(null, null, null);
        verifyMocks();
    }

    @Test
    public void testStartElement() throws SAXException {
        this.xmlConsumer.startElement(null, null, null, null);
        replayMocks();
        this.transformer.setup(null, this.model, null, null);
        this.transformer.startElement(null, null, null, null);
        verifyMocks();
    }

    private void replayMocks() {
        replay(this.xmlConsumer);
    }

    private void verifyMocks() {
        verify(this.xmlConsumer);
    }
}
