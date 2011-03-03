package edu.stanford.irt.laneweb.cocoon.source;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SitemapSourceLocationModifier {
    
    private final Logger log = LoggerFactory.getLogger(SitemapSourceLocationModifier.class);

    public String modify(final String location) {
        String modifiedLocation = getModifiedLocation(location);
        if (modifiedLocation != null) {
            if (log.isWarnEnabled()) {
                log.warn("the url form: " + location + "is deprecated, use the form: " + modifiedLocation + " instead.");
            }
            return modifiedLocation;
        }
        return location;
    }
    
    private String getModifiedLocation(String location) {
        if (location.indexOf("cocoon://eresources") == 0) {
            return "eresources:" + location.substring("cocoon://eresources".length());
        }
        if (location.indexOf("cocoon://apps") == 0) {
            return "apps:" + location.substring("cocoon://apps".length());
        }
        if (location.indexOf("cocoon://content") == 0) {
            return "content:" + location.substring("cocoon://content".length());
        }
        if (location.indexOf("cocoon://rss/ncbi-rss2html") == 0) {
            return "content:" + location.substring("cocoon://rss".length());
        }
        if (location.indexOf("cocoon://rss") == 0) {
            return "rss:" + location.substring("cocoon://rss".length());
        }
        if (location.indexOf("cocoon:") == 0) {
            return "content:" + location.substring("cocoon:".length());
        }
        return null;
    }
}
