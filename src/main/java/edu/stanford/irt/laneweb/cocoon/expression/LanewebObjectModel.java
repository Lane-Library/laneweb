package edu.stanford.irt.laneweb.cocoon.expression;

import java.util.HashMap;
import java.util.Map;

import org.apache.cocoon.el.objectmodel.ObjectModel;
import org.apache.cocoon.el.objectmodel.ObjectModelProvider;
import org.apache.commons.collections.MultiMap;

/**
 * A simplified version of the Cocoon ObjectModel, doesn't keep track of a bunch
 * of stuff. But it works
 */
@SuppressWarnings({ "rawtypes", "serial" })
public class LanewebObjectModel extends HashMap implements ObjectModel {

    public void cleanupLocalContext() {
    }

    public void fillContext() {
        throw new UnsupportedOperationException();
    }

    public MultiMap getAll() {
        throw new UnsupportedOperationException();
    }

    public void markLocalContext() {
    }

    @SuppressWarnings("unchecked")
    public void putAt(final String path, final Object value) {
        put(path, value);
    }

    @SuppressWarnings("unchecked")
    public void setInitialEntries(final Map<String, ObjectModelProvider> initialEntries) {
        for (Entry<String, ObjectModelProvider> entry : initialEntries.entrySet()) {
            put(entry.getKey(), entry.getValue().getObject());
        }
    }

    public void setParent(final ObjectModel parentObjectModel) {
        throw new UnsupportedOperationException();
    }
}
