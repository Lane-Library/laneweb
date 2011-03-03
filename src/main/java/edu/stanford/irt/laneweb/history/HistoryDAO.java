package edu.stanford.irt.laneweb.history;

import java.util.List;

public interface HistoryDAO {

    public List<TrackingData> getHistory(final String emrid);

    public void saveHistory(final List<TrackingData> bookmarks, String emrid);
}