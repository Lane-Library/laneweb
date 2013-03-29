package edu.stanford.irt.laneweb.servlet.redirect;

public class TrailingSlashRedirectProcessor implements RedirectProcessor {

    public String getRedirectURL(final String uri, final String basePath, final String queryString) {
        if (uri.endsWith("/")) {
            StringBuilder sb = new StringBuilder(basePath).append(uri).append("index.html");
            if (queryString != null) {
                sb.append('?').append(queryString);
            }
            return sb.toString();
        }
        return null;
    }
}
