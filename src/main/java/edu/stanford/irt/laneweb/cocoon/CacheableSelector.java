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
        String sitemapURI = ModelUtil.getString(model, Model.SITEMAP_URI, "");
        return !model.containsKey(Model.USER_ID)
                && !model.containsKey(Model.QUERY)
                && !sitemapURI.contains("/bassett/")
                && !"/error.html".equals(sitemapURI)
                && !model.containsKey(Model.EMRID)
                && !model.containsKey(Model.DEBUG);
    }
}
