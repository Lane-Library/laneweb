package edu.stanford.irt.laneweb.bookmarks;

import java.io.Serializable;

public class Bookmark implements Serializable {

    private static final long serialVersionUID = 1L;

    private String label;

    private String url;

    protected Bookmark() {
    }

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

    public void setLabel(final String label) {
        this.label = label;
    }

    public void setUrl(final String url) {
        this.url = url;
    }
}
