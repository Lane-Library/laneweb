package edu.stanford.irt.laneweb.servlet.redirect;

public class SHCRedirectProcessor extends DefaultRedirectProcessor {

    @Override
    public String getRedirectURL(final String uri, final String basePath, final String queryString) {
        if (uri.indexOf("/shc") != 0) {
            return null;
        }
        String redirectURL = super.getRedirectURL(uri, basePath, queryString);
        return redirectURL;
    }
}
