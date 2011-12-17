package edu.stanford.irt.laneweb.cocoon.pipeline;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Iterator;
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
import org.apache.cocoon.generation.Generator;
import org.apache.cocoon.serialization.Serializer;
import org.apache.cocoon.transformation.Transformer;
import org.apache.cocoon.util.HashUtil;
import org.apache.cocoon.xml.XMLConsumer;
import org.apache.cocoon.xml.XMLProducer;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.AggregatedValidity;
import org.apache.excalibur.source.impl.validity.NOPValidity;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.LanewebException;

public class CachingPipeline extends NonCachingPipeline {

    public static final String PIPELOCK_PREFIX = "PIPELOCK:";

    /** This is the Cache holding cached responses */
    private Cache cache;

    /** Cache complete response */
    private boolean cacheCompleteResponse;

    /** The cached response */
    private CachedResponse cachedResponse;

    /** Complete response is cached */
    private boolean completeResponseIsCached;

    /** The index indicating to the first transformer which is not cacheable */
    private int firstNotCacheableTransformerIndex;

    /** The index indicating the first transformer getting input from the cache */
    private int firstProcessedTransformerIndex;

    /** This key indicates the response that is fetched from the cache */
    private PipelineCacheKey fromCacheKey;

    /** The role name of the generator */
    private String generatorRole;

    /** The role name of the serializer */
    private String serializerRole;

    /** This key indicates the response that will get into the cache */
    private PipelineCacheKey toCacheKey;

    /** The source validities used for caching */
    private SourceValidity[] toCacheSourceValidities;

    /** The role names of the transfomrers */
    private List<String> transformerRoles = new LinkedList<String>();

//    /** Store for pipeline locks (optional) */
//    private Store transientStore;

    /** The deserializer */
    private XMLByteStreamInterpreter xmlDeserializer;

    /** The serializer */
    private XMLByteStreamCompiler xmlSerializer;

    public CachingPipeline(final SourceResolver sourceResolver, final Cache cache) {
        super(sourceResolver);
        this.cache = cache;
//        this.transientStore = transientStore;
    }

    /**
     * Add a transformer.
     */
    @Override
    public void addTransformer(final String role, final String source, final Parameters param, final Parameters hintParam) {
        super.addTransformer(role, source, param, hintParam);
        this.transformerRoles.add(role);
    }

    @Override
    public String getKeyForEventPipeline() {
        List<Transformer> transformers = getTransformers();
        if (null != this.toCacheKey && !this.cacheCompleteResponse
                && this.firstNotCacheableTransformerIndex == transformers.size()) {
            return String.valueOf(HashUtil.hash(this.toCacheKey.toString()));
        }
        if (null != this.fromCacheKey && !this.completeResponseIsCached
                && this.firstProcessedTransformerIndex == transformers.size()) {
            return String.valueOf(HashUtil.hash(this.fromCacheKey.toString()));
        }
        return null;
    }

    /**
     * Return valid validity objects for the event pipeline. If the event
     * pipeline (the complete pipeline without the serializer) is cacheable and
     * valid, return all validity objects. Otherwise, return <code>null</code>.
     */
    @Override
    public SourceValidity getValidityForEventPipeline() {
        List<Transformer> transformers = getTransformers();
        if (this.cachedResponse != null) {
            if (!this.cacheCompleteResponse && this.firstNotCacheableTransformerIndex < transformers.size()) {
                // Cache contains only partial pipeline.
                return null;
            }
            if (this.toCacheSourceValidities != null) {
                // This means that the pipeline is valid based on the validities
                // of the individual components
                final AggregatedValidity validity = new AggregatedValidity();
                for (SourceValidity toCacheSourceValiditie : this.toCacheSourceValidities) {
                    validity.add(toCacheSourceValiditie);
                }
                return validity;
            }
            // This means that the pipeline is valid because it has not yet
            // expired
            return NOPValidity.SHARED_INSTANCE;
        } else {
            int vals = 0;
            if (null != this.toCacheKey && !this.cacheCompleteResponse
                    && this.firstNotCacheableTransformerIndex == transformers.size()) {
                vals = this.toCacheKey.size();
            } else if (null != this.fromCacheKey && !this.completeResponseIsCached
                    && this.firstProcessedTransformerIndex == transformers.size()) {
                vals = this.fromCacheKey.size();
            }
            if (vals > 0) {
                final AggregatedValidity validity = new AggregatedValidity();
                for (int i = 0; i < vals; i++) {
                    validity.add(getValidityForInternalPipeline(i));
                }
                return validity;
            }
            return null;
        }
    }

