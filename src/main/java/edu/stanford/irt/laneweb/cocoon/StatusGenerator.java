package edu.stanford.irt.laneweb.cocoon;

import org.apache.excalibur.store.Store;
import org.apache.excalibur.store.StoreJanitor;


public class StatusGenerator extends org.apache.cocoon.generation.StatusGenerator {
    
    public StatusGenerator(Settings settings, StoreJanitor storeJanitor, Store store) {
        this.settings = settings;
        this.storeJanitor = storeJanitor;
        this.storePersistent = store;
    }
}
