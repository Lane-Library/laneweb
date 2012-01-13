package edu.stanford.irt.laneweb.bookmarks;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DummyHistoryDAO implements BookmarkDAO<History> {

    public List<History> getLinks(final String sunetid) {
        List<History> history = new ArrayList<History>();
        history.add(new History("Wikipedia", "http://www.wikipedia.org", new Date(Long.MIN_VALUE)));
        history.add(new History("SlashDot", "http://www.slashdot.org", new Date(Long.MAX_VALUE)));
        return history;
    }

    public void saveLinks(final String sunetid, final List<History> links) {
    }
}
