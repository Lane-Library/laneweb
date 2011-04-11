package edu.stanford.irt.laneweb.cocoon.expression;

import java.util.Map;

import org.apache.cocoon.el.objectmodel.ObjectModelProvider;

import edu.stanford.irt.laneweb.cocoon.pipeline.LanewebEnvironment;

public class LanewebObjectModelProvider implements ObjectModelProvider {

    private Map<String, Object> objectModel;

    public Object getObject() {
        return this.objectModel;
    }

    @SuppressWarnings("unchecked")
    public void setEnvironment(final LanewebEnvironment environment) {
        this.objectModel = environment.getObjectModel();
    }
}
