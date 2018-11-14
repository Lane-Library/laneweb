package edu.stanford.irt.laneweb.trends;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.bookmarks.BookmarkService;

public class BookmarkTrendsReporterTest {

    private BookmarkService bookmarkService;

    private BookmarkTrendsReporter reporter;

    private GoogleTracker tracker;

    @Before
    public void setUp() throws Exception {
        this.bookmarkService = mock(BookmarkService.class);
        this.tracker = mock(GoogleTracker.class);
        this.reporter = new BookmarkTrendsReporter(this.bookmarkService, this.tracker, "localhost");
    }

    @Test
    public final void testReportCount() throws Exception {
        expect(this.bookmarkService.getRowCount()).andReturn(10).times(2);
        this.tracker.trackEvent("/bookmarks", "laneTrends:bookmark", "localhost", "dailyUserCount", 10);
        this.tracker.trackEvent("/bookmarks", "laneTrends:bookmark", "localhost", "dailyUserCount", 10);
        replay(this.tracker, this.bookmarkService);
        this.reporter.reportCount();
        this.reporter.reportCount();
        verify(this.tracker, this.bookmarkService);
    }
}
