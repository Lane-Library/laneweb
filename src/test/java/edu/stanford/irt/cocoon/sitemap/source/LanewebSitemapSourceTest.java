package edu.stanford.irt.cocoon.sitemap.source;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.components.pipeline.ProcessingPipeline;
import org.apache.cocoon.environment.Environment;
import org.apache.cocoon.xml.XMLConsumer;
import org.apache.excalibur.source.SourceValidity;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

public class LanewebSitemapSourceTest {

    private ByteArrayOutputStream baos;

    private Environment environment;

    private ProcessingPipeline pipeline;

    private SitemapSource source;

    private String uri;

    private SourceValidity validity;

    @Before
    public void setUp() throws Exception {
        this.uri = "foo:/bar?foo=bar";
        this.environment = createMock(Environment.class);
        this.pipeline = createMock(ProcessingPipeline.class);
        this.baos = createMock(ByteArrayOutputStream.class);
        this.validity = createMock(SourceValidity.class);
        this.source = new SitemapSource(this.uri, this.environment, this.pipeline, this.baos);
    }

    @Test
    public void testExists() {
        replay(this.environment, this.pipeline, this.baos, this.validity);
        assertTrue(this.source.exists());
        verify(this.environment, this.pipeline, this.baos, this.validity);
    }

    @Test
    public void testGetContentLength() {
        replay(this.environment, this.pipeline, this.baos, this.validity);
        assertEquals(-1, this.source.getContentLength());
        verify(this.environment, this.pipeline, this.baos, this.validity);
    }

    @Test
    public void testGetInputStream() throws IOException, ProcessingException {
        expect(this.pipeline.process(isA(Environment.class))).andReturn(true);
        expect(this.baos.toByteArray()).andReturn(new byte[0]);
        replay(this.environment, this.pipeline, this.baos, this.validity);
        this.source.getInputStream();
        verify(this.environment, this.pipeline, this.baos, this.validity);
    }

    @Test
    public void testGetLastModified() {
        replay(this.environment, this.pipeline, this.baos, this.validity);
        assertEquals(0, this.source.getLastModified());
        verify(this.environment, this.pipeline, this.baos, this.validity);
    }

    @Test
    public void testGetMimeType() {
        replay(this.environment, this.pipeline, this.baos, this.validity);
        try {
            this.source.getMimeType();
            fail();
        } catch (UnsupportedOperationException e) {
        }
        verify(this.environment, this.pipeline, this.baos, this.validity);
    }

    @Test
    public void testGetScheme() {
        replay(this.environment, this.pipeline, this.baos, this.validity);
        assertEquals("foo", this.source.getScheme());
        verify(this.environment, this.pipeline, this.baos, this.validity);
    }

    @Test
    public void testGetURI() {
        expect(this.pipeline.getKeyForEventPipeline()).andReturn("key");
        replay(this.environment, this.pipeline, this.baos, this.validity);
        assertEquals("foo:/bar?foo=bar&pipelinehash=key", this.source.getURI());
        verify(this.environment, this.pipeline, this.baos, this.validity);
    }

    @Test
    public void testGetValidity() {
        expect(this.pipeline.getValidityForEventPipeline()).andReturn(this.validity);
        expect(this.validity.isValid()).andReturn(SourceValidity.VALID);
        replay(this.environment, this.pipeline, this.baos, this.validity);
        assertEquals(SourceValidity.VALID, this.source.getValidity().isValid());
        verify(this.environment, this.pipeline, this.baos, this.validity);
    }

    @Test
    public void testRefresh() {
        replay(this.environment, this.pipeline, this.baos, this.validity);
        try {
            this.source.refresh();
            fail();
        } catch (UnsupportedOperationException e) {
        }
        verify(this.environment, this.pipeline, this.baos, this.validity);
    }

    @Test
    public void testToSAX() throws SAXException, ProcessingException {
        expect(this.pipeline.process(isA(Environment.class), isA(XMLConsumer.class))).andReturn(true);
        replay(this.environment, this.pipeline, this.baos, this.validity);
        this.source.toSAX(null);
        verify(this.environment, this.pipeline, this.baos, this.validity);
    }
}
