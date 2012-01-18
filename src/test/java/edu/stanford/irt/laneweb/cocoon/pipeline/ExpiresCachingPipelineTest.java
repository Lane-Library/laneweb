package edu.stanford.irt.laneweb.cocoon.pipeline;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.caching.Cache;
import org.apache.cocoon.caching.CachedResponse;
import org.apache.cocoon.caching.CachingOutputStream;
import org.apache.cocoon.caching.IdentifierCacheKey;
import org.apache.cocoon.environment.Environment;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.Generator;
import org.apache.cocoon.serialization.Serializer;
import org.apache.cocoon.transformation.Transformer;
import org.apache.cocoon.xml.XMLConsumer;
import org.apache.excalibur.source.SourceValidity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.xml.sax.SAXException;

public class ExpiresCachingPipelineTest {

    private BeanFactory beanFactory;

    private Cache cache;

    private Environment environment;

    private Generator generator;

    private Parameters parameters;

    private ExpiresCachingPipeline pipeline;

    private Serializer serializer;

    private SourceResolver sourceResolver;

	private Transformer transformer;

	private SourceValidity validity;

	private CachedResponse cachedResponse;

	private OutputStream outputStream;

	private XMLConsumer xmlConsumer;

	@Before
	public void setUp() throws Exception {
		this.sourceResolver = createMock(SourceResolver.class);
		this.cache = createMock(Cache.class);
		this.pipeline = new ExpiresCachingPipeline(this.sourceResolver, this.cache, 0);
        this.beanFactory = createMock(BeanFactory.class);
        this.pipeline.setBeanFactory(this.beanFactory);
        this.generator = createMock(Generator.class);
        this.serializer = createMock(Serializer.class);
        this.environment = createMock(Environment.class);
        this.parameters = createMock(Parameters.class);
        this.transformer = createMock(Transformer.class);
        this.validity = createMock(SourceValidity.class);
        this.cachedResponse = createMock(CachedResponse.class);
        this.outputStream = createMock(OutputStream.class);
        this.xmlConsumer = createMock(XMLConsumer.class);
    }
	
	private void replayAll() {
		replay(this.sourceResolver, this.cache, this.beanFactory, this.generator, this.serializer, this.environment,
				this.parameters, this.transformer, this.validity, this.cachedResponse, this.outputStream, this.xmlConsumer);
	}
	
	private void resetAll() {
		reset(this.sourceResolver, this.cache, this.beanFactory, this.generator, this.serializer, this.environment,
				this.parameters, this.transformer, this.validity, this.cachedResponse, this.outputStream, this.xmlConsumer);
	}
	
	@After
	public void verifyAll() {
		verify(this.sourceResolver, this.cache, this.beanFactory, this.generator, this.serializer, this.environment,
				this.parameters, this.transformer, this.validity, this.cachedResponse, this.outputStream, this.xmlConsumer);
	}
    
    private void doInvokeContextStuff() {
        //InvokeContext does these three things, called in GenerateNode:
        this.pipeline.setProcessorManager(null);
        expect(this.parameters.getParameter("expires", null)).andReturn(null);
        expect(this.parameters.getParameterAsInteger("outputBufferSize", 0)).andReturn(0);
        replay(this.parameters);
        this.pipeline.setup(this.parameters);
        verify(this.parameters);
        reset(this.parameters);
        this.pipeline.setErrorHandler(null);
    }
    
    private void doProcessingNodeStuff() {
    	//This gets done in the various processing nodes:
        expect(this.beanFactory.getBean("org.apache.cocoon.generation.Generator/type")).andReturn(this.generator);
        expect(this.beanFactory.getBean("org.apache.cocoon.transformation.Transformer/type")).andReturn(this.transformer).times(2);
        expect(this.beanFactory.getBean("org.apache.cocoon.serialization.Serializer/type")).andReturn(this.serializer);
        replayAll();
        this.pipeline.setGenerator("type", null, null, null);
        this.pipeline.addTransformer("type", null, null, null);
        this.pipeline.addTransformer("type", null, null, null);
        this.pipeline.setSerializer("type", null, null, null, null);
        verifyAll();
        resetAll();
    }
	
