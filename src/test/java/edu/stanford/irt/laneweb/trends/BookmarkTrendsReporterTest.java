package edu.stanford.irt.laneweb.trends;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import edu.stanford.irt.laneweb.bookmarks.BookmarkDAO;

public class BookmarkTrendsReporterTest {

    BookmarkDAO dao;

    Logger logger;

    BookmarkTrendsReporter reporter;

    GoogleTracker tracker;

    @Before
    public void setUp() throws Exception {
        this.logger = createMock(Logger.class);
        this.tracker = createMock(GoogleTracker.class);
        this.reporter = new BookmarkTrendsReporter(this.logger);
        this.dao = createMock(BookmarkDAO.class);
        this.reporter.setBookmarkDAO(this.dao);
        this.reporter.setGoogleTracker(this.tracker);
    }

    @SuppressWarnings("boxing")
    @Test
    public final void testReportCount() throws Exception {
        expect(this.dao.getRowCount()).andReturn(10);
        this.tracker.trackEvent(eq("/bookmarks"), eq("laneTrends:bookmark"), isA(String.class), eq("dailyUserCount"),
                eq(10));
        replay(this.tracker, this.dao);
        this.reporter.reportCount();
        verify(this.tracker, this.dao);
    }
    
}
