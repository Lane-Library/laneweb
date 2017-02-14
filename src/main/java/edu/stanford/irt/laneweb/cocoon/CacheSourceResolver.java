package edu.stanford.irt.laneweb.cocoon;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

import javax.cache.Cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.irt.cocoon.cache.Cacheable;
import edu.stanford.irt.cocoon.cache.CachedResponse;
import edu.stanford.irt.cocoon.cache.Validity;
import edu.stanford.irt.cocoon.cache.validity.ExpiresValidity;
import edu.stanford.irt.cocoon.source.Source;
import edu.stanford.irt.cocoon.source.SourceResolver;
import edu.stanford.irt.laneweb.LanewebException;

/**
 * A SourceResolver that resolves URIs with the cache: scheme. The URI syntax for this scheme is cache:[cache time in
 * minutes]:[cached URI].
 */
public class CacheSourceResolver implements SourceResolver {

    /**
     * A Source implementation that wraps a byte array.
     */
    static class ByteArraySource implements Source, Cacheable {

        private byte[] byteArray;

        private String uri;

        private Validity validity;

        ByteArraySource(final byte[] byteArray, final String uri, final Validity validity) {
            this.byteArray = new byte[byteArray.length];
            System.arraycopy(byteArray, 0, this.byteArray, 0, byteArray.length);
            this.uri = uri;
            this.validity = validity;
        }

        @Override
        public boolean exists() {
            return true;
        }

        @Override
        public InputStream getInputStream() {
            return new ByteArrayInputStream(this.byteArray);
        }

        @Override
        public Serializable getKey() {
            return this.uri;
        }

        @Override
        public String getURI() {
            return this.uri;
        }

        @Override
        public Validity getValidity() {
            return this.validity;
        }
    }

    static final class CacheSourceException extends LanewebException {

        private static final long serialVersionUID = 1L;

        public CacheSourceException(final String message, final Throwable cause) {
            super(message, cause);
        }
    }

    private static final int BUFFER_SIZE = 1024;

    private static final Logger log = LoggerFactory.getLogger(CacheSourceResolver.class);

    private static final long MILLISECONDS_PER_MINUTE = 1000L * 60L;

    private Cache<Serializable, CachedResponse> cache;

    private SourceResolver sourceResolver;

    /**
     * Create a CacheSourceResolver.
     *
     * @param cache
     *            the Cache
     * @param sourceResolver
     *            the resolver for the cached sources.
     */
    public CacheSourceResolver(final Cache<Serializable, CachedResponse> cache, final SourceResolver sourceResolver) {
        this.cache = cache;
        this.sourceResolver = sourceResolver;
    }

    /**
     * resolve a cache: scheme URI
     */
    @Override
    public Source resolveURI(final URI cacheURI) {
        CachedResponse cachedResponse = this.cache.get(cacheURI);
        if (cachedResponse == null) {
            cachedResponse = createCachedResponse(cacheURI);
            this.cache.put(cacheURI, cachedResponse);
        } else if (!cachedResponse.getValidity().isValid()) {
            try {
                cachedResponse = createCachedResponse(cacheURI);
                this.cache.put(cacheURI, cachedResponse);
            } catch (CacheSourceException e) {
                log.warn("failed to get resource {}, using expired cache ({})", cacheURI, e.getCause().getMessage());
            }
        }
        return createSource(cachedResponse.getBytes(), cacheURI.toString(), cachedResponse.getValidity());
    }

    protected Source createSource(final byte[] bytes, final String uri, final Validity validity) {
        return new ByteArraySource(bytes, uri, validity);
    }

    /**
     * get the byte array from a Source object.
     *
     * @param source
     *            the Source
     * @return a byte array created from the InputStream of the Source.
     */
    protected byte[] getBytesFromSource(final Source source) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream input;
        try {
            input = source.getInputStream();
            byte[] buffer = new byte[BUFFER_SIZE];
            while (true) {
                int i = input.read(buffer);
                if (i == -1) {
                    break;
                }
                baos.write(buffer, 0, i);
            }
        } catch (IOException e) {
            throw new CacheSourceException("failed to get bytes", e);
        }
        return baos.toByteArray();
    }

    private CachedResponse createCachedResponse(final URI cacheURI) {
        String schemeSpecificPart = cacheURI.getRawSchemeSpecificPart();
        int colon = schemeSpecificPart.indexOf(':');
        URI uri;
        try {
            uri = new URI(schemeSpecificPart.substring(colon + 1));
        } catch (URISyntaxException e) {
            throw new CacheSourceException("failed to create CachedResponse", e);
        }
        Source source = this.sourceResolver.resolveURI(uri);
        byte[] bytes = getBytesFromSource(source);
        long minutes = Long.parseLong(schemeSpecificPart.substring(0, colon));
        Validity validity = new ExpiresValidity(minutes * MILLISECONDS_PER_MINUTE);
        return new CachedResponse(validity, bytes);
    }
}