    /**
     * Set the generator.
     */
    @Override
    public void setGenerator(final String role, final String source, final Parameters param, final Parameters hintParam) {
        super.setGenerator(role, source, param, hintParam);
        this.generatorRole = role;
    }

    /**
     * Set the serializer.
     */
    @Override
    public void setSerializer(final String role, final String source, final Parameters param, final Parameters hintParam,
            final String mimeType) {
        super.setSerializer(role, source, param, hintParam, mimeType);
        this.serializerRole = role;
    }

    /**
     * Cache longest cacheable key
     * @throws ProcessingException 
     */
    protected void cacheResults(final Environment environment, final OutputStream os) throws ProcessingException {
        if (this.toCacheKey != null) {
            CachedResponse response;
            if (this.cacheCompleteResponse) {
                response = new CachedResponse(this.toCacheSourceValidities, ((CachingOutputStream) os).getContent());
//                response.setContentType(environment.getContentType());
            } else {
                if (this.xmlSerializer == null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("\ncacheCompleteResponse:").append(this.cacheCompleteResponse)
                    .append("\ncachedResponse:").append(this.cachedResponse)
                    .append("\ncompleteResponseIsCached:").append(this.completeResponseIsCached)
                    .append("\nfirstNotCacheableTransformerIndex:").append(this.firstNotCacheableTransformerIndex)
                    .append("\nfirstProcessedTransformerIndex:").append(this.firstProcessedTransformerIndex)
                    .append("\nfromCacheKey:").append(this.fromCacheKey)
                    .append("\ntoCacheKey:").append(this.toCacheKey);
                    throw new IllegalStateException(sb.toString());
                }
                response = new CachedResponse(this.toCacheSourceValidities, (byte[]) this.xmlSerializer.getSAXFragment());
            }
            this.cache.store(this.toCacheKey, response);
        }
    }

    /**
     * Connect the pipeline.
     */
    protected void connectCachingPipeline(final Environment environment) throws ProcessingException {
        XMLByteStreamCompiler localXMLSerializer = null;
        if (!this.cacheCompleteResponse) {
            this.xmlSerializer = new XMLByteStreamCompiler();
            localXMLSerializer = this.xmlSerializer;
        }
        XMLConsumer lastConsumer = getLastConsumer();
        List<Transformer> transformers = getTransformers();
        if (this.cachedResponse == null) {
            XMLProducer prev = getGenerator();
            XMLConsumer next;
            int cacheableTransformerCount = this.firstNotCacheableTransformerIndex;
            Iterator<Transformer> itt = transformers.iterator();
            while (itt.hasNext()) {
                next = itt.next();
                if (localXMLSerializer != null) {
                    if (cacheableTransformerCount == 0) {
                        next = new XMLTeePipe(next, localXMLSerializer);
                        localXMLSerializer = null;
                    } else {
                        cacheableTransformerCount--;
                    }
                }
                connect(environment, prev, next);
                prev = (XMLProducer) next;
            }
            next = lastConsumer;
            if (localXMLSerializer != null) {
                next = new XMLTeePipe(next, localXMLSerializer);
                localXMLSerializer = null;
            }
            connect(environment, prev, next);
        } else {
            this.xmlDeserializer = new XMLByteStreamInterpreter();
            // connect the pipeline:
            XMLProducer prev = this.xmlDeserializer;
            XMLConsumer next;
            int cacheableTransformerCount = 0;
            Iterator<Transformer> itt = transformers.iterator();
            while (itt.hasNext()) {
                next = itt.next();
                if (cacheableTransformerCount >= this.firstProcessedTransformerIndex) {
                    if (localXMLSerializer != null && cacheableTransformerCount == this.firstNotCacheableTransformerIndex) {
                        next = new XMLTeePipe(next, localXMLSerializer);
                        localXMLSerializer = null;
                    }
                    connect(environment, prev, next);
                    prev = (XMLProducer) next;
                }
                cacheableTransformerCount++;
            }
            next = lastConsumer;
            if (localXMLSerializer != null) {
                next = new XMLTeePipe(next, localXMLSerializer);
                localXMLSerializer = null;
            }
            connect(environment, prev, next);
        }
    }

