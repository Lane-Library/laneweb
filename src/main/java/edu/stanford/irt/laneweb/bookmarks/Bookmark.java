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
    
    public boolean equals(Bookmark other) {
        if (this.label == null && this.url == null) {
            return other.label == null && other.url == null;
        } else if (this.label == null) {
            return other.label == null && this.url.equals(other.url);
        } else if (this.url == null) {
            return other.url == null && this.label.equals(other.label);
        }
        return this.label.equals(other.label) && this.url.equals(other.label);
    }
}
