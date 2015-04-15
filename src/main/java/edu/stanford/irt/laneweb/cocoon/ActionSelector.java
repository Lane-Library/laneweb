package edu.stanford.irt.laneweb.cocoon;

import java.util.Map;

import edu.stanford.irt.cocoon.sitemap.select.Selector;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

/**
 * A selector that selects based on the action value in the model, possibly originating from a request parameter named
 * action.
 */
public class ActionSelector implements Selector {

    /**
     * Checks whether the expression is equal to the value of "action" in the model.
     *
     * @return true if the expression equals the value of "action" in the model.
     */
    @Override
    public boolean select(final String expression, final Map<String, Object> objectModel,
            final Map<String, String> parameters) {
        return expression.equals(ModelUtil.getString(objectModel, Model.ACTION));
    }
}