    /**
     * Connect the pipeline.
     */
    @Override
    protected void connectPipeline(final Environment environment) throws ProcessingException {
        if (this.toCacheKey == null && this.cachedResponse == null) {
            super.connectPipeline(environment);
        } else if (!this.completeResponseIsCached) {
            connectCachingPipeline(environment);
        }
    }

    /**
     * The components of the pipeline are checked if they are Cacheable.
     */
    protected void generateCachingKey(final Environment environment) throws ProcessingException {
        this.toCacheKey = null;
        this.firstNotCacheableTransformerIndex = 0;
        this.cacheCompleteResponse = false;
        // first step is to generate the key:
        // All pipeline components starting with the generator
        // are tested if they are either a CacheableProcessingComponent
        // or Cacheable (deprecated). The returned keys are chained together
        // to build a unique key of the request
        // is the generator cacheable?
        Serializable key = null;
        if (getGenerator() instanceof CacheableProcessingComponent) {
            key = ((CacheableProcessingComponent) getGenerator()).getKey();
        }
        List<Transformer> transformers = getTransformers();
        if (key != null) {
            this.toCacheKey = new PipelineCacheKey();
            this.toCacheKey.addKey(newComponentCacheKey(ComponentCacheKey.ComponentType_Generator, this.generatorRole, key));
            // now testing transformers
            final int transformerSize = transformers.size();
            boolean continueTest = true;
            while (this.firstNotCacheableTransformerIndex < transformerSize && continueTest) {
                final Transformer trans = transformers.get(this.firstNotCacheableTransformerIndex);
                key = null;
                if (trans instanceof CacheableProcessingComponent) {
                    key = ((CacheableProcessingComponent) trans).getKey();
                }
                if (key != null) {
                    this.toCacheKey.addKey(newComponentCacheKey(ComponentCacheKey.ComponentType_Transformer,
                            this.transformerRoles.get(this.firstNotCacheableTransformerIndex), key));
                    this.firstNotCacheableTransformerIndex++;
                } else {
                    continueTest = false;
                }
            }
            // all transformers are cacheable => pipeline is cacheable
            // test serializer if this is not an internal request
            Serializer serializer = getSerializer();
            if (this.firstNotCacheableTransformerIndex == transformerSize && serializer == getLastConsumer()) {
                key = null;
                if (serializer instanceof CacheableProcessingComponent) {
                    key = ((CacheableProcessingComponent) serializer).getKey();
                }
                if (key != null) {
                    this.toCacheKey.addKey(newComponentCacheKey(ComponentCacheKey.ComponentType_Serializer, this.serializerRole,
                            key));
                    this.cacheCompleteResponse = true;
                }
            }
        }
    }

//    /**
//     * Makes the lock (instantiates a new object and puts it into the store)
//     */
//    protected void generateLock(final Object key) {
//        if (this.transientStore != null && key != null) {
//            final String lockKey = PIPELOCK_PREFIX + key;
//            synchronized (this.transientStore) {
//                if (!this.transientStore.containsKey(lockKey)) {
//                    Object lock = RequestContextHolder.getRequestAttributes();
//                    try {
//                        this.transientStore.store(lockKey, lock);
//                    } catch (IOException e) {
//                        throw new LanewebException(e);
//                    }
//                }
//            }
//        }
//    }

