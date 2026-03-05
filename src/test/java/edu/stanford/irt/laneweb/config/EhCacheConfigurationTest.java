package edu.stanford.irt.laneweb.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.io.Serializable;

import javax.cache.Cache;
import javax.cache.CacheManager;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import edu.stanford.irt.cocoon.cache.CachedResponse;

public class EhCacheConfigurationTest {

    @Test
    public void testBeansCreated() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                EhCacheConfiguration.class)) {
            CacheManager cacheManager = context.getBean(CacheManager.class);
            assertNotNull(cacheManager);

            @SuppressWarnings("unchecked")
            Cache<Serializable, CachedResponse> cache = (Cache<Serializable, CachedResponse>) context.getBean("cache");
            assertNotNull(cache);
            assertEquals(EhCacheConfiguration.COCOON_CACHE, cache.getName());

            Cache<Serializable, CachedResponse> fromManager = cacheManager.getCache(EhCacheConfiguration.COCOON_CACHE,
                    Serializable.class, CachedResponse.class);
            assertNotNull(fromManager);
            assertSame(fromManager, cache);
        }
    }
}
