package edu.stanford.irt.laneweb.servlet.redirect;

/**
 * computes a redirect for requests that end in a filename without a dot, appending '/index.html'
 * to the end, excepting (/secure|)/apps/**, /m/**, /eresources/** and /rss/**.
 * IMPORTANT: should come after the DefaultRedirectProcessor
 * (the one with the regular expressions) because that one may have some none-dot urls (page2rss, etc).
 * Likewise for TrailingSlashRedirectProcessor
 * 
 * @author ceyates
 *
 */
public class NoDotRedirectProcessor implements RedirectProcessor {

    public String getRedirectURL(final String uri, final String basePath, final String queryString) {
        String result = null;
        if (uri.indexOf("/apps/") == -1 && uri.indexOf("/m/") != 0 && uri.indexOf("/eresources/") != 0 && uri.indexOf("/rss/") != 0) {
            String last = uri.substring(uri.lastIndexOf('/') + 1);
            if (last.indexOf('.') == -1) {
                result = uri + "/index.html";
                result = basePath == null ? result : basePath + result;
                result = queryString == null ? result : result + '?' + queryString;
            }
        }
        return result;
    }
}
