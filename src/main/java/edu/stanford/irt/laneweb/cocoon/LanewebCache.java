package edu.stanford.irt.laneweb.cocoon;

import java.io.Serializable;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.cocoon.caching.CachedResponse;

public class LanewebCache implements org.apache.cocoon.caching.Cache {

    private Cache cache;
    
    private CacheManager manager;
    
    public LanewebCache(CacheManager manager) {
        this.manager = manager;
        this.cache = manager.getCache("cocoon-ehcache");
    }

    public void clear() {
        this.cache.removeAll();
    }

    public boolean containsKey(final Serializable key) {
        return this.cache.get(key) != null;
    }
    
    public void destroy() {
        this.manager.shutdown();
    }

    public CachedResponse get(final Serializable key) {
    	Element element = this.cache.get(key);
    	return (CachedResponse) (element == null ? null : element.getValue());
    }

    public void remove(final Serializable key) {
        this.cache.remove(key);
    }

    public void store(final Serializable key, final CachedResponse response){
        final Element element = new Element(key, response);
        this.cache.put(element);
    }
}
