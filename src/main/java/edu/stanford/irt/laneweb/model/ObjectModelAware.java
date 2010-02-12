package edu.stanford.irt.laneweb.model;

import org.apache.cocoon.el.objectmodel.ObjectModel;

/**
 * Implementing this gives SitemapComponent beans access to the ObjectModel.  This allows them
 * to get values of various type, not just String and primitives as from Parameters.
 * 
 * @author ceyates
 *
 * $Id$
 */
public interface ObjectModelAware {
    
    void setObjectModel(ObjectModel objectModel);
    
    <T> T getObject(String name, Class<T> clazz);
    
    <T> T getObject(String name, Class<T> clazz, T defaultValue);
    
    String getString(String name);
    
    String getString(String name, String defaultValue);
}
