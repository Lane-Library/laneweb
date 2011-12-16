package edu.stanford.irt.laneweb.cocoon.matching;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.matching.AbstractWildcardMatcher;

import edu.stanford.irt.laneweb.model.Model;

public class WildcardSitemapURIMatcher extends AbstractWildcardMatcher {

    @Override
    protected String getMatchString(final Map objectModel, final Parameters parameters) {
        String result = objectModel.get(Model.SITEMAP_URI).toString();
        if (result.indexOf('/') == 0) {
            result = result.substring(1);
        }
        return result;
    }
}
