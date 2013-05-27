package edu.stanford.irt.laneweb.cocoon;

import java.net.URI;
import java.net.URISyntaxException;

public class LocationModifier {

    public URI modify(final URI location) throws URISyntaxException {
        String scheme = location.getScheme();
        String authority = location.getAuthority();
        if (scheme == null || (authority != null && location.getScheme().equals("cocoon"))) {
            return location;
        }
        if ("content".equals(scheme)) {
            return new URI("cocoon://content" + location.getPath());
        }
        if ("classes".equals(scheme)) {
            return new URI("cocoon://classes" + location.getPath());
        }
        if ("cocoon".equals(scheme)) {
            return new URI("cocoon://content" + location.getPath());
        }
        URI modifiedLocation = getModifiedLocation(location.toASCIIString());
        return modifiedLocation == null ? location : modifiedLocation;
    }

    private URI getModifiedLocation(final String location) throws URISyntaxException {
        if (location.indexOf("cocoon://eresources") == 0) {
            return new URI("eresources:" + location.substring("cocoon://eresources".length()));
        }
        if (location.indexOf("cocoon://apps") == 0) {
            return new URI("apps:" + location.substring("cocoon://apps".length()));
        }
        if (location.indexOf("cocoon://content") == 0) {
            return new URI("content:" + location.substring("cocoon://content".length()));
        }
        if (location.indexOf("cocoon://rss/ncbi-rss2html") == 0) {
            return new URI("content:" + location.substring("cocoon://rss".length()));
        }
        if (location.indexOf("cocoon://rss") == 0) {
            return new URI("rss:" + location.substring("cocoon://rss".length()));
        }
        if (location.indexOf("cocoon:") == 0) {
            return new URI("content:" + location.substring("cocoon:".length()));
        }
        return null;
    }
}
