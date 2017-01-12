package edu.stanford.irt.laneweb.cocoon;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.cache.Cache;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.cache.Cacheable;
import edu.stanford.irt.cocoon.cache.CachedResponse;
import edu.stanford.irt.cocoon.cache.Validity;
import edu.stanford.irt.cocoon.source.Source;
import edu.stanford.irt.cocoon.source.SourceResolver;
import edu.stanford.irt.laneweb.LanewebException;

public class CacheSourceResolverTest {

    private Cache<Serializable, CachedResponse> cache;

    private CachedResponse cachedResponse;

    private CacheSourceResolver cacheSourceResolver;

    private Source source;

    private SourceResolver sourceResolver;

    private Validity validity;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.cache = createMock(Cache.class);
        this.sourceResolver = createMock(SourceResolver.class);
        this.cacheSourceResolver = new CacheSourceResolver(this.cache, this.sourceResolver);
        this.cachedResponse = createMock(CachedResponse.class);
        this.validity = createMock(Validity.class);
        this.source = createMock(Source.class);
    }

    @Test
    public void testGetValidity() {
    }

    @Test
    public void testResolveURI() throws URISyntaxException, IOException {
        expect(this.cache.get(new URI("cache:20:http://www.example.com/"))).andReturn(this.cachedResponse);
        expect(this.cachedResponse.getValidity()).andReturn(this.validity).times(2);
        expect(this.validity.isValid()).andReturn(true);
        expect(this.cachedResponse.getBytes()).andReturn("foo".getBytes());
        replay(this.cache, this.sourceResolver, this.cachedResponse, this.validity);
        Source source = this.cacheSourceResolver.resolveURI(new URI("cache:20:http://www.example.com/"));
        assertEquals("cache:20:http://www.example.com/", source.getURI());
        assertTrue(source.exists());
        assertSame(this.validity, ((Cacheable) source).getValidity());
        assertEquals("cache:20:http://www.example.com/", source.getURI());
        assertEquals("cache:20:http://www.example.com/", ((Cacheable) source).getKey());
        byte[] bytes = new byte[3];
        source.getInputStream().read(bytes);
        assertEquals("foo", new String(bytes));
        verify(this.cache, this.sourceResolver, this.cachedResponse, this.validity);
    }

    @Test
    public void testResolveURIIOException() throws URISyntaxException, IOException {
        expect(this.cache.get(new URI("cache:20:http://www.example.com/"))).andReturn(null);
        expect(this.sourceResolver.resolveURI(new URI("http://www.example.com/"))).andReturn(this.source);
        expect(this.source.getInputStream()).andThrow(new IOException());
        replay(this.cache, this.sourceResolver, this.cachedResponse, this.validity, this.source);
        try {
            this.cacheSourceResolver.resolveURI(new URI("cache:20:http://www.example.com/"));
            fail();
        } catch (LanewebException e) {
            assertTrue(e.getCause() instanceof IOException);
        }
        verify(this.cache, this.sourceResolver, this.cachedResponse, this.validity, this.source);
    }

    @Test
    public void testResolveURINotCached() throws URISyntaxException, IOException {
        expect(this.cache.get(new URI("cache:20:http://www.example.com/"))).andReturn(null);
        expect(this.sourceResolver.resolveURI(new URI("http://www.example.com/"))).andReturn(this.source);
        expect(this.source.getInputStream()).andReturn(new ByteArrayInputStream("foo".getBytes()));
        this.cache.put(eq(new URI("cache:20:http://www.example.com/")), isA(CachedResponse.class));
        replay(this.cache, this.sourceResolver, this.cachedResponse, this.validity, this.source);
        Source source = this.cacheSourceResolver.resolveURI(new URI("cache:20:http://www.example.com/"));
        assertEquals("cache:20:http://www.example.com/", source.getURI());
        assertTrue(source.exists());
        Validity v = ((Cacheable) source).getValidity();
        assertTrue(v.isValid());
        byte[] bytes = new byte[3];
        source.getInputStream().read(bytes);
        assertEquals("foo", new String(bytes));
        verify(this.cache, this.sourceResolver, this.cachedResponse, this.validity, this.source);
    }

    @Test
    public void testResolveURINotValid() throws URISyntaxException, IOException {
        expect(this.cache.get(new URI("cache:20:http://www.example.com/"))).andReturn(this.cachedResponse);
        expect(this.cachedResponse.getValidity()).andReturn(this.validity);
        expect(this.validity.isValid()).andReturn(false);
        expect(this.sourceResolver.resolveURI(new URI("http://www.example.com/"))).andReturn(this.source);
        expect(this.source.getInputStream()).andReturn(new ByteArrayInputStream("foo".getBytes()));
        this.cache.put(eq(new URI("cache:20:http://www.example.com/")), isA(CachedResponse.class));
        replay(this.cache, this.sourceResolver, this.cachedResponse, this.validity, this.source);
        Source source = this.cacheSourceResolver.resolveURI(new URI("cache:20:http://www.example.com/"));
        assertEquals("cache:20:http://www.example.com/", source.getURI());
        assertTrue(source.exists());
        byte[] bytes = new byte[3];
        source.getInputStream().read(bytes);
        assertEquals("foo", new String(bytes));
        verify(this.cache, this.sourceResolver, this.cachedResponse, this.validity, this.source);
    }

    @Test
    public void testResolveURINotValidIOException() throws URISyntaxException, IOException {
        expect(this.cache.get(new URI("cache:20:http://www.example.com/"))).andReturn(this.cachedResponse);
        expect(this.cachedResponse.getValidity()).andReturn(this.validity).times(2);
        expect(this.validity.isValid()).andReturn(false);
        expect(this.sourceResolver.resolveURI(new URI("http://www.example.com/"))).andReturn(this.source);
        expect(this.source.getInputStream()).andThrow(new SocketTimeoutException());
        expect(this.cachedResponse.getBytes()).andReturn("foo".getBytes());
        replay(this.cache, this.sourceResolver, this.cachedResponse, this.validity, this.source);
        Source source = this.cacheSourceResolver.resolveURI(new URI("cache:20:http://www.example.com/"));
        assertEquals("cache:20:http://www.example.com/", source.getURI());
        assertTrue(source.exists());
        byte[] bytes = new byte[3];
        source.getInputStream().read(bytes);
        assertEquals("foo", new String(bytes));
        verify(this.cache, this.sourceResolver, this.cachedResponse, this.validity, this.source);
    }

    @Test
    public void testResolveURISyntaxException() throws URISyntaxException, IOException {
        expect(this.cache.get(new URI("cache:20::"))).andReturn(null);
        replay(this.cache, this.sourceResolver, this.cachedResponse, this.validity, this.source);
        try {
            this.cacheSourceResolver.resolveURI(new URI("cache:20::"));
            fail();
        } catch (LanewebException e) {
            assertTrue(e.getCause() instanceof URISyntaxException);
        }
        verify(this.cache, this.sourceResolver, this.cachedResponse, this.validity, this.source);
    }
}
