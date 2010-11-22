package edu.stanford.irt.laneweb.util;

import java.util.Map;

/**
 * Provides type casting and default values when accessing map values
 * 
 * @author ceyates $Id$
 */
public abstract class ModelUtil {

    @SuppressWarnings("unchecked")
    public static <T> T getObject(Map<String, Object> map, String name, Class<T> clazz) {
        return (T) map.get(name);
    }

    public static <T> T getObject(Map<String, Object> map, String name, Class<T> clazz, T defaultValue) {
        if (!map.containsKey(name)) {
            return defaultValue;
        }
        return getObject(map, name, clazz);
    }
    
    public static String getString(Map<String, Object> map, String name) {
        return getObject(map, name, String.class);
    }
    
    public static String getString(Map<String, Object> map, String name, String defaultValue) {
        return getObject(map, name, String.class, defaultValue);
    }
}
