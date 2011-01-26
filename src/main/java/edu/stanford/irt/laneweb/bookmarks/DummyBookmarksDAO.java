package edu.stanford.irt.laneweb.bookmarks;

import org.springframework.stereotype.Component;

@Component
public class DummyBookmarksDAO implements BookmarksDAO {

    @Override
    public Bookmarks getBookmarks(final String emrid) {
        Bookmarks bookmarks = new Bookmarks(emrid);
        bookmarks.add(new Bookmark("Google", "http://www.google.com"));
        bookmarks.add(new Bookmark("UpToDate", "http://www.uptodate.com"));
        return bookmarks;
    }

    @Override
    public void saveBookmarks(final String emrid, final Bookmarks bookmarks) {
    }
}
