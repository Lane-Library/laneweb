package edu.stanford.irt.laneweb.cocoon;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.cache.Cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

    @BeforeEach
    public void setUp() throws Exception {
        this.cache = mock(Cache.class);
        this.sourceResolver = mock(SourceResolver.class);
        this.cacheSourceResolver = new CacheSourceResolver(this.cache, this.sourceResolver);
        this.cachedResponse = mock(CachedResponse.class);
        this.validity = mock(Validity.class);
        this.source = mock(Source.class);
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
        expect(this.source.getURI()).andReturn("http://www.example.com/");
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
        expect(this.source.getURI()).andReturn("\"http://www.example.com/\"");
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
