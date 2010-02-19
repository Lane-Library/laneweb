package edu.stanford.irt.laneweb.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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
    
    public static final String BASE_PATH = "base-path";
    
    public static final String URL = "url";

    public static final String QUERY = "query";
    
    public static final String EMRID = "emrid";
    
    public static final String SUNETID = "sunetid";
    
    public static final String TICKET = "ticket";

    public static final String IPGROUP = "ipgroup";
    
    public static final String PROXY_LINKS = "proxy-links";

    public static final String LIMIT = "limit";
    
    public static final String BASSETT_NUMBER = "basset-number";
    
    public static final String REGION = "region";
    
    public static final String SUBSET = "subset";
    
    public static final String TYPE = "type";
    
    public static final String ALPHA = "alpha";
    
    public static final String MESH = "mesh";
    
    public static final String ENGINES = "engines";
    
    public static final String RESOURCES = "resources";

    public static final String FACETS = "facets";
    
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
