package edu.stanford.irt.laneweb.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import edu.stanford.irt.laneweb.bookmarks.BookmarkService;
import edu.stanford.irt.laneweb.trends.BookmarkTrendsReporter;
import edu.stanford.irt.laneweb.trends.GoogleTracker;

@Configuration
@EnableScheduling
public class TrendsConfiguration {

    private BookmarkService bookmarkService;

    @Autowired
    public TrendsConfiguration(final BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @Bean
    public BookmarkTrendsReporter bookmarkTrendsReporter() throws UnknownHostException {
        return new BookmarkTrendsReporter(this.bookmarkService, googleTracker(),
                InetAddress.getLocalHost().getHostName());
    }

    @Bean
    public GoogleTracker googleTracker() {
        GoogleTracker tracker = new GoogleTracker();
        tracker.setDomainName("lane.stanford.edu");
        tracker.setGoogleAccount("UA-3202241-12");
        tracker.setReferer("http://lane.stanford.edu/index.html");
        tracker.setUserAgent("laneweb");
        return tracker;
    }
}
