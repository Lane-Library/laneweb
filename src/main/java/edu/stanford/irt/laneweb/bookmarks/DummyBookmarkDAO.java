package edu.stanford.irt.laneweb.bookmarks;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class DummyBookmarkDAO implements BookmarkDAO {

    public List<Bookmark> getLinks(final String sunetid) {
        List<Bookmark> bookmarks = new ArrayList<Bookmark>();
        bookmarks.add(new Bookmark("Google", "http://www.google.com"));
        bookmarks.add(new Bookmark("UpToDate", "http://www.uptodate.com"));
        return bookmarks;
    }

    public void saveLinks(final String sunetid, final List<Bookmark> links) {
    }

    public void setDataSource(final DataSource dataSource) {
    }
}