    /**
     * Create a new cache key
     */
    protected ComponentCacheKey newComponentCacheKey(final int type, final String role, final Serializable key) {
        return new ComponentCacheKey(type, role, key);
    }

    /**
     * Process the given <code>Environment</code>, producing the output.
     */
    @Override
    protected boolean processXMLPipeline(final Environment environment) throws ProcessingException {
        if (this.toCacheKey == null && this.cachedResponse == null) {
            return super.processXMLPipeline(environment);
        }
        if (this.cachedResponse != null && this.completeResponseIsCached) {
            // Write response out
            try {
                final OutputStream outputStream = environment.getOutputStream(0);
                final byte[] content = this.cachedResponse.getResponse();
                if (content.length > 0) {
                    outputStream.write(content);
                }
            } catch (IOException e) {
            	throw new LanewebException(e);
			}
        } else {
//            generateLock(this.toCacheKey);
            try {
                OutputStream os = null;
                int outputBufferSize = getOutputBufferSize();
                if (this.cacheCompleteResponse && this.toCacheKey != null) {
                    os = new CachingOutputStream(environment.getOutputStream(outputBufferSize));
                }
                Generator generator = getGenerator();
                Serializer serializer = getSerializer();
                if (serializer != getLastConsumer()) {
                    if (os == null) {
                        os = environment.getOutputStream(outputBufferSize);
                    }
                    // internal processing
                    if (this.xmlDeserializer != null) {
                        this.xmlDeserializer.deserialize(this.cachedResponse.getResponse());
                    } else {
                        generator.generate();
                    }
                } else {
                    if (serializer.shouldSetContentLength()) {
                        if (os == null) {
                            os = environment.getOutputStream(0);
                        }
                        // Set the output stream
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        serializer.setOutputStream(baos);
                        // Execute the pipeline
                        if (this.xmlDeserializer != null) {
                            this.xmlDeserializer.deserialize(this.cachedResponse.getResponse());
                        } else {
                            generator.generate();
                        }
                        baos.writeTo(os);
                    } else {
                        if (os == null) {
                            os = environment.getOutputStream(outputBufferSize);
                        }
                        // Set the output stream
                        serializer.setOutputStream(os);
                        // Execute the pipeline
                        if (this.xmlDeserializer != null) {
                            this.xmlDeserializer.deserialize(this.cachedResponse.getResponse());
                        } else {
                            generator.generate();
                        }
                    }
                }
                //
                // Now that we have processed the pipeline,
                // we do the actual caching
                //
                cacheResults(environment, os);
            } catch (IOException e) {
            	throw new LanewebException(e);
			} catch (SAXException e) {
            	throw new LanewebException(e);
//			} finally {
//                releaseLock(this.toCacheKey);
            }
            return true;
        }
        return true;
    }

//    /**
//     * Releases the lock (notifies it and removes it from the store)
//     */
//    protected void releaseLock(final Object key) {
//        if (this.transientStore != null && key != null) {
//            final String lockKey = PIPELOCK_PREFIX + key;
//            Object lock = null;
//            synchronized (this.transientStore) {
//                if (this.transientStore.containsKey(lockKey)) {
//                    lock = this.transientStore.get(lockKey);
//                    this.transientStore.remove(lockKey);
//                }
//            }
//            if (lock != null) {
//                // Notify everybody who's waiting
//                synchronized (lock) {
//                    lock.notifyAll();
//                }
//            }
//        }
//    }