	@Test
	public void testProcessEnvironment() throws ProcessingException, IOException, SAXException {
		doInvokeContextStuff();
		doProcessingNodeStuff();
		expect(this.parameters.getParameter("cache-key", null)).andReturn("key");
		expect(this.parameters.getParameterAsLong("cache-expires", 3600L)).andReturn(3600L);
		expect(this.parameters.getParameterAsBoolean("purge-cache", false)).andReturn(false);
		expect(this.environment.getObjectModel()).andReturn(null);
		this.generator.setup(this.sourceResolver, null, null, null);
		this.transformer.setup(this.sourceResolver, null, null, null);
		expectLastCall().times(2);
		expect(this.cache.get(isA(IdentifierCacheKey.class))).andReturn(null);
		this.generator.setConsumer(this.transformer);
		this.transformer.setConsumer(this.transformer);
		this.transformer.setConsumer(this.serializer);
		expect(this.environment.getOutputStream(0)).andReturn(this.outputStream);
		this.serializer.setOutputStream(isA(CachingOutputStream.class));
		this.generator.generate();
		this.cache.store(isA(IdentifierCacheKey.class), isA(CachedResponse.class));
		replayAll();
		this.pipeline.process(this.environment);
	}
	
	@Test
	public void testProcessEnvironmentCached() throws ProcessingException, IOException, SAXException {
		doInvokeContextStuff();
		doProcessingNodeStuff();
		expect(this.parameters.getParameter("cache-key", null)).andReturn("key");
		expect(this.parameters.getParameterAsLong("cache-expires", 3600L)).andReturn(3600L);
		expect(this.parameters.getParameterAsBoolean("purge-cache", false)).andReturn(false);
		expect(this.environment.getObjectModel()).andReturn(null);
		this.generator.setup(this.sourceResolver, null, null, null);
		this.transformer.setup(this.sourceResolver, null, null, null);
		expectLastCall().times(2);
		expect(this.cache.get(isA(IdentifierCacheKey.class))).andReturn(this.cachedResponse);
		expect(this.cachedResponse.getValidityObjects()).andReturn(new SourceValidity[]{this.validity});
		expect(this.validity.isValid()).andReturn(SourceValidity.VALID);
		expect(this.cachedResponse.getResponse()).andReturn(new byte[0]);
		expect(this.environment.getOutputStream(0)).andReturn(this.outputStream);
		this.outputStream.write(isA(byte[].class));
		replayAll();
		this.pipeline.process(this.environment);
	}
	
	@Test
	public void testProcessEnvironmentCachedExpired() throws ProcessingException, IOException, SAXException {
		doInvokeContextStuff();
		doProcessingNodeStuff();
		expect(this.parameters.getParameter("cache-key", null)).andReturn("key");
		expect(this.parameters.getParameterAsLong("cache-expires", 3600L)).andReturn(3600L);
		expect(this.parameters.getParameterAsBoolean("purge-cache", false)).andReturn(false);
		expect(this.environment.getObjectModel()).andReturn(null);
		this.generator.setup(this.sourceResolver, null, null, null);
		this.transformer.setup(this.sourceResolver, null, null, null);
		expectLastCall().times(2);
		expect(this.cache.get(isA(IdentifierCacheKey.class))).andReturn(this.cachedResponse);
		expect(this.cachedResponse.getValidityObjects()).andReturn(new SourceValidity[]{this.validity});
		expect(this.validity.isValid()).andReturn(SourceValidity.INVALID);
		this.cache.remove(isA(IdentifierCacheKey.class));
		this.generator.setConsumer(this.transformer);
		this.transformer.setConsumer(this.transformer);
		this.transformer.setConsumer(this.serializer);
		expect(this.environment.getOutputStream(0)).andReturn(this.outputStream);
		this.serializer.setOutputStream(isA(CachingOutputStream.class));
		this.generator.generate();
		this.cache.store(isA(IdentifierCacheKey.class), isA(CachedResponse.class));
		replayAll();
		this.pipeline.process(this.environment);
	}
	
