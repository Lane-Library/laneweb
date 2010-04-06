package edu.stanford.irt.laneweb.cocoon.pipeline;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.caching.Cache;
import org.apache.cocoon.caching.CachedResponse;
import org.apache.cocoon.caching.CachingOutputStream;
import org.apache.cocoon.caching.IdentifierCacheKey;
import org.apache.cocoon.components.pipeline.impl.ExpiresCachingProcessingPipeline;
import org.apache.cocoon.components.sax.XMLByteStreamCompiler;
import org.apache.cocoon.components.sax.XMLByteStreamInterpreter;
import org.apache.cocoon.components.sax.XMLTeePipe;
import org.apache.cocoon.environment.Environment;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Response;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.xml.XMLConsumer;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.ExpiresValidity;
import org.apache.excalibur.source.impl.validity.NOPValidity;

public class ExpiresCachingPipeline extends NonCachingPipeline {

    /** This key can be used to put an expires information in the object model */
    public static final String CACHE_EXPIRES_KEY = ExpiresCachingProcessingPipeline.class.getName() + "/Expires";

    /** This key can be used to put a key in the object model */
    public static final String CACHE_KEY_KEY = ExpiresCachingProcessingPipeline.class.getName() + "/CacheKey";

    /** This is the Cache holding cached responses */
    protected Cache cache;

    /** The cached response */
    protected CachedResponse cachedResponse;

    /** The expires information. */
    protected long cacheExpires;

    /** The key used for caching */
    protected IdentifierCacheKey cacheKey;

    /** The source validity */
    protected SourceValidity cacheValidity;

    /** Default value for expiration */
    protected long defaultCacheExpires = 3600; // 1 hour

    /** The deserializer */
    protected XMLByteStreamInterpreter xmlDeserializer;

    /** The serializer */
    protected XMLByteStreamCompiler xmlSerializer;

    public ExpiresCachingPipeline(final SourceResolver sourceResolver, final Cache cache, final long cacheExpires) {
        super(sourceResolver);
        this.cache = cache;
        this.cacheExpires = cacheExpires;
    }

    /**
     * Connect the XML pipeline.
     */
    @Override
    protected void connectPipeline(final Environment environment) throws ProcessingException {
        if (this.lastConsumer != this.serializer) {
            // internal
            if (this.cachedResponse == null) {
                // if we cache, we need an xml serializer
                if (this.cacheExpires != 0) {
                    final XMLConsumer old = this.lastConsumer;
                    this.xmlSerializer = new XMLByteStreamCompiler();
                    this.lastConsumer = new XMLTeePipe(this.lastConsumer, this.xmlSerializer);
                    super.connectPipeline(environment);
                    this.lastConsumer = old;
                } else {
                    super.connectPipeline(environment);
                }
            } else {
                // we use the cache, so we need an xml deserializer
                this.xmlDeserializer = new XMLByteStreamInterpreter();
            }
        } else {
            // external: we only need to connect if we don't use a cached
            // response
            if (this.cachedResponse == null) {
                super.connectPipeline(environment);
            }
        }
    }

    /*
     * (non-Javadoc)
     * @seeorg.apache.cocoon.components.pipeline.ProcessingPipeline#
     * getKeyForEventPipeline()
     */
    @Override
    public String getKeyForEventPipeline() {
        if (this.cacheKey != null && this.cacheValidity != null) {
            return this.cacheKey.toString();
        }
        return null;
    }

    /**
     * Return valid validity objects for the event pipeline If the
     * "event pipeline" (= the complete pipeline without the serializer) is
     * cacheable and valid, return all validity objects. Otherwise return
     * <code>null</code>
     */
    @Override
    public SourceValidity getValidityForEventPipeline() {
        return this.cacheValidity;
    }

    /**
     * Prepare the pipeline
     */
    @Override
    protected void preparePipeline(final Environment environment) throws ProcessingException {
        // get the key and the expires info
        // we must do this before we call super.preparePipeline,
        // otherwise internal pipelines are instantiated and
        // get a copy of the object model with our info!
        final Map objectModel = environment.getObjectModel();
        String key = (String) objectModel.get(CACHE_KEY_KEY);
        if (key == null) {
            key = this.parameters.getParameter("cache-key", null);
            if (key == null) {
                key = environment.getURIPrefix() + environment.getURI();
            }
        } else {
            objectModel.remove(CACHE_KEY_KEY);
        }
        String expiresValue = (String) objectModel.get(CACHE_EXPIRES_KEY);
        if (expiresValue == null) {
            this.cacheExpires = this.parameters.getParameterAsLong("cache-expires", this.defaultCacheExpires);
        } else {
            this.cacheExpires = Long.valueOf(expiresValue).longValue();
            objectModel.remove(CACHE_EXPIRES_KEY);
        }
        // prepare the pipeline
        super.preparePipeline(environment);
        // and now prepare the caching information
        this.cacheKey = new IdentifierCacheKey(key, this.serializer == this.lastConsumer);
        if (this.cacheExpires > 0) {
            this.cacheValidity = new ExpiresValidity(this.cacheExpires * 1000);
        } else if (this.cacheExpires < 0) {
            this.cacheValidity = NOPValidity.SHARED_INSTANCE;
        }
        final boolean purge = this.parameters.getParameterAsBoolean("purge-cache", false);
        this.cachedResponse = this.cache.get(this.cacheKey);
        if (this.cachedResponse != null) {
            final SourceValidity sv = this.cachedResponse.getValidityObjects()[0];
            if (purge || (this.cacheExpires != -1 && sv.isValid() != SourceValidity.VALID)) {
                this.cache.remove(this.cacheKey);
                this.cachedResponse = null;
            }
        }
        if (this.cacheExpires > 0 && (this.reader != null || this.lastConsumer == this.serializer)) {
            Response res = ObjectModelHelper.getResponse(environment.getObjectModel());
            res.setDateHeader("Expires", System.currentTimeMillis() + (this.cacheExpires * 1000));
            res.setHeader("Cache-Control", "max-age=" + this.cacheExpires + ", public");
        }
    }

