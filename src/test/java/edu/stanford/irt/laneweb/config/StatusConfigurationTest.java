package edu.stanford.irt.laneweb.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.UnknownHostException;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StatusConfigurationTest {

    private StatusConfiguration configuration;

    @BeforeEach
    public void setUp() {
        this.configuration = new StatusConfiguration();
    }

    @Test
    public void testIndexDotHtmlStatusProvider() {
        assertNotNull(this.configuration.indexDotHtmlStatusProvider(null, null, null, null, null));
    }

    @Test
    public void testJvmStatusProvider() {
        assertNotNull(this.configuration.jvmStatusProvider());
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
