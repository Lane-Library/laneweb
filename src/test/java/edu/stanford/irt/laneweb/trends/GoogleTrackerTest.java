package edu.stanford.irt.laneweb.trends;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

public class GoogleTrackerTest {

    Logger logger;

    GoogleTracker tracker;

    @Before
    public void setUp() throws Exception {
        this.logger = createMock(Logger.class);
        this.tracker = new GoogleTracker(this.logger);
        this.tracker.setDomainName("domainName");
        this.tracker.setGoogleAccount("googleAccount");
        this.tracker.setReferer("referer");
        this.tracker.setUserAgent("userAgent");
    }

    @Test
    public final void testTrackEvent() {
        this.logger.info("path/category/action/label/1");
        replay(this.logger);
        this.tracker.trackEvent("path", "category", "action", "label", 1);
        verify(this.logger);
    }
}
