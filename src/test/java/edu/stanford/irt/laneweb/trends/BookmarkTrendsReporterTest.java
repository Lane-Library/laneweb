package edu.stanford.irt.laneweb.trends;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.bookmarks.BookmarkService;

public class BookmarkTrendsReporterTest {

    private BookmarkService dao;

    private BookmarkTrendsReporter reporter;

    private GoogleTracker tracker;

    @Before
    public void setUp() throws Exception {
        this.tracker = createMock(GoogleTracker.class);
        this.reporter = new BookmarkTrendsReporter();
        this.dao = createMock(BookmarkService.class);
        this.reporter.setBookmarkDAO(this.dao);
        this.reporter.setGoogleTracker(this.tracker);
    }

    @SuppressWarnings("boxing")
    @Test
    public final void testReportCount() throws Exception {
        expect(this.dao.getRowCount()).andReturn(10).times(2);
        this.tracker.trackEvent(eq("/bookmarks"), eq("laneTrends:bookmark"), isA(String.class), eq("dailyUserCount"),
                eq(10));
        this.tracker.trackEvent(eq("/bookmarks"), eq("laneTrends:bookmark"), isA(String.class), eq("dailyUserCount"),
                eq(10));
        replay(this.tracker, this.dao);
        this.reporter.reportCount();
        this.reporter.reportCount();
        verify(this.tracker, this.dao);
    }
}
