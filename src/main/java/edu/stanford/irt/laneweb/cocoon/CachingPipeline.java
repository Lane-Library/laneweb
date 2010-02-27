package edu.stanford.irt.laneweb.cocoon;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.avalon.framework.parameters.ParameterException;
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
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.transformation.Transformer;
import org.apache.cocoon.util.HashUtil;
import org.apache.cocoon.xml.XMLConsumer;
import org.apache.cocoon.xml.XMLProducer;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.AggregatedValidity;
import org.apache.excalibur.source.impl.validity.DeferredValidity;
import org.apache.excalibur.source.impl.validity.NOPValidity;
import org.apache.excalibur.store.Store;
import org.springframework.web.context.request.RequestContextHolder;
import org.xml.sax.SAXException;


public class CachingPipeline extends NoncachingPipeline {

    /** This is the Cache holding cached responses */
    protected Cache cache;

    /** The deserializer */
    protected XMLByteStreamInterpreter xmlDeserializer;

    /** The serializer */
    protected XMLByteStreamCompiler xmlSerializer;
    
    public void setCache(Cache cache) {
        this.cache = cache;
    }

    public static final String PIPELOCK_PREFIX = "PIPELOCK:";

    /** The role name of the generator */
    protected String generatorRole;

    /** The role names of the transfomrers */
    protected List transformerRoles = new ArrayList();

    /** The role name of the serializer */
    protected String serializerRole;

    /** The role name of the reader */
    protected String readerRole;

    /** The cached response */
    protected CachedResponse cachedResponse;

    /** The index indicating the first transformer getting input from the cache */
    protected int firstProcessedTransformerIndex;

    /** Complete response is cached */
    protected boolean completeResponseIsCached;


    /** This key indicates the response that is fetched from the cache */
    protected PipelineCacheKey fromCacheKey;

    /** This key indicates the response that will get into the cache */
    protected PipelineCacheKey toCacheKey;

    /** The source validities used for caching */
    protected SourceValidity[] toCacheSourceValidities;

    /** The index indicating to the first transformer which is not cacheable */
    protected int firstNotCacheableTransformerIndex;

    /** Cache complete response */
    protected boolean cacheCompleteResponse;

    /** Store for pipeline locks (optional) */
    protected Store transientStore;


    /**
     * Parameterizable Interface - Configuration
     */
    public void parameterize(Parameters params) throws ParameterException {
        super.parameterize(params);
    }

    public void setTransientStore(Store store) {
        this.transientStore = store;
    }
    /**
     * Set the generator.
     */
    public void setGenerator(String role, String source, Parameters param,
                             Parameters hintParam) throws ProcessingException {
        super.setGenerator(role, source, param, hintParam);
        this.generatorRole = role;
    }

    /**
     * Add a transformer.
     */
    public void addTransformer(String role, String source, Parameters param,
                               Parameters hintParam) throws ProcessingException {
        super.addTransformer(role, source, param, hintParam);
        this.transformerRoles.add(role);
    }

    /**
     * Set the serializer.
     */
    public void setSerializer(String role, String source, Parameters param,
                              Parameters hintParam, String mimeType) throws ProcessingException {
        super.setSerializer(role, source, param, hintParam, mimeType);
        this.serializerRole = role;
    }

    /**
     * Set the Reader.
     */
    public void setReader(String role, String source, Parameters param,
                          String mimeType) throws ProcessingException {
        super.setReader(role, source, param, mimeType);
        this.readerRole = role;
    }

    /**
     * Look up the lock object by key, and if present, wait till notified.
     *
     * @return false if able to find a lock and was notified
     */
    protected boolean waitForLock(Object key) {
        if (transientStore != null) {
            final String lockKey = PIPELOCK_PREFIX + key;

            // Get a lock object from the store
            Object lock;
            synchronized (transientStore) {
                lock = transientStore.get(lockKey);
            }

            // Avoid deadlock with self (see JIRA COCOON-1985).
            Object current = RequestContextHolder.getRequestAttributes();
            if (lock != null && lock != current) {


                try {
                    synchronized (lock) {
                        lock.wait();
                    }

                } catch (InterruptedException e) {
                    /* ignored */
                }

                return false;
            }
        }

        return true;
    }

