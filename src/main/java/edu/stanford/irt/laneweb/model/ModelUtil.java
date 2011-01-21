package edu.stanford.irt.laneweb.model;

import java.util.Map;

/**
 * Provides type casting and default values when accessing map values
 * 
 * @author ceyates $Id: ModelUtil.java 80764 2010-12-15 21:47:39Z
 *         ceyates@stanford.edu $
 */
public abstract class ModelUtil {

    @SuppressWarnings("unchecked")
    public static <T> T getObject(final Map<String, Object> map, final String name, final Class<T> clazz) {
        return (T) map.get(name);
    }

    public static <T> T getObject(final Map<String, Object> map, final String name, final Class<T> clazz, final T defaultValue) {
        if (!map.containsKey(name)) {
            return defaultValue;
        }
        return getObject(map, name, clazz);
    }

    public static String getString(final Map<String, Object> map, final String name) {
        return getObject(map, name, String.class);
    }

    public static String getString(final Map<String, Object> map, final String name, final String defaultValue) {
        return getObject(map, name, String.class, defaultValue);
    }
}
