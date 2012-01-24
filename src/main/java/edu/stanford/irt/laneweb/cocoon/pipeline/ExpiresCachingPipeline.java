package edu.stanford.irt.laneweb.cocoon.pipeline;

import java.io.IOException;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.caching.Cache;
import org.apache.cocoon.caching.CachedResponse;
import org.apache.cocoon.caching.CachingOutputStream;
import org.apache.cocoon.caching.IdentifierCacheKey;
import org.apache.cocoon.components.sax.XMLByteStreamCompiler;
import org.apache.cocoon.components.sax.XMLByteStreamInterpreter;
import org.apache.cocoon.components.sax.XMLTeePipe;
import org.apache.cocoon.environment.Environment;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.Generator;
import org.apache.cocoon.serialization.Serializer;
import org.apache.cocoon.xml.XMLConsumer;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.ExpiresValidity;
import org.apache.excalibur.source.impl.validity.NOPValidity;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.LanewebException;

public class ExpiresCachingPipeline extends NonCachingPipeline {

    /** This is the Cache holding cached responses */
    private Cache cache;

    /** The cached response */
    private CachedResponse cachedResponse;

    /** The expires information. */
    private long cacheExpires;

    /** The key used for caching */
    private IdentifierCacheKey cacheKey;

    /** The source validity */
    private SourceValidity cacheValidity;

    /** Default value for expiration */
    private long defaultCacheExpires = 3600; // 1 hour

    /** The deserializer */
    private XMLByteStreamInterpreter xmlDeserializer;

    /** The serializer */
    private XMLByteStreamCompiler xmlSerializer;

    public ExpiresCachingPipeline(final SourceResolver sourceResolver, final Cache cache, final long cacheExpires) {
        super(sourceResolver);
        this.cache = cache;
        this.cacheExpires = cacheExpires;
    }

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
     * Connect the XML pipeline.
     */
    @Override
    protected void connectPipeline(final Environment environment) throws ProcessingException {
        XMLConsumer lastConsumer = getLastConsumer();
        if (lastConsumer != getSerializer()) {
            // internal
            if (this.cachedResponse == null) {
                // if we cache, we need an xml serializer
                if (this.cacheExpires != 0) {
                    final XMLConsumer old = lastConsumer;
                    this.xmlSerializer = new XMLByteStreamCompiler();
                    setLastConsumer(new XMLTeePipe(lastConsumer, this.xmlSerializer));
                    super.connectPipeline(environment);
                    setLastConsumer(old);
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

    /**
     * Prepare the pipeline
     */
    @Override
    protected void preparePipeline(final Environment environment) throws ProcessingException {
        Parameters parameters = getParameters();
        String key = parameters.getParameter("cache-key", null);
        if (key == null) {
            throw new LanewebException("null cache-key");
        }
        this.cacheExpires = parameters.getParameterAsLong("cache-expires", this.defaultCacheExpires);
        // prepare the pipeline
        super.preparePipeline(environment);
        // and now prepare the caching information
        XMLConsumer lastConsumer = getLastConsumer();
        Serializer serializer = getSerializer();
        this.cacheKey = new IdentifierCacheKey(key, serializer == lastConsumer);
        if (this.cacheExpires > 0) {
            this.cacheValidity = new ExpiresValidity(this.cacheExpires * 1000);
        } else if (this.cacheExpires < 0) {
            this.cacheValidity = NOPValidity.SHARED_INSTANCE;
        }
        final boolean purge = parameters.getParameterAsBoolean("purge-cache", false);
        this.cachedResponse = this.cache.get(this.cacheKey);
        if (this.cachedResponse != null) {
            final SourceValidity sv = this.cachedResponse.getValidityObjects()[0];
            if (purge || (this.cacheExpires != -1 && sv.isValid() != SourceValidity.VALID)) {
                this.cache.remove(this.cacheKey);
                this.cachedResponse = null;
            }
        }
    }

    /**
     * Process the given <code>Environment</code>, producing the output.
     */
    @Override
    protected boolean processXMLPipeline(final Environment environment) throws ProcessingException {
        XMLConsumer lastConsumer = getLastConsumer();
        Serializer serializer = getSerializer();
        try {
            if (this.cachedResponse != null) {
                byte[] content = this.cachedResponse.getResponse();
                if (serializer == lastConsumer) {
                    environment.getOutputStream(0).write(content);
                } else {
                    this.xmlDeserializer.setConsumer(lastConsumer);
                    this.xmlDeserializer.deserialize(content);
                }
            } else {
                // generate new response
                if (this.cacheExpires == 0) {
                    return super.processXMLPipeline(environment);
                }
                byte[] cachedData;
                Generator generator = getGenerator();
                int outputBufferSize = getOutputBufferSize();
                if (serializer == lastConsumer) {
                    CachingOutputStream os = new CachingOutputStream(environment.getOutputStream(outputBufferSize));
                    // set the output stream
                    serializer.setOutputStream(os);
                    generator.generate();
                    cachedData = os.getContent();
                } else {
                    generator.generate();
                    cachedData = (byte[]) this.xmlSerializer.getSAXFragment();
                }
                //
                // Now that we have processed the pipeline,
                // we do the actual caching
                //
                if (this.cacheValidity != null) {
                    this.cachedResponse = new CachedResponse(this.cacheValidity, cachedData);
                    this.cache.store(this.cacheKey, this.cachedResponse);
                }
            }
        } catch (IOException e) {
            throw new LanewebException(e);
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
        return true;
    }
}
