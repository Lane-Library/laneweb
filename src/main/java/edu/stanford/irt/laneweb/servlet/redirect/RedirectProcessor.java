package edu.stanford.irt.laneweb.servlet.redirect;

@FunctionalInterface
public interface RedirectProcessor {

    String getRedirectURL(final String uri, String basePath, String queryString);
}
