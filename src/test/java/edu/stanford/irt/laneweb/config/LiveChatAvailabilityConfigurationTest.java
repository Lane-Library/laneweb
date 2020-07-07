package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class LiveChatAvailabilityConfigurationTest {

    private LiveChatAvailabilityConfiguration configuration;

    @Before
    public void setUp() {
        this.configuration = new LiveChatAvailabilityConfiguration();
    }

    @Test
    public final void testLiveChatService() {
        assertNotNull(this.configuration.liveChatService(null, null));
    }
}
