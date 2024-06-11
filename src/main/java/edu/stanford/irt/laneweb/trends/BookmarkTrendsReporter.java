package edu.stanford.irt.laneweb.trends;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.bookmarks.BookmarkService;
import edu.stanford.irt.laneweb.trends.googleA4.GoogleA4Tracker;

public class BookmarkTrendsReporter {

    private static final Logger log = LoggerFactory.getLogger(BookmarkTrendsReporter.class);

    private BookmarkService bookmarkService;

    private GoogleA4Tracker googleA4Tracker;

    private String localHostname;

    /**
     * @param bookmarkService
     *            the bookmarkService to set
     * @param googleA4Tracker
     *            the googleA4Tracker to set
     * @param googleA4Tracker 
     * @param localHostName
     *            the hostname
     */
    public BookmarkTrendsReporter(final BookmarkService bookmarkService, GoogleA4Tracker googleA4Tracker, final String localHostName) {
        this.bookmarkService = bookmarkService;
        this.googleA4Tracker = googleA4Tracker;
        this.localHostname = localHostName;
    }

    // daily at 1:16AM
    @Scheduled(cron = "0 16 01 * * *")
    public void reportCount() {
        try {
        	int boobkemarkCount = this.bookmarkService.getRowCount();
        	this.googleA4Tracker.trackEvent("/bookmarks" , "laneTrends:bookmark", this.localHostname, "dailyUserCount" , boobkemarkCount);
        } catch (LanewebException e) {
            log.error(e.getMessage(), e);
        }
    }
}
