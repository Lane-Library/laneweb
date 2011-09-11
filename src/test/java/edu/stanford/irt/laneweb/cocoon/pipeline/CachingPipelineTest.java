package edu.stanford.irt.laneweb.cocoon.pipeline;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import java.io.IOException;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.caching.Cache;
import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.cocoon.caching.PipelineCacheKey;
import org.apache.cocoon.components.sax.XMLTeePipe;
import org.apache.cocoon.environment.Environment;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.Generator;
import org.apache.cocoon.serialization.Serializer;
import org.apache.excalibur.store.Store;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.xml.sax.SAXException;

public class CachingPipelineTest {
	
	private CachingPipeline pipeline;
	private Store transientStore;
	private SourceResolver sourceResolver;
	private Cache cache;
	private BeanFactory beanFactory;
	private CacheableSerializer serializer;
	private CacheableGenerator generator;
	private Environment environment;
	private Parameters parameters;
	private static interface CacheableGenerator extends Generator, CacheableProcessingComponent {}
	private static interface CacheableSerializer extends Serializer, CacheableProcessingComponent {}

	private void replayAll() {
		replay(this.transientStore, this.sourceResolver, this.cache, this.beanFactory, this.serializer, this.generator, this.environment, this.parameters);
	}
	
	private void verifyAll() {
		verify(this.transientStore, this.sourceResolver, this.cache, this.beanFactory, this.serializer, this.generator, this.environment, this.parameters);
	}
	@Before
	public void setUp() throws Exception {
		this.transientStore = createMock(Store.class);
		this.sourceResolver = createMock(SourceResolver.class);
		this.cache = createMock(Cache.class);
		this.pipeline = new CachingPipeline(this.sourceResolver, this.cache, this.transientStore);
		this.beanFactory = createMock(BeanFactory.class);
		this.pipeline.setBeanFactory(this.beanFactory);
		this.generator = createMock(CacheableGenerator.class);
		this.serializer = createMock(CacheableSerializer.class);
		this.environment = createMock(Environment.class);
		this.parameters = createMock(Parameters.class);
	}

	@Test
	public void testAddTransformer() {
		expect(this.beanFactory.getBean("org.apache.cocoon.transformation.Transformer/null")).andReturn(null);
		replayAll();
		this.pipeline.addTransformer(null, null, null, null);
		verifyAll();
	}

	@Test
	public void testGetKeyForEventPipeline() {
		replayAll();
		this.pipeline.getKeyForEventPipeline();
		verifyAll();
	}

	@Test
	public void testGetValidityForEventPipeline() {
		replayAll();
		this.pipeline.getValidityForEventPipeline();
		verifyAll();
	}

	@Test
	public void testSetGenerator() {
		expect(this.beanFactory.getBean("org.apache.cocoon.generation.Generator/null")).andReturn(this.generator);
		replayAll();
		this.pipeline.setGenerator(null, null, null, null);
		verifyAll();
	}

	@Test
	public void testSetSerializer() {
		expect(this.beanFactory.getBean("org.apache.cocoon.serialization.Serializer/null")).andReturn(null);
		replayAll();
		this.pipeline.setSerializer(null, null, null, null, "text/xml");
		verifyAll();
	}

	@Test
	public void testConnectPipeline() throws ProcessingException {
		expect(this.beanFactory.getBean("org.apache.cocoon.generation.Generator/null")).andReturn(this.generator);
		expect(this.beanFactory.getBean("org.apache.cocoon.serialization.Serializer/null")).andReturn(this.serializer);
		this.generator.setConsumer(this.serializer);
		replayAll();
		this.pipeline.setGenerator(null, null, null, null);
		this.pipeline.setSerializer(null, null, null, null, "text/xml");
		this.pipeline.connectPipeline(this.environment);
		verifyAll();
	}

