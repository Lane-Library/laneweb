package edu.stanford.irt.laneweb.bookmarks;

import org.springframework.stereotype.Component;

@Component
public class DummyBookmarksDAO implements BookmarksDAO {

    @Override
    public Bookmarks getBookmarks(final String sunetid) {
        return new Bookmarks();
    }

    @Override
    public void saveBookmarks(final String sunetid, final Bookmarks bookmarks) {
    }
}
