package edu.stanford.irt.laneweb.servlet.redirect;

public interface RedirectProcessor {

    String getRedirectURL(final String uri, String basePath, String queryString);
}