package edu.stanford.irt.laneweb;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.apache.cocoon.xml.XMLConsumer;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

public class NoBodyContent404TransformerTest {

    private NoBodyContent404Transformer transformer;

    private XMLConsumer consumer;

    @Before
    public void setUp() throws Exception {
        this.transformer = new NoBodyContent404Transformer();
        this.consumer = createMock(XMLConsumer.class);
        this.transformer.setConsumer(this.consumer);
        this.transformer.setup(null, null, null, null);
    }

    @Test
    public void testFAQNotFound() throws SAXException {
        this.consumer.startElement(null, "body", null, null);
        replay(this.consumer);
        this.transformer.startElement(null, "body", null, null);
        try {
        this.transformer.endElement(null, "body", null);
        } catch (RuntimeException e) {}
        verify(this.consumer);
    }

    @Test
    public void testFAQFound() throws SAXException {
        this.consumer.startElement(null, "body", null, null);
        this.consumer.startElement(null, "div", null, null);
        this.consumer.endElement(null, "div", null);
        this.consumer.endElement(null, "body", null);
        replay(this.consumer);
        this.transformer.startElement(null, "body", null, null);
        this.transformer.startElement(null, "div", null, null);
        this.transformer.endElement(null, "div", null);
        this.transformer.endElement(null, "body", null);
        verify(this.consumer);
    }

}
