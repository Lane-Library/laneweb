package edu.stanford.irt.laneweb.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TrendsConfigurationTest {

    private TrendsConfiguration configuration;

    @BeforeEach
    public void setUp() {
        this.configuration = new TrendsConfiguration();
    }

    @Test
    public void testBookmarkTrendsReporter() throws UnknownHostException {
        assertNotNull(this.configuration.bookmarkTrendsReporter(null, null));
    }

    @Test
    public void testGoogleTracker() throws URISyntaxException, MalformedURLException {
        assertNotNull(this.configuration.googleA4Tracker());
    }
}
