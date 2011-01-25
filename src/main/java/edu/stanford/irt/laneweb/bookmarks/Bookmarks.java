package edu.stanford.irt.laneweb.bookmarks;

import java.util.LinkedList;

public class Bookmarks extends LinkedList<Bookmark> {

    private static final long serialVersionUID = 1L;

    private String emrid;

    public Bookmarks(final String emrid) {
        this.emrid = emrid;
    }

    public String getEmrid() {
        return this.emrid;
    }
}
