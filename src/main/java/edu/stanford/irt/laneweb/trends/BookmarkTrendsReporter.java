package edu.stanford.irt.laneweb.trends;

import java.net.InetAddress;
import java.net.UnknownHostException;

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

    // daily at 1:16AM
    @Scheduled(cron = "0 16 01 * * *")
    public void reportCount() {
        try {
            this.googleTracker.trackEvent("/bookmarks", "laneTrends:bookmark", getLocalHostname(), "dailyUserCount",
                    this.bookmarkService.getRowCount());
        } catch (UnknownHostException | LanewebException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    /**
     * @param bookmarkService
     *            the bookmarkService to set
     */
    public void setBookmarkDAO(final BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    /**
     * @param googleTracker
     *            the googleTracker to set
     */
    public void setGoogleTracker(final GoogleTracker googleTracker) {
        this.googleTracker = googleTracker;
    }

    private String getLocalHostname() throws UnknownHostException {
        if (null != this.localHostname) {
            return this.localHostname;
        }
        InetAddress inetAddress = InetAddress.getLocalHost();
        this.localHostname = inetAddress.getHostName();
        return this.localHostname;
    }
}
