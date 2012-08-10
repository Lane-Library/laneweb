package edu.stanford.irt.cocoon.pipeline.serialize;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Collections;
import java.util.Properties;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.apache.excalibur.source.SourceValidity;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.CocoonException;

public class TransformerSerializerTest {

    private SAXTransformerFactory factory;

    private TransformerHandler handler;

    private Properties properties;

    private TransformerSerializer serializer;

    private Transformer transformer;

    @Before
    public void setUp() {
        this.factory = createMock(SAXTransformerFactory.class);
        this.properties = createMock(Properties.class);
        this.handler = createMock(TransformerHandler.class);
        this.transformer = createMock(Transformer.class);
        expect(this.properties.keySet()).andReturn(Collections.<Object> singleton("foo"));
        expect(this.properties.get("foo")).andReturn("bar");
        replay(this.properties);
        this.serializer = new TransformerSerializer("type", this.factory, this.properties);
        verify(this.properties);
        reset(this.properties);
    }

    @Test
    public void testGetKey() {
        assertEquals(";foo=bar", this.serializer.getKey());
    }

    @Test
    public void testGetValidity() {
        assertEquals(SourceValidity.VALID, this.serializer.getValidity().isValid());
    }

    @Test
    public void testSetOutputStream() throws IOException, TransformerConfigurationException {
        expect(this.factory.newTransformerHandler()).andReturn(this.handler);
        expect(this.handler.getTransformer()).andReturn(this.transformer);
        this.transformer.setOutputProperties(this.properties);
        this.handler.setResult(isA(StreamResult.class));
        replay(this.factory, this.handler, this.transformer);
        this.serializer.setOutputStream(null);
        verify(this.factory, this.handler, this.transformer);
    }

    @Test
    public void testSetOutputStreamThrows() throws IOException, TransformerConfigurationException {
        expect(this.factory.newTransformerHandler()).andThrow(new TransformerConfigurationException());
        replay(this.factory);
        try {
            this.serializer.setOutputStream(null);
            fail();
        } catch (CocoonException e) {
        }
        verify(this.factory);
    }
}
