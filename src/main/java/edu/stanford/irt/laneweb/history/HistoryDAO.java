package edu.stanford.irt.laneweb.history;

import edu.stanford.irt.laneweb.bookmarks.Bookmarks;

public interface HistoryDAO {

    public Bookmarks getHistory(final String emrid);

    public void saveHistory(final Bookmarks bookmarks);
}