package edu.stanford.irt.laneweb.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.cocoon.el.objectmodel.ObjectModel;
import org.apache.cocoon.el.objectmodel.ObjectModelProvider;
import org.apache.commons.collections.MultiMap;

/**
 * A simplified version of the Cocoon ObjectModel, doesn't keep track of a bunch of stuff.  But it works
 * @author ceyates
 *
 * $Id$
 */
public class LanewebObjectModel extends HashMap implements ObjectModel {
    
    public void setInitialEntries(Map<String, ObjectModelProvider> initialEntries) {
        for (Entry<String, ObjectModelProvider> entry : initialEntries.entrySet()) {
            put(entry.getKey(), entry.getValue().getObject());
        }
    }

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

    public void putAt(String path, Object value) {
        put(path, value);
    }

    public void setParent(ObjectModel parentObjectModel) {
        throw new UnsupportedOperationException();
    }
}
