package edu.stanford.irt.laneweb.trends;

import org.junit.Before;
import org.junit.Test;

public class GoogleTrackerTest {

    GoogleTracker tracker;

    @Before
    public void setUp() throws Exception {
        this.tracker = new GoogleTracker();
        this.tracker.setDomainName("domainName");
        this.tracker.setGoogleAccount("googleAccount");
        this.tracker.setReferer("referer");
        this.tracker.setUserAgent("userAgent");
    }

    @Test
    public final void testTrackEvent() {
        this.tracker.trackEvent("path", "category", "action", "label", 1);
    }
}
