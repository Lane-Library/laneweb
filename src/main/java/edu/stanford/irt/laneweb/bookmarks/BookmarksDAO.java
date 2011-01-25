package edu.stanford.irt.laneweb.bookmarks;

public interface BookmarksDAO {

    public Bookmarks getBookmarks(final String emrid);

    public void saveBookmarks(final String emrid, final Bookmarks bookmarks);
}