    /**
     * Makes the lock (instantiates a new object and puts it into the store)
     */
    protected void generateLock(Object key) {
        if (transientStore != null && key != null) {
            final String lockKey = PIPELOCK_PREFIX + key;


            synchronized (transientStore) {
                if (transientStore.containsKey(lockKey)) {
                } else {
                    Object lock = RequestContextHolder.getRequestAttributes();
                    try {
                        transientStore.store(lockKey, lock);
                    } catch (IOException e) {
                        /* should not happen */
                    }
                }
            }
        }
    }

    /**
     * Releases the lock (notifies it and removes it from the store)
     */
    protected void releaseLock(Object key) {
        if (transientStore != null && key != null) {
            final String lockKey = PIPELOCK_PREFIX + key;

            Object lock = null;
            synchronized (transientStore) {
                if (!transientStore.containsKey(lockKey)) {
                } else {
                    lock = transientStore.get(lockKey);
                    transientStore.remove(lockKey);
                }
            }

            if (lock != null) {
                // Notify everybody who's waiting
                synchronized (lock) {
                    lock.notifyAll();
                }
            }
        }
    }

    /**
     * Process the given <code>Environment</code>, producing the output.
     * @throws SAXException 
     * @throws IOException 
     */
    protected boolean processXMLPipeline(Environment environment) throws ProcessingException, IOException, SAXException {
        if (this.toCacheKey == null && this.cachedResponse == null) {
            return super.processXMLPipeline(environment);
        }

        if (this.cachedResponse != null && this.completeResponseIsCached) {

            // Allow for 304 (not modified) responses in dynamic content
            if (checkIfModified(environment, this.cachedResponse.getLastModified())) {
                return true;
            }

            // Set mime-type
            if (this.cachedResponse.getContentType() != null) {
                environment.setContentType(this.cachedResponse.getContentType());
            } else {
                setMimeTypeForSerializer(environment);
            }

            // Write response out
            try {
                final OutputStream outputStream = environment.getOutputStream(0);
                final byte[] content = this.cachedResponse.getResponse();
                if (content.length > 0) {
                    environment.setContentLength(content.length);
                    outputStream.write(content);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            setMimeTypeForSerializer(environment);

            generateLock(this.toCacheKey);
            try {
                OutputStream os = null;

                if (this.cacheCompleteResponse && this.toCacheKey != null) {
                    os = new CachingOutputStream(environment.getOutputStream(this.outputBufferSize));
                }

                if (super.serializer != super.lastConsumer) {
                    if (os == null) {
                        os = environment.getOutputStream(this.outputBufferSize);
                    }

                    // internal processing
                    if (this.xmlDeserializer != null) {
                        this.xmlDeserializer.deserialize(this.cachedResponse.getResponse());
                    } else {
                        this.generator.generate();
                    }

                } else {
                    if (this.serializer.shouldSetContentLength()) {
                        if (os == null) {
                            os = environment.getOutputStream(0);
                        }

                        // Set the output stream
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        this.serializer.setOutputStream(baos);

                        // Execute the pipeline
                        if (this.xmlDeserializer != null) {
                            this.xmlDeserializer.deserialize(this.cachedResponse.getResponse());
                        } else {
                            this.generator.generate();
                        }

                        environment.setContentLength(baos.size());
                        baos.writeTo(os);
                    } else {
                        if (os == null) {
                            os = environment.getOutputStream(this.outputBufferSize);
                        }

                        // Set the output stream
                        this.serializer.setOutputStream(os);

                        // Execute the pipeline
                        if (this.xmlDeserializer != null) {
                            this.xmlDeserializer.deserialize(this.cachedResponse.getResponse());
                        } else {
                            this.generator.generate();
                        }
                    }
                }

                //
                // Now that we have processed the pipeline,
                // we do the actual caching
                //
                CachedResponse completeCachedResponse = cacheResults(environment,os);

                if (completeCachedResponse != null) {
                    //Dirty work-around for setting Last-Modified header as there is no appoporiate method
                    //org.apache.cocoon.environment.http.HttpEnvironment.isResponseModified will set it and the result of
                    //the actual check is neither meaningful nor important here
                    environment.isResponseModified(completeCachedResponse.getLastModified());
                }

//            } catch (Exception e) {
//                handleException(e);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                throw new RuntimeException(e);
            } catch (SAXException e) {
                // TODO Auto-generated catch block
                throw new RuntimeException(e);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                throw new RuntimeException(e);
            } finally {
                releaseLock(this.toCacheKey);
            }

            //Request has been succesfully processed, set approporiate status code
            environment.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        return true;
    }

    /**
     * The components of the pipeline are checked if they are Cacheable.
     */
    protected void generateCachingKey(Environment environment) throws ProcessingException {

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
        if (super.generator instanceof CacheableProcessingComponent) {
            key = ((CacheableProcessingComponent) super.generator).getKey();
        }

        if (key != null) {
            this.toCacheKey = new PipelineCacheKey();
            this.toCacheKey.addKey(
                    newComponentCacheKey(
                            ComponentCacheKey.ComponentType_Generator,
                            this.generatorRole, key));

            // now testing transformers
            final int transformerSize = super.transformers.size();
            boolean continueTest = true;

            while (this.firstNotCacheableTransformerIndex < transformerSize && continueTest) {
                final Transformer trans =
                    (Transformer) super.transformers.get(this.firstNotCacheableTransformerIndex);
                key = null;
                if (trans instanceof CacheableProcessingComponent) {
                    key = ((CacheableProcessingComponent)trans).getKey();
                }
                if (key != null) {
                    this.toCacheKey.addKey(
                            newComponentCacheKey(
                                    ComponentCacheKey.ComponentType_Transformer,
                                    (String) this.transformerRoles.get(this.firstNotCacheableTransformerIndex),
                                    key));

                    this.firstNotCacheableTransformerIndex++;
                } else {
                    continueTest = false;
                }
            }
            // all transformers are cacheable => pipeline is cacheable
            // test serializer if this is not an internal request
            if (this.firstNotCacheableTransformerIndex == transformerSize
                && super.serializer == this.lastConsumer) {

                key = null;
                if (super.serializer instanceof CacheableProcessingComponent) {
                    key = ((CacheableProcessingComponent) this.serializer).getKey();
                }
                if (key != null) {
                    this.toCacheKey.addKey(
                            newComponentCacheKey(
                                    ComponentCacheKey.ComponentType_Serializer,
                                    this.serializerRole,
                                    key));
                    this.cacheCompleteResponse = true;
                }
            }
        }
    }

    /**
     * Generate validity objects for the new response
     */
    protected void setupValidities() throws ProcessingException {

        if (this.toCacheKey != null) {
            // only update validity objects if we cannot use
            // a cached response or when the cached response does
            // cache less than now is cacheable
            if (this.fromCacheKey == null
                    || this.fromCacheKey.size() < this.toCacheKey.size()) {

                this.toCacheSourceValidities =
                    new SourceValidity[this.toCacheKey.size()];

                int len = this.toCacheSourceValidities.length;
                int i = 0;
                while (i < len) {
                    final SourceValidity validity = getValidityForInternalPipeline(i);

                    if (validity == null) {
                        if (i > 0
                                && (this.fromCacheKey == null
                                    || i > this.fromCacheKey.size())) {
                            // shorten key
                            for (int m=i; m < this.toCacheSourceValidities.length; m++) {
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
    protected void validatePipeline(Environment environment) throws ProcessingException {

        this.completeResponseIsCached = this.cacheCompleteResponse;
        this.fromCacheKey = this.toCacheKey.copy();
        this.firstProcessedTransformerIndex = this.firstNotCacheableTransformerIndex;

        boolean finished = false;
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

                        // If an expires parameter was provided, use it. If this parameter is not available
                        // it means that the sitemap was modified, and the old expires value is not valid
                        // anymore.
                        if (expires != 0) {
                            response.setExpires(new Long(expires + System.currentTimeMillis()));
                        } else {
                           response.setExpires(null);
                        }
                    }
                } else {
                    // The response had no expires informations. See if it needs to be set (i.e. because the configuration has changed)
                    if (expires != 0) {
                        response.setExpires(new Long(expires + System.currentTimeMillis()));
                    }
                }

                SourceValidity[] fromCacheValidityObjects = response.getValidityObjects();

                int i = 0;
                while (responseIsValid && i < fromCacheValidityObjects.length) {
                    // BH Check if validities[i] is null, may happen
                    //    if exception was thrown due to malformed content
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
                        } else {
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
                            if (i > 0 && i <= firstNotCacheableTransformerIndex + 1) {
                                this.firstNotCacheableTransformerIndex = i-1;
                            }
                            for(int x=0; x < deleteCount; x++) {
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
                if (!waitForLock(this.fromCacheKey)) {
                    finished = false;
                    continue;
                }


                finished = setupFromCacheKey();
                this.completeResponseIsCached = false;
            }
        }
    }

    boolean setupFromCacheKey() {
        // stop on longest key for smart caching
        this.fromCacheKey = null;
        return true;
    }

    /**
     * Setup the evenet pipeline.
     * The components of the pipeline are checked if they are
     * Cacheable.
     * @throws IOException 
     * @throws SAXException 
     */
    protected void setupPipeline(Environment environment) throws ProcessingException, SAXException, IOException {
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
     * Connect the pipeline.
     */
    protected void connectPipeline(Environment   environment) throws ProcessingException {
        if (this.toCacheKey == null && this.cachedResponse == null) {
            super.connectPipeline(environment);
        } else if (this.completeResponseIsCached) {
            // do nothing
        } else {
            connectCachingPipeline(environment);
        }
    }

    /** Process the pipeline using a reader.
     * @throws ProcessingException if an error occurs
     * @throws IOException 
     * @throws SAXException 
     */
    protected boolean processReader(Environment  environment) throws ProcessingException, IOException, SAXException {
//        try {
            boolean usedCache = false;
            OutputStream outputStream = null;
            SourceValidity readerValidity = null;
            PipelineCacheKey pcKey = null;

            // test if reader is cacheable
            Serializable readerKey = null;
            if (super.reader instanceof CacheableProcessingComponent) {
                readerKey = ((CacheableProcessingComponent)super.reader).getKey();
            }

            boolean finished = false;

            if (readerKey != null) {
                // response is cacheable, build the key
                pcKey = new PipelineCacheKey();
                pcKey.addKey(new ComponentCacheKey(ComponentCacheKey.ComponentType_Reader,
                                                   this.readerRole,
                                                   readerKey));

                while (!finished) {
                    finished = true;
                    // now we have the key to get the cached object
                    CachedResponse cachedObject = this.cache.get(pcKey);
                    if (cachedObject != null) {

                        SourceValidity[] validities = cachedObject.getValidityObjects();
                        if (validities == null || validities.length != 1) {
                            // to avoid getting here again and again, we delete it
                            this.cache.remove(pcKey);
                            this.cachedResponse = null;
                        } else {
                            SourceValidity cachedValidity = validities[0];
                            
                            int valid = cachedValidity.isValid();
                            if (valid == SourceValidity.UNKNOWN) {
                                // get reader validity and compare
                                readerValidity = ((CacheableProcessingComponent) super.reader).getValidity();
                                if (readerValidity != null) {
                                    valid = cachedValidity.isValid(readerValidity);
                                    if (valid == SourceValidity.UNKNOWN) {
                                        readerValidity = null;
                                    }
                                }
                            }

                            if (valid == SourceValidity.VALID) {
                                byte[] response = cachedObject.getResponse();
                                if (response.length > 0) {
                                    usedCache = true;
                                    if (cachedObject.getContentType() != null) {
                                        environment.setContentType(cachedObject.getContentType());
                                    } else {
                                        setMimeTypeForReader(environment);
                                    }
                                    outputStream = environment.getOutputStream(0);
                                    environment.setContentLength(response.length);
                                    outputStream.write(response);
                                }
                            } else {
                                // remove invalid cached object
                                this.cache.remove(pcKey);
                            }
                        }
                    } else {
                        // check if something is being generated right now
                        if (!waitForLock(pcKey)) {
                            finished = false;
                            continue;
                        }
                    }
                }
            }

            if (!usedCache) {
                // make sure lock will be released
                try {
                    if (pcKey != null) {
                        generateLock(pcKey);

                        if (readerValidity == null) {
                            readerValidity = ((CacheableProcessingComponent)super.reader).getValidity();
                        }

                        if (readerValidity != null) {
                            outputStream = environment.getOutputStream(this.outputBufferSize);
                            outputStream = new CachingOutputStream(outputStream);
                        }
                    }

                    setMimeTypeForReader(environment);
                    if (this.reader.shouldSetContentLength()) {
                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                        this.reader.setOutputStream(os);
                        this.reader.generate();
                        environment.setContentLength(os.size());
                        if (outputStream == null) {
                            outputStream = environment.getOutputStream(0);
                        }
                        os.writeTo(outputStream);
                    } else {
                        if (outputStream == null) {
                            outputStream = environment.getOutputStream(this.outputBufferSize);
                        }
                        this.reader.setOutputStream(outputStream);
                        this.reader.generate();
                    }

                    // store the response
                    if (pcKey != null && readerValidity != null) {
                        final CachedResponse res = new CachedResponse(new SourceValidity[] {readerValidity},
                                ((CachingOutputStream)outputStream).getContent());
                        res.setContentType(environment.getContentType());
                        this.cache.store(pcKey, res);
                    }

                } finally {
                    releaseLock(pcKey);
                }
            }
//        } catch (Exception e) {
//            handleException(e);
//        }

        //Request has been succesfully processed, set approporiate status code
        environment.setStatus(HttpServletResponse.SC_OK);
        return true;
    }


    /**
     * Return valid validity objects for the event pipeline.
     *
     * If the event pipeline (the complete pipeline without the
     * serializer) is cacheable and valid, return all validity objects.
     *
     * Otherwise, return <code>null</code>.
     */
    public SourceValidity getValidityForEventPipeline() {
        if (isInternalError()) {
            return null;
        }

        if (this.cachedResponse != null) {
            if (!this.cacheCompleteResponse &&
                    this.firstNotCacheableTransformerIndex < super.transformers.size()) {
                // Cache contains only partial pipeline.
                return null;
                    }

            if (this.toCacheSourceValidities != null) {
                // This means that the pipeline is valid based on the validities
                // of the individual components
                final AggregatedValidity validity = new AggregatedValidity();
                for (int i=0; i < this.toCacheSourceValidities.length; i++) {
                    validity.add(this.toCacheSourceValidities[i]);
                }

                return validity;
            }

            // This means that the pipeline is valid because it has not yet expired
            return NOPValidity.SHARED_INSTANCE;
        } else {
            int vals = 0;

            if (null != this.toCacheKey
                    && !this.cacheCompleteResponse
                    && this.firstNotCacheableTransformerIndex == super.transformers.size()) {
                vals = this.toCacheKey.size();
            } else if (null != this.fromCacheKey
                    && !this.completeResponseIsCached
                    && this.firstProcessedTransformerIndex == super.transformers.size()) {
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

    /* (non-Javadoc)
     * @see org.apache.cocoon.components.pipeline.ProcessingPipeline#getKeyForEventPipeline()
     */
    public String getKeyForEventPipeline() {
        if (isInternalError()) {
            return null;
        }

        if (null != this.toCacheKey
                && !this.cacheCompleteResponse
                && this.firstNotCacheableTransformerIndex == super.transformers.size()) {
            return String.valueOf(HashUtil.hash(this.toCacheKey.toString()));
                }
        if (null != this.fromCacheKey
                && !this.completeResponseIsCached
                && this.firstProcessedTransformerIndex == super.transformers.size()) {
            return String.valueOf(HashUtil.hash(this.fromCacheKey.toString()));
                }

        return null;
    }

    SourceValidity getValidityForInternalPipeline(int index) {
        final SourceValidity validity;


        if (index == 0) {
            // test generator
            validity = ((CacheableProcessingComponent)super.generator).getValidity();
        } else if (index <= firstNotCacheableTransformerIndex) {
            // test transformer
            final Transformer trans = (Transformer)super.transformers.get(index-1);
            validity = ((CacheableProcessingComponent)trans).getValidity();
        } else {
            // test serializer
            validity = ((CacheableProcessingComponent)super.serializer).getValidity();
        }
        return validity;
    }

    /**
    * Cache longest cacheable key
    */
    protected CachedResponse cacheResults(Environment environment, OutputStream os)  throws Exception {
        if (this.toCacheKey != null) {
            // See if there is an expires object for this resource.
            Long expiresObj = (Long) environment.getObjectModel().get(ObjectModelHelper.EXPIRES_OBJECT);

            CachedResponse response;
            if (this.cacheCompleteResponse) {
                response = new CachedResponse(this.toCacheSourceValidities,
                                              ((CachingOutputStream) os).getContent(),
                                              expiresObj);
                response.setContentType(environment.getContentType());
            } else {
                response = new CachedResponse(this.toCacheSourceValidities,
                                              (byte[]) this.xmlSerializer.getSAXFragment(),
                                              expiresObj);
            }

            this.cache.store(this.toCacheKey, response);
            return response;
        }
        return null;
    }

    /**
     * Create a new cache key
     */
    protected ComponentCacheKey newComponentCacheKey(int type, String role,Serializable key) {
        return new ComponentCacheKey(type, role, key);
    }

    /**
     * Connect the pipeline.
     */
    protected void connectCachingPipeline(Environment   environment)
    throws ProcessingException {
        XMLByteStreamCompiler localXMLSerializer = null;
        if (!this.cacheCompleteResponse) {
            this.xmlSerializer = new XMLByteStreamCompiler();
            localXMLSerializer = this.xmlSerializer;
        }

        if (this.cachedResponse == null) {
            XMLProducer prev = super.generator;
            XMLConsumer next;

            int cacheableTransformerCount = this.firstNotCacheableTransformerIndex;

            Iterator itt = this.transformers.iterator();
            while (itt.hasNext()) {
                next = (XMLConsumer) itt.next();
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

            next = super.lastConsumer;
            if (localXMLSerializer != null) {
                next = new XMLTeePipe(next, localXMLSerializer);
                localXMLSerializer = null;
            }
            connect(environment, prev, next);
        } else {
            this.xmlDeserializer = new XMLByteStreamInterpreter();

            // connect the pipeline:
            XMLProducer prev = xmlDeserializer;
            XMLConsumer next;
            int cacheableTransformerCount = 0;
            Iterator itt = this.transformers.iterator();
            while (itt.hasNext()) {
                next = (XMLConsumer) itt.next();
                if (cacheableTransformerCount >= this.firstProcessedTransformerIndex) {
                    if (localXMLSerializer != null
                            && cacheableTransformerCount == this.firstNotCacheableTransformerIndex) {
                        next = new XMLTeePipe(next, localXMLSerializer);
                        localXMLSerializer = null;
                    }
                    connect(environment, prev, next);
                    prev = (XMLProducer) next;
                }
                cacheableTransformerCount++;
            }

            next = super.lastConsumer;
            if (localXMLSerializer != null) {
                next = new XMLTeePipe(next, localXMLSerializer);
                localXMLSerializer = null;
            }
            connect(environment, prev, next);
        }
    }
}

final class DeferredPipelineValidity implements DeferredValidity {

    private final CachingPipeline pipeline;
    private final int index;

    public DeferredPipelineValidity(CachingPipeline pipeline, int index) {
        this.pipeline = pipeline;
        this.index = index;
    }

    /**
     * @see org.apache.excalibur.source.impl.validity.DeferredValidity#getValidity()
     */
    public SourceValidity getValidity() {
        return pipeline.getValidityForInternalPipeline(this.index);
    }
}
