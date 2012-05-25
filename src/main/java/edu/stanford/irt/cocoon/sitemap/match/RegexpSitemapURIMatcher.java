package edu.stanford.irt.cocoon.sitemap.match;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.matching.AbstractRegexpMatcher;

import edu.stanford.irt.laneweb.model.Model;

public class RegexpSitemapURIMatcher extends AbstractRegexpMatcher {

    @SuppressWarnings("rawtypes")
    @Override
    protected String getMatchString(final Map objectModel, final Parameters parameters) {
        String result = objectModel.get(Model.SITEMAP_URI).toString();
        if (result.indexOf('/') == 0) {
            result = result.substring(1);
        }
        return result;
    }
}
