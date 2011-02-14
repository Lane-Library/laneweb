package edu.stanford.irt.laneweb.cocoon.pipeline;

import static org.junit.Assert.*;
import static org.easymock.classextension.EasyMock.*;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import org.apache.cocoon.caching.Cache;
import org.apache.cocoon.caching.ComponentCacheKey;
import org.apache.cocoon.caching.PipelineCacheKey;
import org.apache.cocoon.environment.Environment;
import org.apache.cocoon.serialization.XMLSerializer;
import org.apache.cocoon.xml.XMLConsumer;
import org.apache.excalibur.source.SourceValidity;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;

import edu.stanford.irt.laneweb.cocoon.HTMLGenerator;


public class CachingPipelineTest {
    
    private CachingPipeline pipeline;
    
    private BeanFactory beanFactory;
    
    private HTMLGenerator generator;
    
    private Environment environment;
    
    private XMLSerializer serializer;

    private HashMap objectModel;
    
    private Cache cache;
    
    private XMLConsumer xmlConsumer;
    
    private SourceValidity validity;
    
    private ByteArrayOutputStream baos;

    @Before
    public void setUp() throws Exception {
        this.beanFactory = createMock(BeanFactory.class);
        this.generator = createMock(HTMLGenerator.class);
        this.environment = createMock(Environment.class);
        this.serializer = createMock(XMLSerializer.class);
        this.objectModel = new HashMap();
        this.cache = createMock(Cache.class);
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.validity = createMock(SourceValidity.class);
        this.baos = new ByteArrayOutputStream();
        this.pipeline = new CachingPipeline(null, this.cache, null);
    }

    @Test
    public void testCacheResults() throws Exception {
//        this.pipeline.beanFactory = this.beanFactory;
//        expect(this.beanFactory.getBean("org.apache.cocoon.generation.Generator/")).andReturn(this.generator);
//        expect(this.beanFactory.getBean("org.apache.cocoon.serialization.Serializer/")).andReturn(this.serializer);
//        expect(this.environment.getObjectModel()).andReturn(this.objectModel);
//        expect(this.generator.getKey()).andReturn("");
//        this.generator.setup(null,null,null,null);
//        expect(this.cache.get(isA(PipelineCacheKey.class))).andReturn(null);
//        expect(this.generator.getValidity()).andReturn(this.validity);
//        expect(this.serializer.getMimeType()).andReturn("text/xml");
//        expect(this.serializer.shouldSetContentLength()).andReturn(Boolean.FALSE);
//        this.environment.setContentType("text/xml");
//        expect(this.environment.getURIPrefix()).andReturn(null);
//        expect(this.environment.getURI()).andReturn(null);
//        expect(this.environment.getOutputStream(0)).andReturn(this.baos);
//        this.serializer.setOutputStream(this.baos);
//        this.generator.generate();
//        replay(this.beanFactory, this.generator, this.environment, this.serializer, this.cache, this.xmlConsumer, this.validity);
//        this.pipeline.setGenerator("", null, null, null);
//        this.pipeline.setSerializer("", null, null, null, null);
////        this.pipeline.prepareInternal(this.environment);
//        this.pipeline.process(this.environment, this.xmlConsumer);
////        this.pipeline.generateCachingKey(null);
////        assertNotNull(this.pipeline.cacheResults(this.environment, null));
//        verify(this.beanFactory, this.generator, this.environment, this.serializer, this.cache, this.xmlConsumer, this.validity);
    }
}