    /*
     * (non-Javadoc)
     * @seeorg.apache.cocoon.components.pipeline.AbstractProcessingPipeline#
     * processReader(org.apache.cocoon.environment.Environment)
     */
    @Override
    protected boolean processReader(final Environment environment) throws ProcessingException {
        try {
            if (this.cachedResponse != null) {
                if (this.cachedResponse.getContentType() != null) {
                    environment.setContentType(this.cachedResponse.getContentType());
                } else {
                    this.setMimeTypeForReader(environment);
                }
                final byte[] content = this.cachedResponse.getResponse();
                environment.setContentLength(content.length);
                final OutputStream os = environment.getOutputStream(0);
                os.write(content);
            } else {
                // generate new response
                if (this.cacheExpires == 0) {
                    return super.processReader(environment);
                }
                byte[] cachedData;
                this.setMimeTypeForReader(environment);
                if (this.reader.shouldSetContentLength()) {
                    final OutputStream os = environment.getOutputStream(this.outputBufferSize);
                    // set the output stream
                    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    this.reader.setOutputStream(baos);
                    this.reader.generate();
                    cachedData = baos.toByteArray();
                    environment.setContentLength(cachedData.length);
                    os.write(cachedData);
                } else {
                    final CachingOutputStream os = new CachingOutputStream(environment.getOutputStream(this.outputBufferSize));
                    // set the output stream
                    this.reader.setOutputStream(os);
                    this.reader.generate();
                    cachedData = os.getContent();
                }
                //
                // Now that we have processed the pipeline,
                // we do the actual caching
                //
                if (this.cacheValidity != null) {
                    this.cachedResponse = new CachedResponse(this.cacheValidity, cachedData);
                    this.cachedResponse.setContentType(environment.getContentType());
                    this.cache.store(this.cacheKey, this.cachedResponse);
                }
            }
        } catch (Exception e) {
            handleException(e);
        }
        // Request has been succesfully processed, set approporiate status code
        environment.setStatus(HttpServletResponse.SC_OK);
        return true;
    }

    /**
     * Process the given <code>Environment</code>, producing the output.
     */
    @Override
    protected boolean processXMLPipeline(final Environment environment) throws ProcessingException {
        try {
            if (this.cachedResponse != null) {
                byte[] content = this.cachedResponse.getResponse();
                if (this.serializer == this.lastConsumer) {
                    if (this.cachedResponse.getContentType() != null) {
                        environment.setContentType(this.cachedResponse.getContentType());
                    } else {
                        this.setMimeTypeForSerializer(environment);
                    }
                    final OutputStream outputStream = environment.getOutputStream(0);
                    if (content.length > 0) {
                        environment.setContentLength(content.length);
                        outputStream.write(content);
                    }
                } else {
                    this.setMimeTypeForSerializer(environment);
                    this.xmlDeserializer.setConsumer(this.lastConsumer);
                    this.xmlDeserializer.deserialize(content);
                }
            } else {
                // generate new response
                if (this.cacheExpires == 0) {
                    return super.processXMLPipeline(environment);
                }
                this.setMimeTypeForSerializer(environment);
                byte[] cachedData;
                if (this.serializer == this.lastConsumer) {
                    if (this.serializer.shouldSetContentLength()) {
                        OutputStream os = environment.getOutputStream(this.outputBufferSize);
                        // set the output stream
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        this.serializer.setOutputStream(baos);
                        this.generator.generate();
                        cachedData = baos.toByteArray();
                        environment.setContentLength(cachedData.length);
                        os.write(cachedData);
                    } else {
                        CachingOutputStream os = new CachingOutputStream(environment.getOutputStream(this.outputBufferSize));
                        // set the output stream
                        this.serializer.setOutputStream(os);
                        this.generator.generate();
                        cachedData = os.getContent();
                    }
                } else {
                    this.generator.generate();
                    cachedData = (byte[]) this.xmlSerializer.getSAXFragment();
                }
                //
                // Now that we have processed the pipeline,
                // we do the actual caching
                //
                if (this.cacheValidity != null) {
                    this.cachedResponse = new CachedResponse(this.cacheValidity, cachedData);
                    this.cachedResponse.setContentType(environment.getContentType());
                    this.cache.store(this.cacheKey, this.cachedResponse);
                }
            }
        } catch (Exception e) {
            handleException(e);
        }
        // Request has been succesfully processed, set approporiate status code
        environment.setStatus(HttpServletResponse.SC_OK);
        return true;
    }
}
