package edu.stanford.irt.laneweb.model;

import java.util.Map;

import org.apache.cocoon.el.objectmodel.ObjectModel;

/**
 * Default implementation of {@link:Model}.
 * 
 * @author ceyates
 *
 * $Id$
 */
public class MapModel implements Model {
    
    private Map<String, Object> map;
    
    public MapModel(ObjectModel objectModel) {
        this.map = (Map<String, Object>) objectModel.get("laneweb");
    }

    public <T> T getObject(String name, Class<T> clazz) {
        return (T) this.map.get(name);
    }

    public <T> T getObject(String name, Class<T> clazz, T defaultValue) {
        if (!map.containsKey(name)) {
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
