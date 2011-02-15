package edu.stanford.irt.laneweb.cocoon;

import org.apache.excalibur.store.Store;

public class StatusGenerator extends org.apache.cocoon.generation.StatusGenerator {

    public StatusGenerator(final Settings settings, final Store store) {
        this.settings = settings;
        this.storePersistent = store;
    }
}
