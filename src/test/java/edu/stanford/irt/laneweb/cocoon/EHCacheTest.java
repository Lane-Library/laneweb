package edu.stanford.irt.laneweb.cocoon;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.api.easymock.PowerMock.verify;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import edu.stanford.irt.cocoon.cache.CachedResponse;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Cache.class)
public class EHCacheTest {

    private EHCache ehcache;

    private Element element;

    private Cache mockCache;

    @Before
    public void setUp() throws Exception {
        this.mockCache = createMock(Cache.class);
        CacheManager manager = createMock(CacheManager.class);
        expect(manager.getCache("cocoon-ehcache")).andReturn(this.mockCache);
        replay(manager);
        this.ehcache = new EHCache(manager);
        this.element = new Element("key", new CachedResponse(null, null));
    }

    @Test
    public void testClear() {
        this.mockCache.removeAll();
        replay(this.mockCache);
        this.ehcache.clear();
        verify(this.mockCache);
    }

    @Test
    public void testContainsKeyFalse() {
        expect(this.mockCache.get("key")).andReturn(null);
        replay(this.mockCache);
        assertFalse(this.ehcache.containsKey("key"));
        verify(this.mockCache);
    }

    @Test
    public void testContainsKeyTrue() {
        expect(this.mockCache.get("key")).andReturn(this.element);
        replay(this.mockCache);
        assertTrue(this.ehcache.containsKey("key"));
        verify(this.mockCache);
    }

    @Test
    public void testGetElement() {
        expect(this.mockCache.get("key")).andReturn(this.element);
        replay(this.mockCache);
        assertNotNull(this.ehcache.get("key"));
        verify(this.mockCache);
    }

    @Test
    public void testGetNull() {
        expect(this.mockCache.get("key")).andReturn(null);
        replay(this.mockCache);
        assertNull(this.ehcache.get("key"));
        verify(this.mockCache);
    }

    @Test
    public void testRemove() {
        expect(this.mockCache.remove("key")).andReturn(true);
        replay(this.mockCache);
        this.ehcache.remove("key");
        verify(this.mockCache);
    }

    @Test
    public void testStore() {
        this.mockCache.put(isA(Element.class));
        replay(this.mockCache);
        this.ehcache.store("key", new CachedResponse(null, null));
        verify(this.mockCache);
    }
}
