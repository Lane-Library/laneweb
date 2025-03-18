package edu.stanford.irt.laneweb.cocoon;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Serializable;

import javax.cache.Cache;
import javax.cache.CacheManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.cocoon.cache.CachedResponse;

public class CacheFactoryBeanTest {

    private Cache<Serializable, CachedResponse> cache;

    private CacheFactoryBean cacheFactoryBean;

    private CacheManager cacheManager;

    @BeforeEach
    public void setUp() {
        this.cache = mock(Cache.class);
        this.cacheManager = mock(CacheManager.class);
        expect(this.cacheManager.getCache("cocoon-cache", Serializable.class, CachedResponse.class))
                .andReturn(this.cache);
        replay(this.cacheManager);
        this.cacheFactoryBean = new CacheFactoryBean(this.cacheManager);
    }

    @Test
    public void testGetObject() {
        assertSame(this.cache, this.cacheFactoryBean.getObject());
    }

    @Test
    public void testGetObjectType() {
        assertSame(Cache.class, this.cacheFactoryBean.getObjectType());
    }

    @Test
    public void testIsSingleton() {
        assertTrue(this.cacheFactoryBean.isSingleton());
    }
}
