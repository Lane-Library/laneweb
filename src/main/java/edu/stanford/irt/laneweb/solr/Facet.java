package edu.stanford.irt.laneweb.solr;

public class Facet {

    private long count;

    private boolean enabled;

    private String name;

    private String url;

    public Facet(final String name, final long count, final boolean enabled, final String facetUrl) {
        this.name = name;
        this.count = count;
        this.enabled = enabled;
        this.url = facetUrl;
    }

    public long getCount() {
        return this.count;
    }

    public String getName() {
        return this.name;
    }

    public String getUrl() {
        return this.url;
    }

    public boolean isEnabled() {
        return this.enabled;
    }
}
