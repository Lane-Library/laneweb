package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertNotNull;

import java.net.UnknownHostException;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

public class StatusConfigurationTest {

    private StatusConfiguration configuration;

    @Before
    public void setUp() {
        this.configuration = new StatusConfiguration();
    }

    @Test
    public void testEresourceStatusProvider() {
        assertNotNull(this.configuration.eresourceStatusProvider(null));
    }

    @Test
    public void testIndexDotHtmlStatusProvider() {
        assertNotNull(this.configuration.indexDotHtmlStatusProvider(null, null, null, null, null));
    }

    @Test
    public void testLanewebStatusService() {
        assertNotNull(this.configuration.lanewebStatusService(Collections.emptyList()));
    }

    @Test
    public void testStatusService() throws UnknownHostException {
        assertNotNull(this.configuration.statusService(Collections.emptyList(), null));
    }

    @Test
    public void testSuggestStatusProvider() {
        assertNotNull(this.configuration.suggestStatusProvider(null));
    }
}
