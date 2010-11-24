package edu.stanford.irt.laneweb.cocoon.source;

public class SitemapSourceLocationModifier {

    public String modify(String location) {
        String modifiedLocation = location;
        // remove double slash:
        if (modifiedLocation.indexOf("cocoon://") == 0) {
            modifiedLocation = "cocoon:/" + modifiedLocation.substring("cocoon://".length());
        }
        if (modifiedLocation.indexOf("cocoon:/cached/search/") == 0) {
            modifiedLocation = "cocoon:/content/search/" + modifiedLocation.substring("cocoon:/content/search/".length());
        } else if (modifiedLocation.indexOf(".html") > 0 && modifiedLocation.indexOf("cocoon:/content") != 0) {
            modifiedLocation = "cocoon:/content/" + modifiedLocation.substring("cocoon:/".length());
        }
        return modifiedLocation;
    }
}
