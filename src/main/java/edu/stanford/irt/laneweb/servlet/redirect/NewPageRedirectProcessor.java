package edu.stanford.irt.laneweb.servlet.redirect;


public class NewPageRedirectProcessor extends DefaultRedirectProcessor {

    private static final String NEW_PAGE_BASE_URL = "/newpage.html?page=";

    @Override
    public String getRedirectURL(String uri, String basePath, String queryString) {
        String redirectURL = super.getRedirectURL(uri, basePath, queryString);
        if (redirectURL != null) {
            // add basePath to the page parameter of the newpage.html url
            int newPage = redirectURL.indexOf(NEW_PAGE_BASE_URL);
            if (newPage == 0) {
                redirectURL = basePath + NEW_PAGE_BASE_URL + basePath + redirectURL.substring(NEW_PAGE_BASE_URL.length());
            }
        }
        return redirectURL;
    }
}
