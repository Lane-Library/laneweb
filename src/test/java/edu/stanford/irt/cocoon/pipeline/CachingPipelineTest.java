package edu.stanford.irt.cocoon.pipeline;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.caching.Cache;
import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.cocoon.caching.CachedResponse;
import org.apache.cocoon.caching.CachingOutputStream;
import org.apache.cocoon.caching.PipelineCacheKey;
import org.apache.cocoon.components.sax.XMLByteStreamCompiler;
import org.apache.cocoon.components.sax.XMLTeePipe;
import org.apache.cocoon.environment.Environment;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.Generator;
import org.apache.cocoon.serialization.Serializer;
import org.apache.cocoon.transformation.Transformer;
import org.apache.cocoon.xml.XMLConsumer;
import org.apache.excalibur.source.SourceValidity;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class CachingPipelineTest {

    private static interface CacheableGenerator extends Generator, CacheableProcessingComponent {
    }

    private static interface CacheableSerializer extends Serializer, CacheableProcessingComponent {
    }

    private static interface CacheableTransformer extends Transformer, CacheableProcessingComponent {
    }

    private BeanFactory beanFactory;

    private Cache cache;

    private CachedResponse cachedResponse;

    private Environment environment;

    private CacheableGenerator generator;

    private OutputStream outputStream;

    private Parameters parameters;

    private CachingPipeline pipeline;

    private CacheableSerializer serializer;

    private SourceResolver sourceResolver;

    private CacheableTransformer transformer;

    private SourceValidity validity;

    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() throws Exception {
        this.sourceResolver = createMock(SourceResolver.class);
        this.cache = createMock(Cache.class);
        this.pipeline = new CachingPipeline(this.sourceResolver, this.cache);
        this.beanFactory = createMock(BeanFactory.class);
        this.pipeline.setBeanFactory(this.beanFactory);
        this.generator = createMock(CacheableGenerator.class);
        this.serializer = createMock(CacheableSerializer.class);
        this.environment = createMock(Environment.class);
        this.parameters = createMock(Parameters.class);
        this.transformer = createMock(CacheableTransformer.class);
        this.validity = createMock(SourceValidity.class);
        this.cachedResponse = createMock(CachedResponse.class);
        this.outputStream = createMock(OutputStream.class);
        this.xmlConsumer = createMock(XMLConsumer.class);
    }

    @Test
    public void testGetKeyForEventPipeline() throws ProcessingException, SAXException, IOException {
        doInvokeContextStuff();
        doProcessingNodeStuff();
        expect(this.environment.getObjectModel()).andReturn(null);
        this.generator.setup(this.sourceResolver, null, null, null);
        this.transformer.setup(this.sourceResolver, null, null, null);
        expectLastCall().times(2);
        expect(this.generator.getKey()).andReturn("key");
        expect(this.generator.getValidity()).andReturn(this.validity);
        expect(this.transformer.getKey()).andReturn("key").times(2);
        expect(this.transformer.getValidity()).andReturn(this.validity).times(2);
        expect(this.serializer.getKey()).andReturn("key");
        expect(this.serializer.getValidity()).andReturn(this.validity);
        replay(this.environment, this.generator, this.transformer, this.serializer, this.cache, this.validity);
        this.pipeline.prepareInternal(this.environment);
        this.pipeline.getKeyForEventPipeline();
        verify(this.environment, this.generator, this.transformer, this.serializer, this.cache, this.validity);
    }

    @Test
    public void testGetValidityForEventPipeline() throws ProcessingException, SAXException, IOException {
        doInvokeContextStuff();
        doProcessingNodeStuff();
        expect(this.environment.getObjectModel()).andReturn(null);
        this.generator.setup(this.sourceResolver, null, null, null);
        this.transformer.setup(this.sourceResolver, null, null, null);
        expectLastCall().times(2);
        expect(this.generator.getKey()).andReturn("key");
        expect(this.generator.getValidity()).andReturn(this.validity);
        expect(this.transformer.getKey()).andReturn("key").times(2);
        expect(this.transformer.getValidity()).andReturn(this.validity).times(2);
        expect(this.serializer.getKey()).andReturn("key");
        expect(this.serializer.getValidity()).andReturn(this.validity);
        replay(this.environment, this.generator, this.transformer, this.serializer, this.cache, this.validity);
        this.pipeline.prepareInternal(this.environment);
        this.pipeline.getValidityForEventPipeline();
        verify(this.environment, this.generator, this.transformer, this.serializer, this.cache, this.validity);
    }

    @Test
    public void testProcessEnvironmentCached() throws ProcessingException, SAXException, IOException {
        doInvokeContextStuff();
        doProcessingNodeStuff();
        expect(this.environment.getObjectModel()).andReturn(null);
        this.generator.setup(this.sourceResolver, null, null, null);
        this.transformer.setup(this.sourceResolver, null, null, null);
        expectLastCall().times(2);
        expect(this.generator.getKey()).andReturn("key");
        expect(this.generator.getValidity()).andReturn(this.validity);
        expect(this.transformer.getKey()).andReturn("key").times(2);
        expect(this.transformer.getValidity()).andReturn(this.validity).times(2);
        expect(this.serializer.getKey()).andReturn("key");
        expect(this.serializer.getValidity()).andReturn(this.validity);
        expect(this.cache.get(isA(PipelineCacheKey.class))).andReturn(this.cachedResponse);
        expect(this.cachedResponse.getValidityObjects()).andReturn(
                new SourceValidity[] { this.validity, this.validity, this.validity, this.validity });
        expect(this.validity.isValid()).andReturn(SourceValidity.VALID).times(4);
        expect(this.environment.getOutputStream(0)).andReturn(this.outputStream);
        expect(this.cachedResponse.getResponse()).andReturn(new byte[1]);
        this.outputStream.write(isA(byte[].class));
        replay(this.environment, this.generator, this.transformer, this.serializer, this.cache, this.cachedResponse, this.validity,
                this.outputStream);
        this.pipeline.process(this.environment);
        verify(this.environment, this.generator, this.transformer, this.serializer, this.cache, this.cachedResponse, this.validity,
                this.outputStream);
    }

    @Test
    public void testProcessEnvironmentCachedInvalid() throws ProcessingException, SAXException, IOException {
        doInvokeContextStuff();
        doProcessingNodeStuff();
        expect(this.environment.getObjectModel()).andReturn(null);
        this.generator.setup(this.sourceResolver, null, null, null);
        this.transformer.setup(this.sourceResolver, null, null, null);
        expectLastCall().times(2);
        expect(this.generator.getKey()).andReturn("key");
        expect(this.generator.getValidity()).andReturn(this.validity);
        expect(this.transformer.getKey()).andReturn("key").times(2);
        expect(this.transformer.getValidity()).andReturn(this.validity).times(2);
        expect(this.serializer.getKey()).andReturn("key");
        expect(this.serializer.getValidity()).andReturn(this.validity);
        expect(this.cache.get(isA(PipelineCacheKey.class))).andReturn(this.cachedResponse);
        expect(this.cachedResponse.getValidityObjects()).andReturn(
                new SourceValidity[] { this.validity, this.validity, this.validity, this.validity });
        expect(this.validity.isValid()).andReturn(SourceValidity.INVALID);
        this.cache.remove(isA(PipelineCacheKey.class));
        // expect(this.generator.getValidity()).andReturn(this.validity);
        // expect(this.transformer.getValidity()).andReturn(this.validity).times(2);
        // expect(this.serializer.getValidity()).andReturn(this.validity);
        this.generator.setConsumer(this.transformer);
        this.transformer.setConsumer(this.transformer);
        this.transformer.setConsumer(this.serializer);
        expect(this.environment.getOutputStream(0)).andReturn(null);
        this.serializer.setOutputStream(isA(CachingOutputStream.class));
        this.generator.generate();
        this.cache.store(isA(PipelineCacheKey.class), isA(CachedResponse.class));
        replay(this.environment, this.generator, this.transformer, this.serializer, this.cache, this.cachedResponse, this.validity,
                this.outputStream);
        this.pipeline.process(this.environment);
        verify(this.environment, this.generator, this.transformer, this.serializer, this.cache, this.cachedResponse, this.validity,
                this.outputStream);
    }

    @Test
    public void testProcessEnvironmentInternalCached() throws ProcessingException, SAXException, IOException {
        doInvokeContextStuff();
        doProcessingNodeStuff();
        expect(this.environment.getObjectModel()).andReturn(null);
        this.generator.setup(this.sourceResolver, null, null, null);
        this.transformer.setup(this.sourceResolver, null, null, null);
        expectLastCall().times(2);
        expect(this.generator.getKey()).andReturn("key");
        expect(this.generator.getValidity()).andReturn(this.validity);
        expect(this.transformer.getKey()).andReturn("key").times(2);
        expect(this.transformer.getValidity()).andReturn(this.validity).times(2);
        expect(this.serializer.getKey()).andReturn("key");
        expect(this.serializer.getValidity()).andReturn(this.validity);
        expect(this.cache.get(isA(PipelineCacheKey.class))).andReturn(this.cachedResponse);
        expect(this.cachedResponse.getValidityObjects()).andReturn(
                new SourceValidity[] { this.validity, this.validity, this.validity, this.validity });
        expect(this.validity.isValid()).andReturn(SourceValidity.VALID).times(4);
        expect(this.environment.getOutputStream(0)).andReturn(this.outputStream);
        expect(this.cachedResponse.getResponse()).andReturn(new byte[1]);
        this.outputStream.write(isA(byte[].class));
        replay(this.environment, this.generator, this.transformer, this.serializer, this.cache, this.cachedResponse, this.validity,
                this.outputStream);
        this.pipeline.prepareInternal(this.environment);
        this.pipeline.getValidityForEventPipeline();
        this.pipeline.getKeyForEventPipeline();
        this.pipeline.process(this.environment);
        verify(this.environment, this.generator, this.transformer, this.serializer, this.cache, this.cachedResponse, this.validity,
                this.outputStream);
    }

    @Test
    public void testProcessEnvironmentInternalNotCached() throws ProcessingException, SAXException, IOException {
        doInvokeContextStuff();
        doProcessingNodeStuff();
        expect(this.environment.getObjectModel()).andReturn(null);
        this.generator.setup(this.sourceResolver, null, null, null);
        this.transformer.setup(this.sourceResolver, null, null, null);
        expectLastCall().times(2);
        expect(this.generator.getKey()).andReturn("key");
        expect(this.generator.getValidity()).andReturn(this.validity);
        expect(this.transformer.getKey()).andReturn("key").times(2);
        expect(this.transformer.getValidity()).andReturn(this.validity).times(2);
        expect(this.serializer.getKey()).andReturn("key");
        expect(this.serializer.getValidity()).andReturn(this.validity);
        expect(this.cache.get(isA(PipelineCacheKey.class))).andReturn(null);
        this.generator.setConsumer(this.transformer);
        this.transformer.setConsumer(this.transformer);
        this.transformer.setConsumer(this.serializer);
        expect(this.environment.getOutputStream(0)).andReturn(null);
        this.serializer.setOutputStream(isA(CachingOutputStream.class));
        this.generator.generate();
        this.cache.store(isA(PipelineCacheKey.class), isA(CachedResponse.class));
        replay(this.environment, this.generator, this.transformer, this.serializer, this.cache, this.cachedResponse, this.validity,
                this.outputStream);
        this.pipeline.prepareInternal(this.environment);
        this.pipeline.getValidityForEventPipeline();
        this.pipeline.getKeyForEventPipeline();
        this.pipeline.process(this.environment);
        verify(this.environment, this.generator, this.transformer, this.serializer, this.cache, this.cachedResponse, this.validity,
                this.outputStream);
    }

    @Test
    public void testProcessEnvironmentNotCached() throws ProcessingException, SAXException, IOException {
        doInvokeContextStuff();
        doProcessingNodeStuff();
        expect(this.environment.getObjectModel()).andReturn(null);
        this.generator.setup(this.sourceResolver, null, null, null);
        this.transformer.setup(this.sourceResolver, null, null, null);
        expectLastCall().times(2);
        expect(this.generator.getKey()).andReturn("key");
        expect(this.generator.getValidity()).andReturn(this.validity);
        expect(this.transformer.getKey()).andReturn("key").times(2);
        expect(this.transformer.getValidity()).andReturn(this.validity).times(2);
        expect(this.serializer.getKey()).andReturn("key");
        expect(this.serializer.getValidity()).andReturn(this.validity);
        expect(this.cache.get(isA(PipelineCacheKey.class))).andReturn(null);
        this.generator.setConsumer(this.transformer);
        this.transformer.setConsumer(this.transformer);
        this.transformer.setConsumer(this.serializer);
        expect(this.environment.getOutputStream(0)).andReturn(null);
        this.serializer.setOutputStream(isA(CachingOutputStream.class));
        this.generator.generate();
        this.cache.store(isA(PipelineCacheKey.class), isA(CachedResponse.class));
        replay(this.environment, this.generator, this.transformer, this.serializer, this.cache, this.validity);
        this.pipeline.process(this.environment);
        verify(this.environment, this.generator, this.transformer, this.serializer, this.cache, this.validity);
    }

    @Test
    public void testProcessEnvironmentXMLConsumerCached() throws ProcessingException, SAXException, IOException {
        doInvokeContextStuff();
        doProcessingNodeStuff();
        expect(this.environment.getObjectModel()).andReturn(null);
        this.generator.setup(this.sourceResolver, null, null, null);
        this.transformer.setup(this.sourceResolver, null, null, null);
        expectLastCall().times(2);
        expect(this.generator.getKey()).andReturn("key");
        expect(this.generator.getValidity()).andReturn(this.validity);
        expect(this.transformer.getKey()).andReturn("key").times(2);
        expect(this.transformer.getValidity()).andReturn(this.validity).times(2);
        expect(this.serializer.getKey()).andReturn("key");
        expect(this.serializer.getValidity()).andReturn(this.validity);
        expect(this.cache.get(isA(PipelineCacheKey.class))).andReturn(this.cachedResponse);
        expect(this.cachedResponse.getValidityObjects()).andReturn(
                new SourceValidity[] { this.validity, this.validity, this.validity });
        expect(this.validity.isValid()).andReturn(SourceValidity.VALID).times(3);
        expect(this.cachedResponse.getResponse()).andReturn(getCompiledBytes("<foo/>"));
        this.xmlConsumer.setDocumentLocator(isA(Locator.class));
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startElement(eq(""), eq("foo"), eq("foo"), isA(Attributes.class));
        this.xmlConsumer.endElement("", "foo", "foo");
        this.xmlConsumer.endDocument();
        replay(this.environment, this.generator, this.transformer, this.serializer, this.cache, this.cachedResponse, this.validity,
                this.outputStream, this.xmlConsumer);
        this.pipeline.prepareInternal(this.environment);
        this.pipeline.getValidityForEventPipeline();
        this.pipeline.getKeyForEventPipeline();
        this.pipeline.process(this.environment, this.xmlConsumer);
        verify(this.environment, this.generator, this.transformer, this.serializer, this.cache, this.cachedResponse, this.validity,
                this.outputStream, this.xmlConsumer);
    }

    @Test
    public void testProcessEnvironmentXMLConsumerCachedUnknownValidity() throws ProcessingException, SAXException, IOException {
        doInvokeContextStuff();
        doProcessingNodeStuff();
        expect(this.environment.getObjectModel()).andReturn(null);
        this.generator.setup(this.sourceResolver, null, null, null);
        this.transformer.setup(this.sourceResolver, null, null, null);
        expectLastCall().times(2);
        expect(this.generator.getKey()).andReturn("key");
        expect(this.generator.getValidity()).andReturn(this.validity);
        expect(this.transformer.getKey()).andReturn("key").times(2);
        expect(this.transformer.getValidity()).andReturn(this.validity).times(2);
        expect(this.serializer.getKey()).andReturn("key");
        expect(this.serializer.getValidity()).andReturn(this.validity);
        expect(this.cache.get(isA(PipelineCacheKey.class))).andReturn(this.cachedResponse);
        expect(this.cachedResponse.getValidityObjects()).andReturn(
                new SourceValidity[] { this.validity, this.validity, this.validity });
        expect(this.validity.isValid()).andReturn(SourceValidity.UNKNOWN);
        expect(this.validity.isValid(this.validity)).andReturn(SourceValidity.VALID);
        expect(this.validity.isValid()).andReturn(SourceValidity.VALID).times(2);
        expect(this.cachedResponse.getResponse()).andReturn(getCompiledBytes("<foo/>"));
        this.xmlConsumer.setDocumentLocator(isA(Locator.class));
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startElement(eq(""), eq("foo"), eq("foo"), isA(Attributes.class));
        this.xmlConsumer.endElement("", "foo", "foo");
        this.xmlConsumer.endDocument();
        replay(this.environment, this.generator, this.transformer, this.serializer, this.cache, this.cachedResponse, this.validity,
                this.outputStream, this.xmlConsumer);
        this.pipeline.prepareInternal(this.environment);
        this.pipeline.getValidityForEventPipeline();
        this.pipeline.getKeyForEventPipeline();
        this.pipeline.process(this.environment, this.xmlConsumer);
        verify(this.environment, this.generator, this.transformer, this.serializer, this.cache, this.cachedResponse, this.validity,
                this.outputStream, this.xmlConsumer);
    }

    @Test
    public void testProcessEnvironmentXMLConsumerNotCached() throws ProcessingException, SAXException, IOException {
        doInvokeContextStuff();
        doProcessingNodeStuff();
        expect(this.environment.getObjectModel()).andReturn(null);
        this.generator.setup(this.sourceResolver, null, null, null);
        this.transformer.setup(this.sourceResolver, null, null, null);
        expectLastCall().times(2);
        expect(this.generator.getKey()).andReturn("key");
        expect(this.generator.getValidity()).andReturn(this.validity);
        expect(this.transformer.getKey()).andReturn("key").times(2);
        expect(this.transformer.getValidity()).andReturn(this.validity).times(2);
        expect(this.serializer.getKey()).andReturn("key");
        expect(this.serializer.getValidity()).andReturn(this.validity);
        expect(this.cache.get(isA(PipelineCacheKey.class))).andReturn(null);
        this.generator.setConsumer(this.transformer);
        this.transformer.setConsumer(this.transformer);
        this.transformer.setConsumer(isA(XMLTeePipe.class));
        this.generator.generate();
        this.cache.store(isA(PipelineCacheKey.class), isA(CachedResponse.class));
        replay(this.environment, this.generator, this.transformer, this.serializer, this.cache, this.validity);
        this.pipeline.prepareInternal(this.environment);
        this.pipeline.getValidityForEventPipeline();
        this.pipeline.getKeyForEventPipeline();
        this.pipeline.process(this.environment, null);
        verify(this.environment, this.generator, this.transformer, this.serializer, this.cache, this.validity);
    }

    private void doInvokeContextStuff() {
        // InvokeContext does these three things, called in GenerateNode:
        this.pipeline.setProcessorManager(null);
        this.pipeline.setup(this.parameters);
        this.pipeline.setErrorHandler(null);
    }

    private void doProcessingNodeStuff() {
        // This gets done in the various processing nodes:
        expect(this.beanFactory.getBean("org.apache.cocoon.generation.Generator/type")).andReturn(this.generator);
        expect(this.beanFactory.getBean("org.apache.cocoon.transformation.Transformer/type")).andReturn(this.transformer).times(2);
        expect(this.beanFactory.getBean("org.apache.cocoon.serialization.Serializer/type")).andReturn(this.serializer);
        replay(this.beanFactory, this.generator, this.transformer, this.serializer);
        this.pipeline.setGenerator("type", null, null, null);
        this.pipeline.addTransformer("type", null, null, null);
        this.pipeline.addTransformer("type", null, null, null);
        this.pipeline.setSerializer("type", null, null, null, null);
        verify(this.beanFactory, this.generator, this.transformer, this.serializer);
        reset(this.beanFactory, this.generator, this.transformer, this.serializer);
    }

    private byte[] getCompiledBytes(final String xml) throws IOException, SAXException {
        XMLByteStreamCompiler compiler = new XMLByteStreamCompiler();
        XMLReader reader = XMLReaderFactory.createXMLReader();
        reader.setContentHandler(compiler);
        reader.parse(new InputSource(new ByteArrayInputStream(xml.getBytes())));
        return (byte[]) compiler.getSAXFragment();
    }
}
