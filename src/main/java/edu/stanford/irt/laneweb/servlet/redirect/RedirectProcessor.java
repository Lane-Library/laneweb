package edu.stanford.irt.laneweb.servlet.redirect;

public interface RedirectProcessor {

    public String getRedirectURL(final String uri, String basePath, String queryString);
}