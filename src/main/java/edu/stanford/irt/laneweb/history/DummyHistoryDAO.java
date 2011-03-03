package edu.stanford.irt.laneweb.history;

import java.util.LinkedList;
import java.util.List;


public class DummyHistoryDAO implements HistoryDAO {

    public List<TrackingData> getHistory(String emrid) {
        return new LinkedList<TrackingData>();
    }

    public void saveHistory(List<TrackingData> bookmarks, String emrid) {
    }
}
