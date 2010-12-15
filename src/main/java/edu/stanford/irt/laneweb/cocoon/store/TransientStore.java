package edu.stanford.irt.laneweb.cocoon.store;

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
        Vector<Object> vector = new Vector<Object>(this.map.keySet());
        return vector.elements();
    }

    public void remove(final Object key) {
        this.map.remove(key);
    }

    public void setStoreJanitor(final StoreJanitor storeJanitor) {
        storeJanitor.register(this);
    }

    public int size() {
        return this.map.size();
    }

    public void store(final Object key, final Object value) throws IOException {
        this.map.put(key, value);
    }
}
