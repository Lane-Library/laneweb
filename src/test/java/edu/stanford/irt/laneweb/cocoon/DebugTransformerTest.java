package edu.stanford.irt.laneweb.cocoon;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.model.Model;


public class DebugTransformerTest {

    private XMLConsumer consumer;

    private DebugTransformer transformer;

    @Before
    public void setUp() {
        this.consumer = createMock(XMLConsumer.class);
        this.transformer = new DebugTransformer();
        this.transformer.setXMLConsumer(this.consumer);
    }

    @Test
    public void testStartDocument() throws SAXException {
        // expect(this.parameters.keySet()).andReturn(Collections.<String>emptySet());
        this.consumer.startDocument();
        this.consumer.comment(isA(char[].class), eq(0), eq(13));
        replay(this.consumer);
        this.transformer.setModel(Collections.<String, Object> singletonMap(Model.DEBUG, Boolean.TRUE));
        this.transformer.startDocument();
        verify(this.consumer);
    }

    @Test
    public void testStartDocumentFalse() throws SAXException {
        // expect(this.parameters.keySet()).andReturn(Collections.<String>emptySet());
        this.consumer.startDocument();
        replay(this.consumer);
        this.transformer.setModel(Collections.<String, Object> singletonMap(Model.DEBUG, Boolean.FALSE));
        this.transformer.startDocument();
        verify(this.consumer);
    }
}
