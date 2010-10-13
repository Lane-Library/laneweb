package edu.stanford.irt.laneweb.model;

import java.util.Map;

/**
 * Implementing this gives SitemapComponent beans access to the Model. This allows them to get values of various type,
 * not just String and primitives as from Parameters.
 * 
 * @author ceyates $Id$
 */
public interface ModelAware {
    
    public <T> T getObject(Map<String, Object> map, String name, Class<T> clazz);

    public <T> T getObject(Map<String, Object> map, String name, Class<T> clazz, T defaultValue);
    
    public String getString(Map<String, Object> map, String name);
    
    public String getString(Map<String, Object> map, String name, String defaultValue);
}
