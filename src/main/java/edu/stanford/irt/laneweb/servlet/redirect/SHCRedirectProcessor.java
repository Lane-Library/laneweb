package edu.stanford.irt.laneweb.servlet.redirect;

import java.util.Map;

public class SHCRedirectProcessor extends DefaultRedirectProcessor {

    public SHCRedirectProcessor(Map<String, String> redirectMap) {
        super(redirectMap);
    }

    @Override
    public String getRedirectURL(final String uri, final String basePath, final String queryString) {
        if (uri.indexOf("/shc") != 0) {
            return null;
        }
        return super.getRedirectURL(uri, basePath, queryString);
    }
}
