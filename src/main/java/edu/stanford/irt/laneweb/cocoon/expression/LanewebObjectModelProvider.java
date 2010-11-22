package edu.stanford.irt.laneweb.cocoon.expression;

import java.util.Map;

import org.apache.cocoon.el.objectmodel.ObjectModelProvider;

public class LanewebObjectModelProvider implements ObjectModelProvider {

    private Map<String, Object> objectModel;

    public Object getObject() {
        return this.objectModel;
    }
    
    public void setObjectModel(Map<String, Object> objectModel) {
        this.objectModel = objectModel;
    }
}
