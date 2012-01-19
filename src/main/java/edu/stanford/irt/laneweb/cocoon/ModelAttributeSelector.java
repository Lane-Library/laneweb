package edu.stanford.irt.laneweb.cocoon;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.selection.Selector;

/**
 * A Selector that returns <code>true</code when the configured
 * <code>attributeName</code> is present in the model, regardless of its value.
 */
public class ModelAttributeSelector implements Selector {

    /** the name of the model attribute */
    private String attributeName;

    /**
     * @return <code>true</code> if the configured name is a key in the model.
     */
    @SuppressWarnings("rawtypes")
    public boolean select(final String expression, final Map model, final Parameters parameters) {
        return model.containsKey(this.attributeName);
    }

    /**
     * The Setter for the <code>attributeName</code> value.
     * 
     * @param attributeName
     */
    public void setAttributeName(final String attributeName) {
        this.attributeName = attributeName;
    }
}
