package edu.stanford.irt.laneweb.trends;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;

import edu.stanford.irt.laneweb.bookmarks.BookmarkDAO;

public class BookmarkTrendsReporter {

    private BookmarkDAO bookmarkDAO;

    private GoogleTracker googleTracker;

    private String localHostname;

    private final Logger log;
    
    public BookmarkTrendsReporter(Logger log) {
        this.log = log;
    }

    // daily at 1:16AM
    @Scheduled(cron = "0 16 01 * * *")
    public void reportCount() {
        this.googleTracker.trackEvent("/bookmarks", "laneTrends:bookmark", getLocalHostname(), "dailyUserCount",
                this.bookmarkDAO.getRowCount());
    }

    /**
     * @param bookmarkDAO
     *            the bookmarkDAO to set
     */
    public void setBookmarkDAO(final BookmarkDAO bookmarkDAO) {
        this.bookmarkDAO = bookmarkDAO;
    }

    /**
     * @param googleTracker
     *            the googleTracker to set
     */
    public void setGoogleTracker(final GoogleTracker googleTracker) {
        this.googleTracker = googleTracker;
    }

    private String getLocalHostname() {
        if (null != this.localHostname) {
            return this.localHostname;
        }
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            this.localHostname = inetAddress.getHostName();
        } catch (UnknownHostException e) {
            this.log.error(e.getMessage(), e);
        }
        return this.localHostname;
    }
}
