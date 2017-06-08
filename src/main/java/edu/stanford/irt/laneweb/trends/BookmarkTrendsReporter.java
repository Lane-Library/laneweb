package edu.stanford.irt.laneweb.trends;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.bookmarks.BookmarkService;

public class BookmarkTrendsReporter {

    private static final Logger LOG = LoggerFactory.getLogger(BookmarkTrendsReporter.class);

    private BookmarkService bookmarkService;

    private GoogleTracker googleTracker;

    private String localHostname;

    /**
     * @param bookmarkService
     *            the bookmarkService to set
     * @param googleTracker
     *            the googleTracker to set
     */
    public BookmarkTrendsReporter(final BookmarkService bookmarkService, final GoogleTracker googleTracker,
            final String localHostName) {
        this.bookmarkService = bookmarkService;
        this.googleTracker = googleTracker;
        this.localHostname = localHostName;
    }

    // daily at 1:16AM
    @Scheduled(cron = "0 16 01 * * *")
    public void reportCount() {
        try {
            this.googleTracker.trackEvent("/bookmarks", "laneTrends:bookmark", this.localHostname, "dailyUserCount",
                    this.bookmarkService.getRowCount());
        } catch (LanewebException e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
