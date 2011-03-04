package edu.stanford.irt.laneweb.history;

import edu.stanford.irt.laneweb.bookmarks.Bookmarks;


public class DummyHistoryDAO implements HistoryDAO {

    public Bookmarks getHistory(String emrid) {
        return new Bookmarks(emrid);
    }

    public void saveHistory(Bookmarks history) {
    }
}
