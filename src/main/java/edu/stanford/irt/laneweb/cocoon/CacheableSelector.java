package edu.stanford.irt.laneweb.cocoon;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.irt.cocoon.sitemap.select.Selector;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

/**
 * A Selector that returns false if the model contains any values that would make caching pointless or inefficient
 */
public class CacheableSelector implements Selector {
    
    private Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Checks to see if the model contains particular values, currently SUNETID, DEBUG, QUERY, EMRID,
     * BASE_PATH.contains(/stage)
     */
    @Override
    public boolean select(final String expression, final Map<String, Object> model, final Map<String, String> parameters) {
//        boolean result =  (model.containsKey(Model.SUNETID)
//                || model.containsKey(Model.QUERY)
//                || model.containsKey(Model.EMRID)
//                || "/error.html".equals(ModelUtil.getString(model, Model.SITEMAP_URI))
//                || model.containsKey(Model.DEBUG)
//                || ModelUtil.getString(model, Model.BASE_PATH, "").contains("/stage"));
        String reason = getReason(model);
        boolean result = "".equals(reason);
        this.log.info(new StringBuilder().append(result).append(reason).append(':').append(model).toString());
        return result;
    }
    
    private String getReason(Map<String, Object> model) {
        String result = "";
        if (model.containsKey(Model.SUNETID)) {
            result = ":sunetid";
        } else if (model.containsKey(Model.QUERY)) {
            result = ":query";
        } else if ("/error.html".equals(ModelUtil.getString(model, Model.SITEMAP_URI))) {
            result = ":/error.html";
        } else if (model.containsKey(Model.EMRID)) {
            result = ":emrid";
        } else if (model.containsKey(Model.DEBUG)) {
            result = ":debug";
        } else if ( ModelUtil.getString(model, Model.BASE_PATH, "").contains("/stage")) {
            result = ":/stage";
        }
        return result;
    }
}