	@Test
	public void testInternalProcessEnvironment() throws ProcessingException, SAXException, IOException {
		doInvokeContextStuff();
		doProcessingNodeStuff();
		expect(this.parameters.getParameter("cache-key", null)).andReturn("key");
		expect(this.parameters.getParameterAsLong("cache-expires", 3600L)).andReturn(3600L);
		expect(this.parameters.getParameterAsBoolean("purge-cache", false)).andReturn(false);
		expect(this.environment.getObjectModel()).andReturn(null);
		this.generator.setup(this.sourceResolver, null, null, null);
		this.transformer.setup(this.sourceResolver, null, null, null);
		expectLastCall().times(2);
		expect(this.cache.get(isA(IdentifierCacheKey.class))).andReturn(null);
		this.generator.setConsumer(this.transformer);
		this.transformer.setConsumer(this.transformer);
		this.transformer.setConsumer(this.serializer);
		expect(this.environment.getOutputStream(0)).andReturn(this.outputStream);
		this.serializer.setOutputStream(isA(CachingOutputStream.class));
		this.generator.generate();
		this.cache.store(isA(IdentifierCacheKey.class), isA(CachedResponse.class));
		replayAll();
		this.pipeline.prepareInternal(this.environment);
		this.pipeline.getValidityForEventPipeline();
		this.pipeline.getKeyForEventPipeline();
		this.pipeline.process(this.environment);
	}
	
	@Test
	public void testInternalProcessEnvironmentCached() throws ProcessingException, SAXException, IOException {
		doInvokeContextStuff();
		doProcessingNodeStuff();
		expect(this.parameters.getParameter("cache-key", null)).andReturn("key");
		expect(this.parameters.getParameterAsLong("cache-expires", 3600L)).andReturn(3600L);
		expect(this.parameters.getParameterAsBoolean("purge-cache", false)).andReturn(false);
		expect(this.environment.getObjectModel()).andReturn(null);
		this.generator.setup(this.sourceResolver, null, null, null);
		this.transformer.setup(this.sourceResolver, null, null, null);
		expectLastCall().times(2);
		expect(this.cache.get(isA(IdentifierCacheKey.class))).andReturn(this.cachedResponse);
		expect(this.cachedResponse.getValidityObjects()).andReturn(new SourceValidity[]{this.validity});
		expect(this.validity.isValid()).andReturn(SourceValidity.VALID);
		expect(this.cachedResponse.getResponse()).andReturn(new byte[0]);
		expect(this.environment.getOutputStream(0)).andReturn(this.outputStream);
		this.outputStream.write(isA(byte[].class));
		replayAll();
		this.pipeline.prepareInternal(this.environment);
		this.pipeline.getValidityForEventPipeline();
		this.pipeline.getKeyForEventPipeline();
		this.pipeline.process(this.environment);
	}
	
