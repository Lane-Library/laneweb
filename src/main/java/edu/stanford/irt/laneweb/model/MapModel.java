package edu.stanford.irt.laneweb.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.cocoon.el.objectmodel.ObjectModel;

/**
 * Default implementation of {@link:Model}.
 * 
 * @author ceyates
 *
 * $Id$
 */
public class MapModel extends HashMap<String, Object> implements Model {
    
    public MapModel(ObjectModel objectModel) {
        super.putAll((Map<? extends String, ? extends Object>) objectModel.get("laneweb"));
    }

    public <T> T getObject(String name, Class<T> clazz) {
        return (T) get(name);
    }

    public <T> T getObject(String name, Class<T> clazz, T defaultValue) {
        if (!containsKey(name)) {
            return defaultValue;
        }
        return getObject(name, clazz);
    }
    
    public String getString(String name) {
        return getObject(name, String.class);
    }
    
    public String getString(String name, String defaultValue) {
        return getObject(name, String.class, defaultValue);
    }
}