	@Test
	public void testProcessXMLPipeline() throws ProcessingException, IOException, SAXException {
		expect(this.beanFactory.getBean("org.apache.cocoon.generation.Generator/null")).andReturn(this.generator);
		this.environment.setContentType("text/xml");
		this.generator.generate();
		replayAll();
		this.pipeline.setGenerator(null, null, null, null);
		this.pipeline.processXMLPipeline(this.environment);
		verifyAll();
	}

	@Test
	public void testSetupPipeline() {
		expect(this.parameters.getParameter("expires", null)).andReturn(null);
		expect(this.parameters.getParameterAsInteger("outputBufferSize", 0)).andReturn(0);
		replayAll();
		this.pipeline.setup(this.parameters);
		verifyAll();
	}

	@Test
	public void testCachingPipeline() {
		replayAll();
		new CachingPipeline(null, null, null);
		verifyAll();
	}

	@Test
	public void testCacheResults() throws ProcessingException {
		replayAll();
		this.pipeline.cacheResults(null, null);
		verifyAll();
	}

	@Test
	public void testConnectCachingPipeline() throws ProcessingException {
		expect(this.beanFactory.getBean("org.apache.cocoon.generation.Generator/null")).andReturn(this.generator);
		expect(this.beanFactory.getBean("org.apache.cocoon.serialization.Serializer/null")).andReturn(this.serializer);
		this.generator.setConsumer(isA(XMLTeePipe.class));
		replayAll();
		this.pipeline.setGenerator(null, null, null, null);
		this.pipeline.setSerializer(null, null, null, null, "text/xml");
		this.pipeline.connectCachingPipeline(this.environment);
		verifyAll();
	}

	@Test
	public void testGenerateCachingKey() throws ProcessingException {
		replayAll();
		this.pipeline.generateCachingKey(this.environment);
		verifyAll();
	}

	@Test
	public void testGenerateLock() throws IOException {
		PipelineCacheKey key = new PipelineCacheKey();
		expect(this.transientStore.containsKey("PIPELOCK:PK")).andReturn(false);
		this.transientStore.store("PIPELOCK:PK", null);
		replayAll();
		this.pipeline.generateLock(key);
		verifyAll();
	}

	@Test
	public void testNewComponentCacheKey() {
		replayAll();
		this.pipeline.newComponentCacheKey(0, "", "");
		verifyAll();
	}

	@Test
	public void testReleaseLock() {
		replayAll();
		this.pipeline.releaseLock(null);
		verifyAll();
	}

	@Test
	public void testSetupValidities() throws ProcessingException {
		replayAll();
		this.pipeline.setupValidities();
		verifyAll();
	}

	@Test
	public void testValidatePipeline() throws ProcessingException {
		expect(this.beanFactory.getBean("org.apache.cocoon.generation.Generator/g")).andReturn(this.generator);
		expect(this.beanFactory.getBean("org.apache.cocoon.serialization.Serializer/s")).andReturn(this.serializer);
		expect(this.generator.getKey()).andReturn("G");
		expect(this.serializer.getKey()).andReturn("S");
		expect(this.cache.get(isA(PipelineCacheKey.class))).andReturn(null);
		expect(this.transientStore.get("PIPELOCK:PK_G-g-G_S-s-S")).andReturn(null);
		replayAll();
		this.pipeline.setGenerator("g", null, null, null);
		this.pipeline.setSerializer("s", null, null, null, "text/xml");
		this.pipeline.generateCachingKey(this.environment);
		this.pipeline.validatePipeline(this.environment);
		verifyAll();
	}

	@Test
	public void testWaitForLock() {
		expect(this.transientStore.get("PIPELOCK:null")).andReturn(null);
		replayAll();
		this.pipeline.waitForLock(null);
		verifyAll();
	}

	@Test
	public void testGetValidityForInternalPipeline() {
		replayAll();
		this.pipeline.getValidityForEventPipeline();
		verifyAll();
	}

	@Test
	public void testSetupFromCacheKey() {
		replayAll();
		this.pipeline.setupFromCacheKey();
		verifyAll();
	}

}
