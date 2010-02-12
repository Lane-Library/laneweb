package edu.stanford.irt.laneweb.model;

import java.util.Map;

import org.apache.cocoon.el.objectmodel.ObjectModel;

/**
 * Default implementation of {@link:ObjectModelAware}.  Extending this allows a bean
 * to get values of various type, not just String and primitives as from Parameters.
 * 
 * @author ceyates
 *
 * $Id$
 */
public class AbstractObjectModelAware implements ObjectModelAware {
    
    private Map model;

    public <T> T getObject(String name, Class<T> clazz) {
        return (T) this.model.get(name);
    }

    public <T> T getObject(String name, Class<T> clazz, T defaultValue) {
        if (!model.containsKey(name)) {
            return defaultValue;
        }
        return getObject(name, clazz);
    }

    public void setObjectModel(ObjectModel objectModel) {
        this.model = (Map) objectModel.get("laneweb");
    }
    
    public String getString(String name) {
        return getObject(name, String.class);
    }
    
    public String getString(String name, String defaultValue) {
        return getObject(name, String.class, defaultValue);
    }
}