	@Test
	public void testInternalProcessEnvironmentCachedExpired() throws ProcessingException, SAXException, IOException {
		doInvokeContextStuff();
		doProcessingNodeStuff();
		expect(this.parameters.getParameter("cache-key", null)).andReturn("key");
		expect(this.parameters.getParameterAsLong("cache-expires", 3600L)).andReturn(3600L);
		expect(this.parameters.getParameterAsBoolean("purge-cache", false)).andReturn(false);
		expect(this.environment.getObjectModel()).andReturn(null);
		this.generator.setup(this.sourceResolver, null, null, null);
		this.transformer.setup(this.sourceResolver, null, null, null);
		expectLastCall().times(2);
		expect(this.cache.get(isA(IdentifierCacheKey.class))).andReturn(this.cachedResponse);
		expect(this.cachedResponse.getValidityObjects()).andReturn(new SourceValidity[]{this.validity});
		expect(this.validity.isValid()).andReturn(SourceValidity.INVALID);
		this.cache.remove(isA(IdentifierCacheKey.class));
		this.generator.setConsumer(this.transformer);
		this.transformer.setConsumer(this.transformer);
		this.transformer.setConsumer(this.serializer);
		expect(this.environment.getOutputStream(0)).andReturn(this.outputStream);
		this.serializer.setOutputStream(isA(CachingOutputStream.class));
		this.generator.generate();
		this.cache.store(isA(IdentifierCacheKey.class), isA(CachedResponse.class));
		replayAll();
		this.pipeline.prepareInternal(this.environment);
		this.pipeline.getValidityForEventPipeline();
		this.pipeline.getKeyForEventPipeline();
		this.pipeline.process(this.environment);
	}
	
//	@Test
	public void testProcessEnvironmentXMLConsumer() throws ProcessingException, SAXException, IOException {
		doInvokeContextStuff();
		doProcessingNodeStuff();
		expect(this.parameters.getParameter("cache-key", null)).andReturn("key");
		expect(this.parameters.getParameterAsLong("cache-expires", 3600L)).andReturn(3600L);
		expect(this.parameters.getParameterAsBoolean("purge-cache", false)).andReturn(false);
		expect(this.environment.getObjectModel()).andReturn(null);
		this.generator.setup(this.sourceResolver, null, null, null);
		this.transformer.setup(this.sourceResolver, null, null, null);
		expectLastCall().times(2);
		replayAll();
		this.pipeline.prepareInternal(this.environment);
		this.pipeline.getValidityForEventPipeline();
		this.pipeline.getKeyForEventPipeline();
		this.pipeline.process(this.environment, this.xmlConsumer);
	}
	
//	@Test
	public void testProcessEnvironmentXMLConsumerCached() throws ProcessingException, SAXException, IOException {
		doInvokeContextStuff();
		doProcessingNodeStuff();
		expect(this.parameters.getParameter("cache-key", null)).andReturn("key");
		expect(this.parameters.getParameterAsLong("cache-expires", 3600L)).andReturn(3600L);
		expect(this.parameters.getParameterAsBoolean("purge-cache", false)).andReturn(false);
		expect(this.environment.getObjectModel()).andReturn(null);
		this.generator.setup(this.sourceResolver, null, null, null);
		this.transformer.setup(this.sourceResolver, null, null, null);
		expectLastCall().times(2);
		replayAll();
		this.pipeline.prepareInternal(this.environment);
		this.pipeline.getValidityForEventPipeline();
		this.pipeline.getKeyForEventPipeline();
		this.pipeline.process(this.environment, this.xmlConsumer);
	}
	
//	@Test
	public void testProcessEnvironmentXMLConsumerCachedExpired() throws ProcessingException, SAXException, IOException {
		doInvokeContextStuff();
		doProcessingNodeStuff();
		expect(this.parameters.getParameter("cache-key", null)).andReturn("key");
		expect(this.parameters.getParameterAsLong("cache-expires", 3600L)).andReturn(3600L);
		expect(this.parameters.getParameterAsBoolean("purge-cache", false)).andReturn(false);
		expect(this.environment.getObjectModel()).andReturn(null);
		this.generator.setup(this.sourceResolver, null, null, null);
		this.transformer.setup(this.sourceResolver, null, null, null);
		expectLastCall().times(2);
		replayAll();
		this.pipeline.prepareInternal(this.environment);
		this.pipeline.getValidityForEventPipeline();
		this.pipeline.getKeyForEventPipeline();
		this.pipeline.process(this.environment, this.xmlConsumer);
	}

//	@Test
	public void testGetKeyForEventPipeline() {
		fail("Not yet implemented");
	}

//	@Test
	public void testGetValidityForEventPipeline() {
		fail("Not yet implemented");
	}

}
