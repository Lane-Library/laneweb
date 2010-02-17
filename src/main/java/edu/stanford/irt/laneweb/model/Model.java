package edu.stanford.irt.laneweb.model;

/**
 * Model for sitemap components.
 * 
 * @author ceyates
 *
 * $Id$
 */
public interface Model {
    
    <T> T getObject(String name, Class<T> clazz);
    
    <T> T getObject(String name, Class<T> clazz, T defaultValue);
    
    String getString(String name);
    
    String getString(String name, String defaultValue);
}
