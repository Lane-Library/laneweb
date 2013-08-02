package edu.stanford.irt.laneweb.cocoon;

import java.util.Map;

import org.slf4j.Logger;

import edu.stanford.irt.cocoon.sitemap.select.Selector;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

/**
 * A Selector that returns false if the model contains any values that would make caching pointless or inefficient
 */
public class CacheableSelector implements Selector {
    
    private Logger log;
    
    public CacheableSelector(Logger log) {
        this.log = log;
    }

    /**
     * Checks to see if the model contains particular values, currently SUNETID, DEBUG, QUERY, EMRID,
     * BASE_PATH.contains(/stage)
     */
    @Override
    public boolean select(final String expression, final Map<String, Object> model, final Map<String, String> parameters) {
        String reason = getReason(model);
        boolean result = "".equals(reason);
        this.log.info(new StringBuilder().append(result).append(reason).append(':').append(model).toString());
        return result;
    }
    
    private String getReason(Map<String, Object> model) {
        String result = "";
        String sitemapURI = ModelUtil.getString(model, Model.SITEMAP_URI, "");
        if (model.containsKey(Model.SUNETID)) {
            result = ":sunetid";
        } else if (model.containsKey(Model.QUERY)) {
            result = ":query";
        } else if (sitemapURI.indexOf("/bassett/") > -1) {
            result = ":bassett";
        } else if ("/error.html".equals(sitemapURI)) {
            result = ":error";
        } else if (model.containsKey(Model.EMRID)) {
            result = ":emrid";
        } else if (model.containsKey(Model.DEBUG)) {
            result = ":debug";
        } else if (ModelUtil.getString(model, Model.BASE_PATH, "").contains("/stage")) {
            result = ":stage";
        }
        return result;
    }
}
