package edu.stanford.irt.laneweb.bookmarks;

import java.io.Serializable;

import org.codehaus.jackson.map.annotate.JsonDeserialize;

public class Bookmark implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonDeserialize
    private String label;

    @JsonDeserialize
    private String url;
    
    protected Bookmark() {}

    public Bookmark(final String label, final String url) {
        this.label = label;
        this.url = url;
    }

    public String getLabel() {
        return this.label;
    }

    public String getUrl() {
        return this.url;
    }
}
