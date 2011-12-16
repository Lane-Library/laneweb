package edu.stanford.irt.laneweb.cocoon.matching;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;

public class URLDecodingWildcardURIMatcher extends WildcardSitemapURIMatcher {

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
