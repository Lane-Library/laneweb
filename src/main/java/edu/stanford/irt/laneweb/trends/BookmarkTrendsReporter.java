package edu.stanford.irt.laneweb.trends;

import edu.stanford.irt.lane.trends.GoogleTracker;
import edu.stanford.irt.laneweb.bookmarks.BookmarkDAO;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

public class BookmarkTrendsReporter {

    private BookmarkDAO bookmarkDAO;

    private GoogleTracker googleTracker;

    private String localHostname;

    private Logger log = LoggerFactory.getLogger(BookmarkTrendsReporter.class);

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

    // daily at 1:16AM
    @Scheduled(cron = "0 16 01 * * *")
    public void reportCount() {
        this.googleTracker.trackEvent("/bookmarks", "lane:bookmark", "dailyUserCount", getLocalHostname(),
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
}
