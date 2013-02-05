package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.eresources.Eresource;



public aspect CollectionManagerCache {
    
    private Map<CacheKey, Object> cache = new HashMap<CacheKey, Object>();
    
    pointcut browseType() :
        call (Collection<Eresource> CollectionManager.getType(String, char));
    
    Object around() : browseType() {
        CacheKey key = new CacheKey((String) thisJoinPoint.getArgs()[0], ((Character) thisJoinPoint.getArgs()[1]).charValue());
        Object value = this.cache.get(key);
        if (value == null) {
            value = proceed();
            this.cache.put(key, value);
        }
        return value;
    }

    class CacheKey {
        String type;
        char alpha;
        CacheKey(final String type, final char alpha) {
            this.type = type;
            this.alpha = alpha;
        }
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof CacheKey)) {
                return false;
            }
            CacheKey other = (CacheKey) o;
            return this.alpha == other.alpha && this.type.equals(other.type);
        }
        @Override
        public int hashCode() {
            return this.type.hashCode();
        }
        @Override
        public String toString() {
            return this.type + ":" + this.alpha;
        }
    }
}
