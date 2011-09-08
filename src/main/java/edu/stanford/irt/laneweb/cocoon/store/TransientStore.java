package edu.stanford.irt.laneweb.cocoon.store;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.excalibur.store.Store;

public class TransientStore implements Store {

    private Map<Object, Object> map = Collections.synchronizedMap(new HashMap<Object, Object>());

    public void clear() {
        this.map.clear();
    }

    public boolean containsKey(final Object key) {
        return this.map.containsKey(key);
    }

    public void free() {
        this.map.clear();
    }

    public Object get(final Object key) {
        return this.map.get(key);
    }

    public Enumeration<Object> keys() {
    	final Iterator<Object> iterator = this.map.keySet().iterator();
    	return new Enumeration<Object>() {

			@Override
			public boolean hasMoreElements() {
				return iterator.hasNext();
			}

			@Override
			public Object nextElement() {
				return iterator.next();
			}
    		
    	};
    }

    public void remove(final Object key) {
        this.map.remove(key);
    }

    public int size() {
        return this.map.size();
    }

    public void store(final Object key, final Object value) throws IOException {
        this.map.put(key, value);
    }
}
