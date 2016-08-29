package edu.stanford.irt.laneweb.cocoon;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.api.easymock.PowerMock.verify;

import java.io.Serializable;

import javax.cache.Cache;
import javax.cache.CacheManager;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.cache.CachedResponse;

public class JCacheTest {

    private CachedResponse cachedResponse;

    private JCache jcache;

    private Cache<Serializable, CachedResponse> mockCache;

    @Before
    public void setUp() throws Exception {
        this.mockCache = createMock(Cache.class);
        CacheManager manager = createMock(CacheManager.class);
        expect(manager.getCache("cocoon-cache", Serializable.class, CachedResponse.class)).andReturn(this.mockCache);
        replay(manager);
        this.jcache = new JCache(manager);
        this.cachedResponse = createMock(CachedResponse.class);
    }

    @Test
    public void testClear() {
        this.mockCache.removeAll();
        replay(this.mockCache);
        this.jcache.clear();
        verify(this.mockCache);
    }

    @Test
    public void testContainsKeyFalse() {
        expect(this.mockCache.get("key")).andReturn(null);
        replay(this.mockCache);
        assertFalse(this.jcache.containsKey("key"));
        verify(this.mockCache);
    }

    @Test
    public void testContainsKeyTrue() {
        expect(this.mockCache.get("key")).andReturn(this.cachedResponse);
        replay(this.mockCache);
        assertTrue(this.jcache.containsKey("key"));
        verify(this.mockCache);
    }

    @Test
    public void testGetElement() {
        expect(this.mockCache.get("key")).andReturn(this.cachedResponse);
        replay(this.mockCache);
        assertNotNull(this.jcache.get("key"));
        verify(this.mockCache);
    }

    @Test
    public void testGetNull() {
        expect(this.mockCache.get("key")).andReturn(null);
        replay(this.mockCache);
        assertNull(this.jcache.get("key"));
        verify(this.mockCache);
    }

    @Test
    public void testRemove() {
        expect(this.mockCache.remove("key")).andReturn(true);
        replay(this.mockCache);
        this.jcache.remove("key");
        verify(this.mockCache);
    }

    @Test
    public void testStore() {
        this.mockCache.put("key", this.cachedResponse);
        replay(this.mockCache);
        this.jcache.store("key", this.cachedResponse);
        verify(this.mockCache);
    }
}
