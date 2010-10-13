package edu.stanford.irt.laneweb.model;

import java.util.Map;

/**
 * Default implementation of {@link:ModelAware}. Extending this allows a bean to get values of various type, not just
 * String and primitives as from Parameters.
 * 
 * @author ceyates $Id$
 */
public abstract class DefaultModelAware implements ModelAware {

    protected Map<String, Object> model;

    @SuppressWarnings("unchecked")
    public <T> T getObject(Map<String, Object> map, String name, Class<T> clazz) {
        return (T) map.get(name);
    }

    public <T> T getObject(Map<String, Object> map, String name, Class<T> clazz, T defaultValue) {
        if (!map.containsKey(name)) {
            return defaultValue;
        }
        return getObject(map, name, clazz);
    }
    
    public String getString(Map<String, Object> map, String name) {
        return getObject(map, name, String.class);
    }
    
    public String getString(Map<String, Object> map, String name, String defaultValue) {
        return getObject(map, name, String.class, defaultValue);
    }
}
