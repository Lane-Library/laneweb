package edu.stanford.irt.laneweb.trends;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.laneweb.bookmarks.BookmarkService;
import edu.stanford.irt.laneweb.trends.googleA4.GoogleA4Tracker;

public class BookmarkTrendsReporterTest {

    private BookmarkService bookmarkService;

    private BookmarkTrendsReporter reporter;

    private GoogleA4Tracker ga4tracker;

    @BeforeEach
    public void setUp() throws Exception {
        this.bookmarkService = mock(BookmarkService.class);
        this.ga4tracker = mock(GoogleA4Tracker.class);
        this.reporter = new BookmarkTrendsReporter(this.bookmarkService, this.ga4tracker, "localhost");
    }

    @Test
    public final void testReportCount() throws Exception {
        expect(this.bookmarkService.getRowCount()).andReturn(10);
        this.ga4tracker.trackEvent("/bookmarks", "laneTrends:bookmark", "localhost", "dailyUserCount", 10);
        replay(this.ga4tracker, this.bookmarkService);
        this.reporter.reportCount();
        verify(this.ga4tracker, this.bookmarkService);
    }
}
