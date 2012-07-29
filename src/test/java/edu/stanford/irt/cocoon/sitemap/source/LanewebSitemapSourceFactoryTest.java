package edu.stanford.irt.cocoon.sitemap.source;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collections;
import java.util.Map;

import org.apache.cocoon.Processor;
import org.apache.cocoon.Processor.InternalPipelineDescription;
import org.apache.cocoon.components.pipeline.ProcessingPipeline;
import org.apache.cocoon.environment.Environment;
import org.junit.Before;
import org.junit.Test;

public class LanewebSitemapSourceFactoryTest {

    private static final class TestFactory extends SitemapSourceFactory {

        public TestFactory(Processor processor) {
            super(processor);
        }

        @Override
        protected Map<String, Object> getModel() {
            return model;
        }
    }

    private static Map<String, Object> model;

    private SitemapSourceFactory factory;

    private ProcessingPipeline pipeline;

    private InternalPipelineDescription pipelineDescription;

    private Processor processor;

    @Before
    public void setUp() throws Exception {
        this.processor = createMock(Processor.class);
        this.factory = new TestFactory(this.processor);
        model = Collections.emptyMap();
        this.pipelineDescription = createMock(InternalPipelineDescription.class);
        this.pipeline = createMock(ProcessingPipeline.class);
        this.pipelineDescription.processingPipeline = this.pipeline;
    }

    @Test
    public void testGetSource() throws Exception {
        expect(this.processor.buildPipeline(isA(Environment.class))).andReturn(this.pipelineDescription);
        this.pipeline.prepareInternal(isA(Environment.class));
        replay(this.processor, this.pipeline, this.pipelineDescription);
        this.factory.getSource("foo:/bar?foo=bar", null);
        verify(this.processor, this.pipeline, this.pipelineDescription);
    }

    @Test
    public void testRelease() {
        this.factory.release(null);
    }
}
