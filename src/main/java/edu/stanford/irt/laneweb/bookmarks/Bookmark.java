package edu.stanford.irt.laneweb.bookmarks;

public class Bookmark {

    private String label;

    private String url;

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
