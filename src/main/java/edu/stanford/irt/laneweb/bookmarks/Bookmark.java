package edu.stanford.irt.laneweb.bookmarks;

import java.io.Serializable;

import edu.stanford.irt.laneweb.LanewebException;

public class Bookmark implements Serializable {

    private static final long serialVersionUID = 1L;

    private transient int hashcode = 0;

    private String label;

    private String url;

    public Bookmark(final String label, final String url) {
        if (label == null) {
            throw new LanewebException("null label");
        }
        if (url == null) {
            throw new LanewebException("null url");
        }
        this.label = label;
        this.url = url;
    }

    @Override
    public boolean equals(final Object other) {
        boolean equals = false;
        if (other instanceof Bookmark && other.hashCode() == hashCode()) {
            Bookmark that = (Bookmark) other;
            equals = this.label.equals(that.label) && this.url.equals(that.url);
        }
        return equals;
    }

    public String getLabel() {
        return this.label;
    }

    public String getUrl() {
        return this.url;
    }

    @Override
    public int hashCode() {
        if (this.hashcode == 0) {
            this.hashcode = this.label.hashCode() ^ this.url.hashCode();
        }
        return this.hashcode;
    }
}
