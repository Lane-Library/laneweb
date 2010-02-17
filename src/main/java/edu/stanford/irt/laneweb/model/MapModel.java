package edu.stanford.irt.laneweb.model;

import java.util.Map;

/**
 * Default implementation of {@link:Model}.
 * 
 * @author ceyates
 *
 * $Id$
 */
public class MapModel implements Model {
    
    private Map<String, Object> map;
    
    public MapModel(Map<String, Object> map) {
        this.map = map;
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
