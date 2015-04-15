package edu.stanford.irt.laneweb.cocoon;

import java.io.Serializable;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import edu.stanford.irt.cocoon.cache.Cache;
import edu.stanford.irt.cocoon.cache.CachedResponse;

public class EHCache implements Cache {

    private net.sf.ehcache.Cache cache;

    public EHCache(final CacheManager manager) {
        this.cache = manager.getCache("cocoon-ehcache");
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
        Element element = this.cache.get(key);
        return (CachedResponse) (element == null ? null : element.getObjectValue());
    }

    @Override
    public void remove(final Serializable key) {
        this.cache.remove(key);
    }

    @Override
    public void store(final Serializable key, final CachedResponse response) {
        final Element element = new Element(key, response);
        this.cache.put(element);
    }
}
