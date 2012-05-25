package edu.stanford.irt.cocoon.pipeline;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.components.pipeline.AbstractProcessingPipeline;
import org.apache.cocoon.environment.Environment;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.Generator;
import org.apache.cocoon.serialization.Serializer;
import org.apache.cocoon.sitemap.SitemapErrorHandler;
import org.apache.cocoon.transformation.Transformer;
import org.apache.cocoon.xml.XMLConsumer;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.pipeline.NonCachingPipeline;

public class NonCachingPipelineTest {

    private BeanFactory beanFactory;

    private Environment environment;

    private Generator generator;

    private Parameters parameters;

    private NonCachingPipeline pipeline;

    private Serializer serializer;

    private SourceResolver sourceResolver;

    private Transformer transformer;

    @Before
    public void setUp() throws Exception {
        this.parameters = createMock(Parameters.class);
        this.beanFactory = createMock(BeanFactory.class);
        this.generator = createMock(Generator.class);
        this.transformer = createMock(Transformer.class);
        this.serializer = createMock(Serializer.class);
        this.environment = createMock(Environment.class);
        this.pipeline = new NonCachingPipeline(this.sourceResolver);
        this.pipeline.setBeanFactory(this.beanFactory);
    }

    @Test
    public void testAbstractProcessingPipeline() {
        new AbstractProcessingPipeline() {
        };
    }

    @Test
    public void testAddTransformer() throws ProcessingException {
        expect(this.beanFactory.getBean("org.apache.cocoon.generation.Generator/foo")).andReturn(this.generator);
        expect(this.beanFactory.getBean("org.apache.cocoon.transformation.Transformer/foo")).andReturn(this.transformer);
        replay(this.beanFactory, this.generator, this.transformer);
        this.pipeline.setGenerator("foo", null, null, null);
        this.pipeline.addTransformer("foo", null, null, null);
        assertEquals(this.transformer, this.pipeline.getTransformers().get(0));
        verify(this.beanFactory, this.generator, this.transformer);
    }

    @Test
    public void testGetKeyForEventPipeline() {
        this.pipeline.getKeyForEventPipeline();
    }

    @Test
    public void testGetValidityForEventPipeline() {
        this.pipeline.getValidityForEventPipeline();
    }

    @Test
    public void testInformBranchPoint() {
        this.pipeline.informBranchPoint();
    }

    @Test
    public void testNonCachingProcessingPipeline() {
        new NonCachingPipeline(null);
    }

    @Test
    public void testProcessEnvironment() throws ProcessingException, SAXException, IOException {
        expect(this.beanFactory.getBean("org.apache.cocoon.generation.Generator/null")).andReturn(this.generator);
        expect(this.beanFactory.getBean("org.apache.cocoon.serialization.Serializer/null")).andReturn(this.serializer);
        expect(this.environment.getObjectModel()).andReturn(null);
        expect(this.environment.getOutputStream(0)).andReturn(null);
        this.generator.setup(null, null, null, null);
        this.generator.setConsumer(this.serializer);
        this.generator.generate();
        this.serializer.setOutputStream(null);
        replay(this.beanFactory, this.environment, this.generator, this.serializer);
        this.pipeline.setGenerator(null, null, null, null);
        this.pipeline.setSerializer(null, null, null, null, "text/plain");
        this.pipeline.process(this.environment);
        verify(this.beanFactory, this.environment, this.generator, this.serializer);
    }

    @Test
    public void testProcessEnvironmentXMLConsumer() throws ProcessingException, IOException, SAXException {
        expect(this.beanFactory.getBean("org.apache.cocoon.generation.Generator/null")).andReturn(this.generator);
        expect(this.beanFactory.getBean("org.apache.cocoon.transformation.Transformer/null")).andReturn(this.transformer);
        expect(this.beanFactory.getBean("org.apache.cocoon.serialization.Serializer/null")).andReturn(this.serializer);
        this.generator.setConsumer(this.transformer);
        this.generator.setup(null, null, null, null);
        this.generator.generate();
        XMLConsumer consumer = createMock(XMLConsumer.class);
        this.transformer.setConsumer(consumer);
        this.transformer.setup(null, null, null, null);
        expect(this.environment.getObjectModel()).andReturn(null);
        expect(this.environment.getOutputStream(0)).andReturn(null);
        this.serializer.setOutputStream(null);
        replay(this.environment, this.beanFactory, this.generator, this.transformer, this.serializer, consumer);
        this.pipeline.setGenerator(null, null, null, null);
        this.pipeline.addTransformer(null, null, null, null);
        this.pipeline.setSerializer(null, null, null, null, "text/xml");
        this.pipeline.prepareInternal(this.environment);
        this.pipeline.process(this.environment, consumer);
        verify(this.environment, this.beanFactory, this.generator, this.transformer, this.serializer, consumer);
    }

    @Test
    public void testSetErrorHandler() {
        this.pipeline.setErrorHandler(null);
        try {
            this.pipeline.setErrorHandler(createMock(SitemapErrorHandler.class));
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void testSetGetGenerator() throws ProcessingException {
        expect(this.beanFactory.getBean("org.apache.cocoon.generation.Generator/null")).andReturn(this.generator);
        replay(this.beanFactory, this.generator);
        this.pipeline.setGenerator(null, null, null, null);
        assertEquals(this.generator, this.pipeline.getGenerator());
        verify(this.beanFactory, this.generator);
    }

    @Test
    public void testSetProcessorManager() {
        this.pipeline.setProcessorManager(null);
    }

    @Test
    public void testSetSerializer() throws ProcessingException {
        expect(this.beanFactory.getBean("org.apache.cocoon.generation.Generator/null")).andReturn(this.generator);
        expect(this.beanFactory.getBean("org.apache.cocoon.serialization.Serializer/null")).andReturn(this.serializer);
        replay(this.beanFactory, this.generator, this.serializer);
        this.pipeline.setGenerator(null, null, null, null);
        this.pipeline.setSerializer(null, null, null, null, "text/plain");
        verify(this.beanFactory, this.generator, this.serializer);
    }

    @Test
    public void testSetup() {
        expect(this.parameters.getParameter("expires", null)).andReturn("access plus 1 seconds");
        expect(this.parameters.getParameterAsInteger("outputBufferSize", 0)).andReturn(1024);
        replay(this.parameters);
        this.pipeline.setup(this.parameters);
        assertEquals(1000, this.pipeline.getExpires());
        assertEquals(1024, this.pipeline.getOutputBufferSize());
        verify(this.parameters);
    }
}
