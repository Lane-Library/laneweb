package edu.stanford.irt.laneweb.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import edu.stanford.irt.laneweb.bookmarks.BookmarkService;
import edu.stanford.irt.laneweb.trends.BookmarkTrendsReporter;
import edu.stanford.irt.laneweb.trends.GoogleTracker;
import edu.stanford.irt.laneweb.trends.googleA4.GoogleA4Tracker;

@Configuration
@EnableScheduling
public class TrendsConfiguration {

    @Bean
    public BookmarkTrendsReporter bookmarkTrendsReporter(final BookmarkService bookmarkService,	GoogleTracker googleTracker, GoogleA4Tracker googleA4Tracker)
            throws UnknownHostException {
        return new BookmarkTrendsReporter(bookmarkService, googleTracker, googleA4Tracker, InetAddress.getLocalHost().getHostName());
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
    
    
    @Bean
    public GoogleA4Tracker googleA4Tracker() {
    	String endPoint="https://www.google-analytics.com/mp/collect";
    	String measurementId="G-62CXNZDHBW"; 
    	String apiSecret="cgb-y6UnQcq-Lxogk1YOmw";
    	String clientId="lane-library-trend";
        GoogleA4Tracker tracker = new GoogleA4Tracker( endPoint,  measurementId,  apiSecret, clientId);
        return tracker;
    }
}
