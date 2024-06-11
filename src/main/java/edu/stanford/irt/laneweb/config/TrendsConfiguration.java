package edu.stanford.irt.laneweb.config;

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import edu.stanford.irt.laneweb.bookmarks.BookmarkService;
import edu.stanford.irt.laneweb.trends.BookmarkTrendsReporter;
import edu.stanford.irt.laneweb.trends.googleA4.GoogleA4Tracker;

@Configuration
@EnableScheduling
public class TrendsConfiguration {

    String scheme = "https";
    String host = "www.google-analytics.com";
    String path= "/mp/collect";
    String measurementId="G-62CXNZDHBW"; 
    String apiSecret="cgb-y6UnQcq-Lxogk1YOmw";
    String clientId="lane-library-trend";
    
    @Bean
    BookmarkTrendsReporter bookmarkTrendsReporter(final BookmarkService bookmarkService, GoogleA4Tracker googleA4Tracker)
            throws UnknownHostException {
        return new BookmarkTrendsReporter(bookmarkService, googleA4Tracker, InetAddress.getLocalHost().getHostName());
    }
    
    
    @Bean
    GoogleA4Tracker googleA4Tracker() throws URISyntaxException{
        String queryString = "measurement_id=".concat(measurementId).concat("&api_secret=").concat(apiSecret);
        URI uri = new URI(scheme, null, host, 443, path, queryString, null);
        return new GoogleA4Tracker( uri, clientId);
    }
}
