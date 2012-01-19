package edu.stanford.irt.laneweb.cocoon.source;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collections;

import org.apache.cocoon.Processor;
import org.apache.cocoon.Processor.InternalPipelineDescription;
import org.apache.cocoon.components.pipeline.ProcessingPipeline;
import org.apache.cocoon.environment.Environment;
import org.apache.excalibur.source.SourceValidity;
import org.junit.Before;
import org.junit.Test;

public class LanewebSitemapSourceFactoryTest {

    private static final class TestFactory extends LanewebSitemapSourceFactory {

        @Override
        protected Environment getEnvironment() {
            return environment;
        }
    }

    private static Environment environment;

    private LanewebSitemapSourceFactory factory;

    private ProcessingPipeline pipeline;

    private InternalPipelineDescription pipelineDescription;

    private Processor processor;

    private SourceValidity validity;

    @Before
    public void setUp() throws Exception {
        this.factory = new TestFactory();
        environment = createMock(Environment.class);
        this.processor = createMock(Processor.class);
        this.factory.setProcessor(this.processor);
        this.pipelineDescription = createMock(InternalPipelineDescription.class);
        this.pipeline = createMock(ProcessingPipeline.class);
        this.pipelineDescription.processingPipeline = this.pipeline;
        this.validity = createMock(SourceValidity.class);
    }

    @Test
    public void testGetSource() throws Exception {
        expect(environment.getObjectModel()).andReturn(Collections.emptyMap());
        expect(this.processor.buildPipeline(isA(Environment.class))).andReturn(this.pipelineDescription);
        this.pipeline.prepareInternal(isA(Environment.class));
        expect(this.pipeline.getValidityForEventPipeline()).andReturn(this.validity);
        expect(this.pipeline.getKeyForEventPipeline()).andReturn("key");
        replay(environment, this.processor, this.pipeline, this.pipelineDescription);
        this.factory.getSource("foo:/bar?foo=bar", null);
        verify(environment, this.processor, this.pipeline, this.pipelineDescription);
    }

    @Test
    public void testRelease() {
        this.factory.release(null);
    }

    @Test
    public void testSetProcessor() {
        this.factory.setProcessor(null);
    }
}
