package edu.stanford.irt.laneweb.bookmarks;

public interface BookmarksDAO {

    public Bookmarks getBookmarks(final String sunetid);

    public void saveBookmarks(final String sunetid, final Bookmarks bookmarks);
}