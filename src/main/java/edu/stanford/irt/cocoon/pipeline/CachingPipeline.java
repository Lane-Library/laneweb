package edu.stanford.irt.cocoon.pipeline;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.caching.Cache;
import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.cocoon.caching.CachedResponse;
import org.apache.cocoon.caching.CachingOutputStream;
import org.apache.cocoon.caching.ComponentCacheKey;
import org.apache.cocoon.caching.PipelineCacheKey;
import org.apache.cocoon.components.sax.XMLByteStreamCompiler;
import org.apache.cocoon.components.sax.XMLByteStreamInterpreter;
import org.apache.cocoon.components.sax.XMLTeePipe;
import org.apache.cocoon.environment.Environment;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.transformation.Transformer;
import org.apache.cocoon.util.HashUtil;
import org.apache.cocoon.xml.XMLConsumer;
import org.apache.cocoon.xml.XMLProducer;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.AggregatedValidity;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.LanewebException;

public class CachingPipeline extends NonCachingPipeline {

    /** This is the Cache holding cached responses */
    private Cache cache;

    /** The source validities used for caching */
    private List<SourceValidity> cachedValidities;

    /** This key indicates the response that will get into the cache */
    private PipelineCacheKey cacheKey;

    private List<CacheableProcessingComponent> components;

    private List<String> componentTypes;

    private List<SourceValidity> eventCachedValidities;

    private PipelineCacheKey eventCacheKey;

    private boolean isEventPipeline;

    public CachingPipeline(final SourceResolver sourceResolver, final Cache cache) {
        super(sourceResolver);
        this.cache = cache;
        this.cacheKey = new PipelineCacheKey();
        this.eventCacheKey = new PipelineCacheKey();
        this.cachedValidities = new LinkedList<SourceValidity>();
        this.eventCachedValidities = new LinkedList<SourceValidity>();
        this.components = new LinkedList<CacheableProcessingComponent>();
        this.componentTypes = new LinkedList<String>();
    }

    /**
     * Add a transformer.
     */
    @Override
    public void addTransformer(final String type, final String source, final Parameters param, final Parameters hintParam) {
        super.addTransformer(type, source, param, hintParam);
        List<Transformer> transformers = getTransformers();
        this.components.add((CacheableProcessingComponent) transformers.get(transformers.size() - 1));
        this.componentTypes.add(type);
    }

    @Override
    public String getKeyForEventPipeline() {
        return Long.toString(HashUtil.hash(this.cacheKey.toString()));
    }

    /**
     * Return valid validity objects for the event pipeline. If the event
     * pipeline (the complete pipeline without the serializer) is cacheable and
     * valid, return all validity objects. Otherwise, return <code>null</code>.
     */
    @Override
    public SourceValidity getValidityForEventPipeline() {
        AggregatedValidity validity = new AggregatedValidity();
        for (SourceValidity sourceValidity : this.cachedValidities) {
            validity.add(sourceValidity);
        }
        return validity;
    }

    @Override
    public boolean process(final Environment environment, final XMLConsumer consumer) throws ProcessingException {
        this.isEventPipeline = true;
        return super.process(environment, consumer);
    }

    /**
     * Set the generator.
     */
    @Override
    public void setGenerator(final String type, final String source, final Parameters param, final Parameters hintParam) {
        super.setGenerator(type, source, param, hintParam);
        this.components.add((CacheableProcessingComponent) getGenerator());
        this.componentTypes.add(type);
    }

    /**
     * Set the serializer.
     */
    @Override
    public void setSerializer(final String type, final String source, final Parameters param, final Parameters hintParam,
            final String mimeType) {
        super.setSerializer(type, source, param, hintParam, mimeType);
        this.components.add((CacheableProcessingComponent) getSerializer());
        this.componentTypes.add(type);
    }

    /**
     * Connect the pipeline.
     */
    @Override
    protected void connectPipeline(final Environment environment) throws ProcessingException {
    }

