package edu.stanford.irt.laneweb.bookmarks;

import org.springframework.stereotype.Component;

@Component
public class DummyBookmarksDAO implements BookmarksDAO {

    @Override
    public Bookmarks getBookmarks(final String emrid) {
        return new Bookmarks();
    }

    @Override
    public void saveBookmarks(final String emrid, final Bookmarks bookmarks) {
    }
}
