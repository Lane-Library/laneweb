package edu.stanford.irt.laneweb.cocoon;

import java.net.URI;
import java.net.URISyntaxException;

import edu.stanford.irt.laneweb.LanewebException;

public class LocationModifier {

    public URI modify(final URI location) throws URISyntaxException {
        String scheme = location.getScheme();
        String authority = location.getAuthority();
        String path = location.getPath();
        String query = location.getQuery();
        URI result = null;
        if ("cocoon".equals(scheme) && authority == null) {
            result = new URI("cocoon://content" + path);
        } else if ("content".equals(scheme)) {
            result = new URI("cocoon://content" + path);
        } else if ("eresources".equals(scheme)) {
            result = new URI("cocoon://eresources" + path);
        } else if ("classes".equals(scheme)) {
            result = new URI("cocoon://classes" + path);
        } else if ("apps".equals(scheme)) {
            result = new URI("cocoon://apps" + path);
        } else if ("mobile".equals(scheme)) {
            result = new URI("cocoon://mobile" + path);
        } else if ("rss".equals(scheme)) {
            result = new URI("cocoon://rss" + path);
        }
        
        if (query != null && result != null) {
            throw new LanewebException("need to add query ?" + query + " to uri " + result);
        }
        return result == null ? location : result;
    }
}