    /**
     * Process the given <code>Environment</code>, producing the output.
     * 
     * @throws
     */
    @Override
    protected boolean processXMLPipeline(final Environment environment) throws ProcessingException {
        try {
            if (this.isEventPipeline) {
                CachedResponse response = getValidCachedResponse(this.eventCacheKey, this.eventCachedValidities);
                if (response == null) {
                    XMLByteStreamCompiler xmlSerializer = new XMLByteStreamCompiler();
                    XMLProducer prev = getGenerator();
                    XMLConsumer next;
                    List<Transformer> transformers = getTransformers();
                    for (Transformer transformer : transformers) {
                        next = transformer;
                        connect(environment, prev, next);
                        prev = (Transformer) next;
                    }
                    next = new XMLTeePipe(getLastConsumer(), xmlSerializer);
                    connect(environment, prev, next);
                    getGenerator().generate();
                    this.cache.store(
                            this.eventCacheKey,
                            new CachedResponse(this.eventCachedValidities.toArray(new SourceValidity[this.eventCachedValidities
                                    .size()]), (byte[]) xmlSerializer.getSAXFragment()));
                } else {
                    XMLByteStreamInterpreter xmlDeserializer = new XMLByteStreamInterpreter();
                    connect(environment, xmlDeserializer, getLastConsumer());
                    xmlDeserializer.deserialize(response.getResponse());
                }
            } else {
                // CachedResponse response = this.cache.get(this.cacheKey);
                CachedResponse response = getValidCachedResponse(this.cacheKey, this.cachedValidities);
                if (response == null) {
                    CachingOutputStream cachingStream = new CachingOutputStream(environment.getOutputStream(0));
                    super.connectPipeline(environment);
                    getSerializer().setOutputStream(cachingStream);
                    getGenerator().generate();
                    this.cache.store(this.cacheKey,
                            new CachedResponse(this.cachedValidities.toArray(new SourceValidity[this.cachedValidities.size()]),
                                    cachingStream.getContent()));
                } else {
                    environment.getOutputStream(0).write(response.getResponse());
                }
            }
            return true;
        } catch (IOException e) {
            throw new LanewebException(e);
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }

    @Override
    protected void setupPipeline(final Environment environment) throws ProcessingException {
        super.setupPipeline(environment);
        int last = this.components.size() - 1;
        int type = ComponentCacheKey.ComponentType_Generator;
        for (int i = 0; i <= last; i++) {
            addCacheableComponent(this.components.get(i), this.componentTypes.get(i), type);
            type = i == last - 1 ? ComponentCacheKey.ComponentType_Serializer : ComponentCacheKey.ComponentType_Transformer;
        }
    }

    private void addCacheableComponent(final CacheableProcessingComponent component, final String type, final int keyType) {
        ComponentCacheKey key = new ComponentCacheKey(keyType, type, component.getKey());
        SourceValidity validity = component.getValidity();
        this.cacheKey.addKey(key);
        this.cachedValidities.add(validity);
        if (keyType != ComponentCacheKey.ComponentType_Serializer) {
            this.eventCacheKey.addKey(key);
            this.eventCachedValidities.add(validity);
        }
    }

    // TODO: also check expires value from cache
    private CachedResponse getValidCachedResponse(final PipelineCacheKey key, final List<SourceValidity> validities) {
        CachedResponse response = this.cache.get(key);
        if (response != null) {
            SourceValidity[] cachedValidities = response.getValidityObjects();
            if (cachedValidities == null || cachedValidities.length != validities.size()) {
                response = null;
            } else {
                for (int i = 0; i < cachedValidities.length; i++) {
                    SourceValidity sourceValidity = cachedValidities[i];
                    int validity = sourceValidity == null ? SourceValidity.INVALID : sourceValidity.isValid();
                    if (validity == SourceValidity.INVALID) {
                        response = null;
                        break;
                    } else if (validity == SourceValidity.UNKNOWN) {
                        validity = sourceValidity.isValid(validities.get(i));
                        if (validity != SourceValidity.VALID) {
                            response = null;
                            break;
                        }
                    }
                }
            }
            // TODO: check if I really need to to this:
            if (response == null) {
                this.cache.remove(key);
            }
        }
        return response;
    }
}