    /**
     * Setup the evenet pipeline. The components of the pipeline are checked if
     * they are Cacheable.
     */
    @Override
    protected void setupPipeline(final Environment environment) throws ProcessingException {
        super.setupPipeline(environment);
        // Generate the key to fill the cache
        generateCachingKey(environment);
        // Test the cache for a valid response
        if (this.toCacheKey != null) {
            validatePipeline(environment);
        }
        setupValidities();
    }

    /**
     * Generate validity objects for the new response
     */
    protected void setupValidities() throws ProcessingException {
        if (this.toCacheKey != null) {
            // only update validity objects if we cannot use
            // a cached response or when the cached response does
            // cache less than now is cacheable
            if (this.fromCacheKey == null || this.fromCacheKey.size() < this.toCacheKey.size()) {
                this.toCacheSourceValidities = new SourceValidity[this.toCacheKey.size()];
                int len = this.toCacheSourceValidities.length;
                int i = 0;
                while (i < len) {
                    final SourceValidity validity = getValidityForInternalPipeline(i);
                    if (validity == null) {
                        if (i > 0 && (this.fromCacheKey == null || i > this.fromCacheKey.size())) {
                            // shorten key
                            for (int m = i; m < this.toCacheSourceValidities.length; m++) {
                                this.toCacheKey.removeLastKey();
                                if (!this.cacheCompleteResponse) {
                                    this.firstNotCacheableTransformerIndex--;
                                }
                                this.cacheCompleteResponse = false;
                            }
                            SourceValidity[] copy = new SourceValidity[i];
                            System.arraycopy(this.toCacheSourceValidities, 0, copy, 0, copy.length);
                            this.toCacheSourceValidities = copy;
                            len = this.toCacheSourceValidities.length;
                        } else {
                            // caching is not possible!
                            this.toCacheKey = null;
                            this.toCacheSourceValidities = null;
                            this.cacheCompleteResponse = false;
                            len = 0;
                        }
                    } else {
                        this.toCacheSourceValidities[i] = validity;
                    }
                    i++;
                }
            } else {
                // we don't have to cache
                this.toCacheKey = null;
                this.cacheCompleteResponse = false;
            }
        }
    }

