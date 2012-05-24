package edu.stanford.irt.cocoon.pipeline;

import java.util.Map;

/**
 * ModelAware pipline components are aware of the model.
 */
public interface ModelAware {

    /**
     * Set the model
     * 
     * @param model
     */
    void setModel(Map<String, Object> model);
}
