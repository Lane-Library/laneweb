package edu.stanford.irt.laneweb.cocoon;

import java.util.HashMap;
import java.util.Map;

import org.apache.cocoon.el.objectmodel.ObjectModel;
import org.apache.cocoon.el.objectmodel.ObjectModelProvider;
import org.apache.commons.collections.MultiMap;

/**
 * A Cocoon ObjectModel that does nothing
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

    public void putAt(final String path, final Object value) {
    }

    public void setInitialEntries(final Map<String, ObjectModelProvider> initialEntries) {
    }

    public void setParent(final ObjectModel parentObjectModel) {
        throw new UnsupportedOperationException();
    }
}
