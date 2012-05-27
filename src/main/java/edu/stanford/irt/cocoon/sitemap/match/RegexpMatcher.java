package edu.stanford.irt.cocoon.sitemap.match;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.matching.AbstractRegexpMatcher;

public class RegexpMatcher extends AbstractRegexpMatcher {

    private String key;

    public RegexpMatcher(final String key) {
        this.key = key;
    }

    @Override
    protected String getMatchString(final Map model, final Parameters parameters) {
        String result = null;
        if (model.containsKey(this.key)) {
            result = model.get(this.key).toString();
            if (result.indexOf('/') == 0) {
                result = result.substring(1);
            }
        }
        return result;
    }
}
