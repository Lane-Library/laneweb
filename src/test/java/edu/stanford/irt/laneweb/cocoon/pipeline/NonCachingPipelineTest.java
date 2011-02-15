package edu.stanford.irt.laneweb.cocoon.pipeline;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.IOException;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.components.pipeline.AbstractProcessingPipeline;
import org.apache.cocoon.environment.Environment;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.Generator;
import org.apache.cocoon.reading.Reader;
import org.apache.cocoon.serialization.Serializer;
import org.apache.cocoon.transformation.Transformer;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.xml.sax.SAXException;

public class NonCachingPipelineTest {

    private BeanFactory beanFactory;

    private Environment environment;

    private Generator generator;

    private Parameters parameters;

    private NonCachingPipeline pipeline;

    private Reader reader;

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
        this.reader = createMock(Reader.class);
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
        verify(this.beanFactory, this.generator, this.transformer);
    }

    @Test
    public void testGetGenerator() {
        this.pipeline.getGenerator();
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
    public void testPrepareInternal() throws ProcessingException, SAXException, IOException {
        expect(this.beanFactory.getBean("org.apache.cocoon.reading.Reader/foo")).andReturn(this.reader);
        expect(this.environment.getObjectModel()).andReturn(null);
        this.reader.setup(null, null, null, this.parameters);
        expect(this.parameters.isParameter("expires")).andReturn(false);
        replay(this.beanFactory, this.reader, this.environment, this.parameters);
        this.pipeline.setReader("foo", null, this.parameters, null);
        this.pipeline.prepareInternal(this.environment);
        verify(this.beanFactory, this.reader, this.environment, this.parameters);
    }

    @Test
    public void testProcessEnvironmentXMLConsumer() throws ProcessingException, IOException, SAXException {
        expect(this.beanFactory.getBean("org.apache.cocoon.generation.Generator/foo")).andReturn(this.generator);
        expect(this.beanFactory.getBean("org.apache.cocoon.transformation.Transformer/foo")).andReturn(this.transformer);
        this.generator.setConsumer(this.transformer);
        this.generator.generate();
        this.transformer.setConsumer(null);
        this.environment.setContentType("text/xml");
        replay(this.environment, this.beanFactory, this.generator, this.transformer);
        this.pipeline.setGenerator("foo", null, null, null);
        this.pipeline.addTransformer("foo", null, null, null);
        this.pipeline.process(this.environment, null);
        verify(this.environment, this.beanFactory, this.generator, this.transformer);
    }

    @Test
    public void testSetErrorHandler() {
        this.pipeline.setErrorHandler(null);
    }

    @Test
    public void testSetGenerator() throws ProcessingException {
        expect(this.beanFactory.getBean("org.apache.cocoon.generation.Generator/foo")).andReturn(this.generator);
        replay(this.beanFactory, this.generator);
        this.pipeline.setGenerator("foo", null, null, null);
        verify(this.beanFactory, this.generator);
    }

    @Test
    public void testSetProcessorManager() {
        this.pipeline.setProcessorManager(null);
    }

    @Test
    public void testSetReader() throws ProcessingException {
        expect(this.beanFactory.getBean("org.apache.cocoon.reading.Reader/foo")).andReturn(this.reader);
        replay(this.beanFactory, this.reader);
        this.pipeline.setReader("foo", null, null, null);
        verify(this.beanFactory, this.reader);
    }

    @Test
    public void testSetSerializer() throws ProcessingException {
        expect(this.beanFactory.getBean("org.apache.cocoon.generation.Generator/foo")).andReturn(this.generator);
        expect(this.beanFactory.getBean("org.apache.cocoon.serialization.Serializer/foo")).andReturn(this.serializer);
        replay(this.beanFactory, this.generator, this.serializer);
        this.pipeline.setGenerator("foo", null, null, null);
        this.pipeline.setSerializer("foo", null, null, null, null);
        verify(this.beanFactory, this.generator, this.serializer);
    }

    @Test
    public void testSetup() {
        expect(this.parameters.getParameter("expires", null)).andReturn(null);
        expect(this.parameters.getParameterAsInteger("outputBufferSize", 0)).andReturn(0);
        replay(this.parameters);
        this.pipeline.setup(this.parameters);
        verify(this.parameters);
    }
}
