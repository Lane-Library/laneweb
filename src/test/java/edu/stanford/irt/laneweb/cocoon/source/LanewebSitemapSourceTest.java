package edu.stanford.irt.laneweb.cocoon.source;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.Processor;
import org.apache.cocoon.Processor.InternalPipelineDescription;
import org.apache.cocoon.components.pipeline.ProcessingPipeline;
import org.apache.cocoon.environment.Environment;
import org.apache.cocoon.xml.XMLConsumer;
import org.apache.excalibur.source.SourceValidity;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;


public class LanewebSitemapSourceTest {
    
    private LanewebSitemapSource source;
    private Environment environment;
    private String uri;
    private Processor processor;
    private Map<String, Object> model;
    private InternalPipelineDescription pipelineDescription;
    private ProcessingPipeline pipeline;
    private SourceValidity validity;

    @Before
    public void setUp() throws Exception {
        this.environment = createMock(Environment.class);
        this.processor = createMock(Processor.class);
        this.uri = "foo:/bar?foo=bar";
        this.model = new HashMap<String, Object>();
        this.pipelineDescription = createMock(InternalPipelineDescription.class);
        this.pipeline = createMock(ProcessingPipeline.class);
        this.pipelineDescription.processingPipeline = this.pipeline;
        this.validity = createMock(SourceValidity.class);
        expect(this.environment.getObjectModel()).andReturn(this.model);
        expect(this.processor.buildPipeline(isA(Environment.class))).andReturn(this.pipelineDescription);
        this.pipeline.prepareInternal(isA(Environment.class));
        expect(this.pipeline.getValidityForEventPipeline()).andReturn(this.validity);
        expect(this.pipeline.getKeyForEventPipeline()).andReturn("key");
        replay(this.environment, this.processor, this.pipelineDescription, this.pipeline, this.validity);
        this.source = new LanewebSitemapSource(this.uri, this.environment, this.processor);
        verify(this.environment, this.processor, this.pipelineDescription, this.pipeline, this.validity);
        reset(this.environment, this.processor, this.pipelineDescription, this.pipeline, this.validity);
    }

    @Test
    public void testExists() {
        assertTrue(this.source.exists());
    }

    @Test
    public void testGetContentLength() {
        assertEquals(-1, this.source.getContentLength());
    }

    @Test
    public void testGetInputStream() throws IOException, ProcessingException {
        expect(this.pipeline.process(isA(Environment.class))).andReturn(true);
        replay(this.pipeline);
        this.source.getInputStream();
        verify(this.pipeline);
    }

    @Test
    public void testGetLastModified() {
        assertEquals(0, this.source.getLastModified());
    }

    @Test
    public void testGetMimeType() {
        try {
            this.source.getMimeType();
            fail();
        } catch (UnsupportedOperationException e) {}
    }

    @Test
    public void testGetScheme() {
        assertEquals("foo", this.source.getScheme());
    }

    @Test
    public void testGetURI() {
        assertEquals("foo:/bar?foo=bar&pipelinehash=key", this.source.getURI());
    }

    @Test
    public void testGetValidity() {
        expect(this.validity.isValid()).andReturn(SourceValidity.VALID);
        replay(this.validity);
        assertEquals(SourceValidity.VALID, this.source.getValidity().isValid());
        verify(this.validity);
    }

    @Test
    public void testRefresh() {
        try {
            this.source.refresh();
            fail();
        } catch (UnsupportedOperationException e) {}
    }

    @Test
    public void testToSAX() throws SAXException, ProcessingException {
        expect(this.pipeline.process(isA(Environment.class), isA(XMLConsumer.class))).andReturn(true);
        replay(this.pipeline);
        this.source.toSAX(null);
        verify(this.pipeline);
    }
}
