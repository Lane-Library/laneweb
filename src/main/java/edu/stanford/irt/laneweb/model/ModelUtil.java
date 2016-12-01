package edu.stanford.irt.laneweb.model;

import java.util.Map;

/**
 * Provides type casting and default values when accessing map values
 */
public class ModelUtil {

    private ModelUtil() {
        // empty private default constructor
    }

    public static <T> T getObject(final Map<String, Object> map, final String name, final Class<T> clazz) {
        return clazz.cast(map.get(name));
    }

    public static <T> T getObject(final Map<String, Object> map, final String name, final Class<T> clazz,
            final T defaultValue) {
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
