package edu.stanford.irt.laneweb.store;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.apache.excalibur.store.Store;
import org.apache.excalibur.store.StoreJanitor;

public class TransientStore implements Store {
    
    private Map<Object, Object> map = Collections.synchronizedMap(new HashMap<Object, Object>());

    public void clear() {
        this.map.clear();
    }

    public boolean containsKey(Object key) {
        return this.map.containsKey(key);
    }

    public void free() {
        this.map.clear();
    }

    public Object get(Object key) {
        return this.map.get(key);
    }

    public Enumeration<Object> keys() {
        Vector<Object> v = new Vector<Object>(map.keySet());
        return v.elements();
    }

    public void remove(Object key) {
        this.map.remove(key);
    }

    public int size() {
        return this.map.size();
    }

    public void store(Object key, Object value) throws IOException {
        this.map.put(key, value);
    }
    
    public void setStoreJanitor(final StoreJanitor storeJanitor) {
        storeJanitor.register(this);
    }

}
