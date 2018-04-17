package edu.stanford.irt.laneweb.trends;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.IOException;
import java.net.URLConnection;

import org.junit.Before;
import org.junit.Test;

public class GoogleTrackerTest {

    private URLConnection connection;

    private GoogleTracker.URLConnectionFactory connectionFactory;

    private GoogleTracker tracker;

    @Before
    public void setUp() throws Exception {
        this.connectionFactory = mock(GoogleTracker.URLConnectionFactory.class);
        this.tracker = new GoogleTracker(this.connectionFactory);
        this.connection = mock(URLConnection.class);
        this.tracker.setDomainName("domainName");
        this.tracker.setGoogleAccount("googleAccount");
        this.tracker.setReferer("referer");
        this.tracker.setUserAgent("userAgent");
    }

    @Test
    public final void testTrackEvent() throws IOException {
        expect(this.connectionFactory.getConnection(isA(String.class))).andReturn(this.connection).times(2);
        this.connection.setUseCaches(false);
        this.connection.addRequestProperty("User-Agent", "userAgent");
        this.connection.addRequestProperty("Accept-Language", "en-us,en;q=0.5");
        this.connection.setUseCaches(false);
        this.connection.addRequestProperty("User-Agent", "userAgent");
        this.connection.addRequestProperty("Accept-Language", "en-us,en;q=0.5");
        expect(this.connection.getContent()).andReturn(null).times(2);
        replay(this.connectionFactory, this.connection);
        this.tracker.trackEvent("path", "category", "action", "label", 1);
        this.tracker.trackEvent("path", "category", "action", "label", 1);
        verify(this.connectionFactory, this.connection);
    }
}
