package edu.stanford.irt.cocoon.pipeline;

import java.util.Map;

/**
 * ParametersAware are aware of sitemap parameters
 */
public interface ParametersAware {

    /**
     * Set the parameters
     * 
     * @param parameters
     */
    void setParameters(Map<String, String> parameters);
}
