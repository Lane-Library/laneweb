package edu.stanford.irt.cocoon.sitemap.match;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;

public class URLDecodingMatcher extends WildcardMatcher {

    public URLDecodingMatcher(final String key) {
        super(key);
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected String getMatchString(final Map objectModel, final Parameters parameters) {
        try {
            return URLDecoder.decode(super.getMatchString(objectModel, parameters), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
