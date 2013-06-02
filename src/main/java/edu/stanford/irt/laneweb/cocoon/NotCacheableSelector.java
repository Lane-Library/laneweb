package edu.stanford.irt.laneweb.cocoon;

import java.util.Map;

import edu.stanford.irt.cocoon.sitemap.select.Selector;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

/**
 * A Selector that returns true if the model contains any values that would make caching pointless or inefficient
 */
public class NotCacheableSelector implements Selector {

    /**
     * Checks to see if the model contains particular values, currently SUNETID, DEBUG, QUERY, EMRID,
     * BASE_PATH.contains(/stage)
     */
    @Override
    public boolean select(final String expression, final Map<String, Object> model, final Map<String, String> parameters) {
        return (model.containsKey(Model.SUNETID)
                || model.containsKey(Model.QUERY)
                || model.containsKey(Model.EMRID)
                || model.containsKey(Model.DEBUG)
                || ModelUtil.getString(model, Model.BASE_PATH, "").contains("/stage"));
    }
}
