package edu.stanford.irt.laneweb.store;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.excalibur.store.Store;

public class StoreJanitor implements org.apache.excalibur.store.StoreJanitor {

    private Set<Store> stores = new HashSet<Store>();

    public Iterator<Store> iterator() {
        return this.stores.iterator();
    }

    public void register(final Store store) {
        this.stores.add(store);
    }

    public void unregister(final Store store) {
        this.stores.remove(store);
    }
}
