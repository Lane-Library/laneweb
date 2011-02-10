package edu.stanford.irt.laneweb.bookmarks;

import javax.sql.DataSource;

public class DummyBookmarksDAO implements BookmarksDAO {

    @Override
    public Bookmarks getBookmarks(final String emrid) {
        Bookmarks bookmarks = new Bookmarks(emrid);
        bookmarks.add(new Bookmark("Google", "http://www.google.com"));
        bookmarks.add(new Bookmark("UpToDate", "http://www.uptodate.com"));
        return bookmarks;
    }

    @Override
    public void saveBookmarks(final Bookmarks bookmarks) {
    }
    
    public void setDataSource(DataSource dataSource) {
        
    }
}
