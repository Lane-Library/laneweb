package edu.stanford.irt.laneweb.cocoon;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import edu.stanford.irt.cocoon.cache.Cache;
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
    private static final class ByteArraySource implements Source {

        private byte[] byteArray;

        private String uri;

        private ByteArraySource(final byte[] byteArray, final String uri) {
            this.byteArray = new byte[byteArray.length];
            System.arraycopy(byteArray, 0, this.byteArray, 0, byteArray.length);
            this.uri = uri;
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
        public String getURI() {
            return this.uri;
        }
    }

    private static final int BUFFER_SIZE = 1024;

    private static final long MILLISECONDS_PER_MINUTE = 1000L * 60L;

    private Cache cache;

    private SourceResolver sourceResolver;

    /**
     * Create a CacheSourceResolver.
     *
     * @param cache
     *            the Cache
     * @param sourceResolver
     *            the resolver for the cached sources.
     */
    public CacheSourceResolver(final Cache cache, final SourceResolver sourceResolver) {
        this.cache = cache;
        this.sourceResolver = sourceResolver;
    }

    /**
     * resolve a cache: scheme URI
     */
    @Override
    public Source resolveURI(final URI cacheURI) {
        CachedResponse cachedResponse = this.cache.get(cacheURI);
        if (cachedResponse == null || !cachedResponse.getValidity().isValid()) {
            String schemeSpecificPart = cacheURI.getRawSchemeSpecificPart();
            int colon = schemeSpecificPart.indexOf(':');
            try {
                URI uri = new URI(schemeSpecificPart.substring(colon + 1));
                Source source = this.sourceResolver.resolveURI(uri);
                byte[] bytes = getBytesFromSource(source);
                long minutes = Long.parseLong(schemeSpecificPart.substring(0, colon));
                Validity validity = new ExpiresValidity(minutes * MILLISECONDS_PER_MINUTE);
                cachedResponse = new CachedResponse(validity, bytes);
                this.cache.store(cacheURI, cachedResponse);
            } catch (URISyntaxException | IOException e) {
                throw new LanewebException(e);
            }
        }
        return new ByteArraySource(cachedResponse.getBytes(), cacheURI.toString());
    }

    /**
     * get the byte array from a Source object.
     *
     * @param source
     *            the Source
     * @return a byte array creted from the InputStream of the Source.
     * @throws IOException
     *             if getting the InputStream from the Source fails
     */
    private byte[] getBytesFromSource(final Source source) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream input = source.getInputStream();
        byte[] buffer = new byte[BUFFER_SIZE];
        while (true) {
            int i = input.read(buffer);
            if (i == -1) {
                break;
            }
            baos.write(buffer, 0, i);
        }
        return baos.toByteArray();
    }
}
