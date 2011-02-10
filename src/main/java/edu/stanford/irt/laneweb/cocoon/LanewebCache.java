package edu.stanford.irt.laneweb.cocoon;

import java.io.IOException;
import java.io.Serializable;

import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.caching.Cache;
import org.apache.cocoon.caching.CachedResponse;
import org.apache.excalibur.store.Store;

/**
 * implemented this because the default implementation does excessive logging.
 * @author ceyates
 *
 */
public class LanewebCache implements Cache {

    protected Store store;

    public void clear() {
        this.store.clear();
    }

    public boolean containsKey(final Serializable key) {
        return this.store.containsKey(key);
    }

    public CachedResponse get(final Serializable key) {
        return (CachedResponse) this.store.get(key);
    }

    public void remove(final Serializable key) {
        this.store.remove(key);
    }

    public void setStore(final Store store) {
        this.store = store;
    }

    public void store(final Serializable key, final CachedResponse response) throws ProcessingException {
        try {
            this.store.store(key, response);
        } catch (IOException e) {
            throw new ProcessingException(e);
        }
    }
}