    /**
     * Calculate the key that can be used to get something from the cache, and
     * handle expires properly.
     */
    protected void validatePipeline(final Environment environment) throws ProcessingException {
        this.completeResponseIsCached = this.cacheCompleteResponse;
        this.fromCacheKey = this.toCacheKey.copy();
        this.firstProcessedTransformerIndex = this.firstNotCacheableTransformerIndex;
        boolean finished = false;
        long expires = getExpires();
        while (this.fromCacheKey != null && !finished) {
            finished = true;
            final CachedResponse response = this.cache.get(this.fromCacheKey);
            // now test validity
            if (response != null) {
                boolean responseIsValid = true;
                boolean responseIsUsable = true;
                // See if we have an explicit "expires" setting. If so,
                // and if it's still fresh, we're done.
                Long responseExpires = response.getExpires();
                if (responseExpires != null) {
                    if (responseExpires.longValue() > System.currentTimeMillis()) {
                        this.cachedResponse = response;
                        return;
                    } else {
                        // If an expires parameter was provided, use it. If this
                        // parameter is not available
                        // it means that the sitemap was modified, and the old
                        // expires value is not valid
                        // anymore.
                        if (expires != 0) {
                            response.setExpires(Long.valueOf(expires + System.currentTimeMillis()));
                        } else {
                            response.setExpires(null);
                        }
                    }
                } else {
                    // The response had no expires informations. See if it needs
                    // to be set (i.e. because the configuration has changed)
                    if (expires != 0) {
                        response.setExpires(Long.valueOf(expires + System.currentTimeMillis()));
                    }
                }
                SourceValidity[] fromCacheValidityObjects = response.getValidityObjects();
                int i = 0;
                while (responseIsValid && i < fromCacheValidityObjects.length) {
                    // BH Check if validities[i] is null, may happen
                    // if exception was thrown due to malformed content
                    SourceValidity validity = fromCacheValidityObjects[i];
                    int valid = validity == null ? SourceValidity.INVALID : validity.isValid();
                    if (valid == SourceValidity.UNKNOWN) {
                        // Don't know if valid, make second test
                        validity = getValidityForInternalPipeline(i);
                        if (validity != null) {
                            valid = fromCacheValidityObjects[i].isValid(validity);
                            if (valid == SourceValidity.UNKNOWN) {
                                validity = null;
                            }
                        }
                    }
                    if (valid != SourceValidity.VALID) {
                        responseIsValid = false;
                        // update validity
                        if (validity == null) {
                            responseIsUsable = false;
                        }
                    } else {
                        i++;
                    }
                }
                if (responseIsValid) {
                    // we are valid, ok that's it
                    this.cachedResponse = response;
                    this.toCacheSourceValidities = fromCacheValidityObjects;
                } else {
                    // we are not valid!
                    if (!responseIsUsable) {
                        // we could not compare, because we got no
                        // validity object, so shorten pipeline key
                        if (i > 0) {
                            int deleteCount = fromCacheValidityObjects.length - i;
                            if (i > 0 && i <= this.firstNotCacheableTransformerIndex + 1) {
                                this.firstNotCacheableTransformerIndex = i - 1;
                            }
                            for (int x = 0; x < deleteCount; x++) {
                                this.toCacheKey.removeLastKey();
                            }
                        } else {
                            this.toCacheKey = null;
                        }
                        this.cacheCompleteResponse = false;
                    } else {
                        // the entry is invalid, remove it
                        this.cache.remove(this.fromCacheKey);
                    }
                    // try a shorter key
                    if (i > 0) {
                        this.fromCacheKey.removeLastKey();
                        if (!this.completeResponseIsCached) {
                            this.firstProcessedTransformerIndex--;
                        }
                    } else {
                        this.fromCacheKey = null;
                    }
                    finished = false;
                    this.completeResponseIsCached = false;
                }
            } else {
                // check if there might be one being generated
//                if (!waitForLock(this.fromCacheKey)) {
//                    finished = false;
//                    continue;
//                }
                // no cached response found
                finished = setupFromCacheKey();
                this.completeResponseIsCached = false;
            }
        }
    }

    /**
     * Look up the lock object by key, and if present, wait till notified.
     * 
     * @return false if able to find a lock and was notified
     */
//    protected boolean waitForLock(final Object key) {
//        if (this.transientStore != null) {
//            final String lockKey = PIPELOCK_PREFIX + key;
//            // Get a lock object from the store
//            Object lock;
//            synchronized (this.transientStore) {
//                lock = this.transientStore.get(lockKey);
//            }
//            // Avoid deadlock with self (see JIRA COCOON-1985).
//            Object current = RequestContextHolder.getRequestAttributes();
//            if (lock != null && lock != current) {
//                try {
//                    synchronized (lock) {
//                        lock.wait();
//                    }
//                } catch (InterruptedException e) {
//                    /* ignored */
//                }
//                return false;
//            }
//        }
//        return true;
//    }

    SourceValidity getValidityForInternalPipeline(final int index) {
        final SourceValidity validity;
        if (index == 0) {
            // test generator
            validity = ((CacheableProcessingComponent) getGenerator()).getValidity();
        } else if (index <= this.firstNotCacheableTransformerIndex) {
            // test transformer
            final Transformer trans = getTransformers().get(index - 1);
            validity = ((CacheableProcessingComponent) trans).getValidity();
        } else {
            // test serializer
            validity = ((CacheableProcessingComponent) getSerializer()).getValidity();
        }
        return validity;
    }

    boolean setupFromCacheKey() {
        // stop on longest key for smart caching
        this.fromCacheKey = null;
        return true;
    }
}
