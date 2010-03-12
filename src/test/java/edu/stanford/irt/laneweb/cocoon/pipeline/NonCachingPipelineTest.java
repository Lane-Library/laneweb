package edu.stanford.irt.laneweb.cocoon.pipeline;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.components.pipeline.AbstractProcessingPipeline;
import org.apache.cocoon.environment.Environment;
import org.apache.cocoon.generation.Generator;
import org.apache.cocoon.reading.Reader;
import org.apache.cocoon.serialization.Serializer;
import org.apache.cocoon.transformation.Transformer;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;


public class NonCachingPipelineTest {
    
    private NonCachingPipeline pipeline;
    
    private Parameters parameters;
    
    private ServiceManager serviceManager;
    
    private Generator generator;
    
    private Transformer transformer;
    
    private Serializer serializer;
    
    private Reader reader;
    
    private Environment environment;

    @Before
    public void setUp() throws Exception {
        this.pipeline = new NonCachingPipeline();
        this.parameters = createMock(Parameters.class);
        this.serviceManager = createMock(ServiceManager.class);
        this.generator = createMock(Generator.class);
        this.transformer = createMock(Transformer.class);
        this.serializer = createMock(Serializer.class);
        this.reader = createMock(Reader.class);
        this.environment = createMock(Environment.class);
        this.pipeline.service(this.serviceManager);
    }

    @Test
    public void testNonCachingProcessingPipeline() {
        new NonCachingPipeline();
    }

    @Test
    public void testAbstractProcessingPipeline() {
        new AbstractProcessingPipeline(){};
    }

    @Test
    public void testService() throws ServiceException {
        this.pipeline.service(null);
    }

    @Test
    public void testSetProcessorManager() {
        this.pipeline.setProcessorManager(null);
    }

    @Test
    public void testParameterize() throws ParameterException {
        expect(this.parameters.getParameter("expires", null)).andReturn(null);
        expect(this.parameters.getParameterAsInteger("outputBufferSize", -1)).andReturn(-1);
        replay(this.parameters);
        this.pipeline.parameterize(this.parameters);
        verify(this.parameters);
    }

    @Test
    public void testSetup() {
        expect(this.parameters.getParameter("expires", null)).andReturn(null);
        expect(this.parameters.getParameterAsInteger("outputBufferSize", 0)).andReturn(0);
        replay(this.parameters);
        this.pipeline.setup(this.parameters);
        verify(this.parameters);
    }

    @Test
    public void testInformBranchPoint() {
        this.pipeline.informBranchPoint();
    }

    @Test
    public void testGetGenerator() {
        this.pipeline.getGenerator();
    }

    @Test
    public void testSetGenerator() throws ProcessingException, ServiceException {
        expect(this.serviceManager.lookup("org.apache.cocoon.generation.Generator/foo")).andReturn(this.generator);
        replay(this.serviceManager, this.generator);
        this.pipeline.setGenerator("foo", null, null, null);
        verify(this.serviceManager, this.generator);
    }

    @Test
    public void testAddTransformer() throws ProcessingException, ServiceException {
        expect(this.serviceManager.lookup("org.apache.cocoon.generation.Generator/foo")).andReturn(this.generator);
        expect(this.serviceManager.lookup("org.apache.cocoon.transformation.Transformer/foo")).andReturn(this.transformer);
        replay(this.serviceManager, this.generator, this.transformer);
        this.pipeline.setGenerator("foo", null, null, null);
        this.pipeline.addTransformer("foo", null, null, null);
        verify(this.serviceManager, this.generator, this.transformer);
    }

    @Test
    public void testSetSerializer() throws ProcessingException, ServiceException {
        expect(this.serviceManager.lookup("org.apache.cocoon.generation.Generator/foo")).andReturn(this.generator);
        expect(this.serviceManager.lookup("org.apache.cocoon.serialization.Serializer/foo")).andReturn(this.serializer);
        replay(this.serviceManager, this.generator, this.serializer);
        this.pipeline.setGenerator("foo", null, null, null);
        this.pipeline.setSerializer("foo", null, null, null, null);
        verify(this.serviceManager, this.generator, this.serializer);
    }

    @Test
    public void testSetReader() throws ProcessingException, ServiceException {
        expect(this.serviceManager.lookup("org.apache.cocoon.reading.Reader/foo")).andReturn(this.reader);
        replay(this.serviceManager, this.reader);
        this.pipeline.setReader("foo", null, null, null);
        verify(this.serviceManager, this.reader);
    }

    @Test
    public void testSetErrorHandler() {
        this.pipeline.setErrorHandler(null);
    }

    @Test
    public void testPrepareInternal() throws ProcessingException, ServiceException, SAXException, IOException {
        expect(this.serviceManager.lookup("org.apache.cocoon.reading.Reader/foo")).andReturn(this.reader);
        expect(this.serviceManager.lookup("org.apache.excalibur.source.SourceResolver")).andReturn(null);
        expect(this.environment.getObjectModel()).andReturn(null);
        this.reader.setup(null, null, null, this.parameters);
        expect(this.parameters.isParameter("expires")).andReturn(false);
        replay(this.serviceManager, this.reader, this.environment, this.parameters);
        this.pipeline.setReader("foo", null, this.parameters, null);
        this.pipeline.prepareInternal(this.environment);
        verify(this.serviceManager, this.reader, this.environment, this.parameters);
    }

    @Test
    public void testRecycle() {
        try {
            this.pipeline.recycle();
            fail("calling this should fail, has pipline scope in the application");
        } catch (UnsupportedOperationException e) {
            //not supported
        }
    }

    @Test
    public void testProcessEnvironmentXMLConsumer() throws ProcessingException, ServiceException, IOException, SAXException {
        expect(this.serviceManager.lookup("org.apache.cocoon.generation.Generator/foo")).andReturn(this.generator);
        expect(this.serviceManager.lookup("org.apache.cocoon.transformation.Transformer/foo")).andReturn(this.transformer);
        this.generator.setConsumer(this.transformer);
        this.generator.generate();
        this.transformer.setConsumer(null);
        this.environment.setContentType("text/xml");
        replay(this.environment, this.serviceManager, this.generator, this.transformer);
        this.pipeline.setGenerator("foo", null, null, null);
        this.pipeline.addTransformer("foo", null, null, null);
        this.pipeline.process(this.environment, null);
        verify(this.environment, this.serviceManager, this.generator, this.transformer);
    }

    @Test
    public void testGetValidityForEventPipeline() {
        this.pipeline.getValidityForEventPipeline();
    }

    @Test
    public void testGetKeyForEventPipeline() {
        this.pipeline.getKeyForEventPipeline();
    }
}
