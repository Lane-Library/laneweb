package edu.stanford.irt.laneweb.cocoon;

import java.util.Map;

import edu.stanford.irt.cocoon.sitemap.select.Selector;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

/**
 * A Selector that returns false if the model contains any values that would make caching pointless or inefficient
 */
public class CacheableSelector implements Selector {

    /**
     * Checks to see if the model contains particular values, currently USER_ID, DEBUG, QUERY, EMRID, or the sitemap-uri
     * is /error.html or contains /bassett/, and if it does return false.
     */
    @Override
    public boolean select(final String expression, final Map<String, Object> model,
            final Map<String, String> parameters) {
        boolean result = true;
        String sitemapURI = ModelUtil.getString(model, Model.SITEMAP_URI, "");
        if (model.containsKey(Model.USER_ID)) {
            result = false;
        } else if (model.containsKey(Model.QUERY)) {
            result = false;
        } else if (sitemapURI.indexOf("/bassett/") > -1) {
            result = false;
        } else if ("/error.html".equals(sitemapURI)) {
            result = false;
        } else if (model.containsKey(Model.EMRID)) {
            result = false;
        } else if (model.containsKey(Model.DEBUG)) {
            result = false;
        }
        return result;
    }
}
