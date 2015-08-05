package edu.stanford.irt.laneweb.trends;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.bookmarks.BookmarkDAO;

public class BookmarkTrendsReporter {

    private static final Logger LOG = LoggerFactory.getLogger(BookmarkTrendsReporter.class);

    private BookmarkDAO bookmarkDAO;

    private GoogleTracker googleTracker;

    private String localHostname;

    // daily at 1:16AM
    @Scheduled(cron = "0 16 01 * * *")
    public void reportCount() {
        try {
            this.googleTracker.trackEvent("/bookmarks", "laneTrends:bookmark", getLocalHostname(), "dailyUserCount",
                    this.bookmarkDAO.getRowCount());
        } catch (UnknownHostException | LanewebException e) {
            LOG.error(e.getMessage(), e);
        }
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

    private String getLocalHostname() throws UnknownHostException {
        if (null != this.localHostname) {
            return this.localHostname;
        }
        InetAddress inetAddress = InetAddress.getLocalHost();
        this.localHostname = inetAddress.getHostName();
        return this.localHostname;
    }
}
