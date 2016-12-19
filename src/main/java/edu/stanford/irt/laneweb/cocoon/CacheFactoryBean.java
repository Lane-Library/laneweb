package edu.stanford.irt.laneweb.cocoon;

import java.io.Serializable;

import javax.cache.Cache;
import javax.cache.CacheManager;

import org.springframework.beans.factory.FactoryBean;

import edu.stanford.irt.cocoon.cache.CachedResponse;

public class CacheFactoryBean implements FactoryBean<Cache<Serializable, CachedResponse>> {

    private Cache<Serializable, CachedResponse> cache;

    public CacheFactoryBean(final CacheManager cacheManager) {
        this.cache = cacheManager.getCache("cocoon-cache", Serializable.class, CachedResponse.class);
    }

    @Override
    public Cache<Serializable, CachedResponse> getObject() {
        return this.cache;
    }

    @Override
    public Class<?> getObjectType() {
        return Cache.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
