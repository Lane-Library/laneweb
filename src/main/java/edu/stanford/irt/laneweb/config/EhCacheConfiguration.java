package edu.stanford.irt.laneweb.config;

import java.net.URISyntaxException;

import javax.cache.Cache;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;

import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.stanford.irt.cocoon.cache.CachedResponse;

@Configuration

public class EhCacheConfiguration {

    public static final String COCOON_CACHE = "cocoon-cache";

    @Bean(destroyMethod = "close")
    public javax.cache.CacheManager jCacheManager() {
        CachingProvider provider = Caching.getCachingProvider("org.ehcache.jsr107.EhcacheCachingProvider");
        javax.cache.CacheManager jCacheManager = provider.getCacheManager();

        CacheConfiguration<java.io.Serializable, CachedResponse> ehcacheConfig = CacheConfigurationBuilder
                .newCacheConfigurationBuilder(
                        java.io.Serializable.class,
                        CachedResponse.class,
                        ResourcePoolsBuilder.newResourcePoolsBuilder().heap(1000, EntryUnit.ENTRIES))
                .build();

        javax.cache.configuration.Configuration<java.io.Serializable, CachedResponse> jcacheConfig = Eh107Configuration
                .fromEhcacheCacheConfiguration(ehcacheConfig);

        jCacheManager.createCache(COCOON_CACHE, jcacheConfig);

        // Equivalent to <jsr107:mbeans enable-management="true" enable-statistics="true"/>
        jCacheManager.enableManagement(COCOON_CACHE, true);
        jCacheManager.enableStatistics(COCOON_CACHE, true);

        return jCacheManager;
    }

    @Bean
    Cache<java.io.Serializable, CachedResponse> cache() throws URISyntaxException {
        return jCacheManager().getCache(COCOON_CACHE, java.io.Serializable.class, CachedResponse.class);
    }

}
