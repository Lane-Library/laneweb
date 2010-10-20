package edu.stanford.irt.laneweb.servlet.redirect;

public class SHCRedirectProcessor extends DefaultRedirectProcessor {

    private static final String NEW_PAGE_BASE_URL = "/newpage.html?page=";

    @Override
    public String getRedirectURL(final String uri, final String basePath, final String queryString) {
        if (uri.indexOf("/shc") != 0) {
            return null;
        }
        String redirectURL = super.getRedirectURL(uri, basePath, queryString);
        // add basePath to the page parameter of the newpage.html url
        int newPage = redirectURL.indexOf(NEW_PAGE_BASE_URL);
        if (newPage == 0) {
            redirectURL = NEW_PAGE_BASE_URL + basePath + redirectURL.substring(NEW_PAGE_BASE_URL.length());
        }
        return redirectURL;
    }
}
