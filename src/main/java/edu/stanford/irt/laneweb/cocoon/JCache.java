package edu.stanford.irt.laneweb.cocoon;

import java.io.Serializable;

import javax.cache.Cache;
import javax.cache.CacheManager;

import edu.stanford.irt.cocoon.cache.CachedResponse;

public class JCache implements edu.stanford.irt.cocoon.cache.Cache {

    private Cache<Serializable, CachedResponse> cache;

    public JCache(final CacheManager manager) {
        this.cache = manager.getCache("cocoon-cache", Serializable.class, CachedResponse.class);
    }

    @Override
    public void clear() {
        this.cache.removeAll();
    }

    @Override
    public boolean containsKey(final Serializable key) {
        return this.cache.get(key) != null;
    }

    @Override
    public CachedResponse get(final Serializable key) {
        return this.cache.get(key);
    }

    @Override
    public void remove(final Serializable key) {
        this.cache.remove(key);
    }

    @Override
    public void store(final Serializable key, final CachedResponse response) {
        this.cache.put(key, response);
    }
}
