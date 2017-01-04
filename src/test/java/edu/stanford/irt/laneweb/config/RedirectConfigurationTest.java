package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class RedirectConfigurationTest {

    private RedirectConfiguration configuration;

    @Before
    public void setUp() {
        this.configuration = new RedirectConfiguration();
    }

    @Test
    public void testRedirectProcessor() {
        assertNotNull(this.configuration.redirectProcessor());
    }
}
