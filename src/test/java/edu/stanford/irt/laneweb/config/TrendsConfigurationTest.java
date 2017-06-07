package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertNotNull;

import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;

public class TrendsConfigurationTest {

    private TrendsConfiguration configuration;

    @Before
    public void setUp() {
        this.configuration = new TrendsConfiguration(null);
    }

    @Test
    public void testBookmarkTrendsReporter() throws UnknownHostException {
        assertNotNull(this.configuration.bookmarkTrendsReporter());
    }

    @Test
    public void testGoogleTracker() {
        assertNotNull(this.configuration.googleTracker());
    }
}
