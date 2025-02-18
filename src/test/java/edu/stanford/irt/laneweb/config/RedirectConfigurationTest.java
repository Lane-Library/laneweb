package edu.stanford.irt.laneweb.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RedirectConfigurationTest {

    private RedirectConfiguration configuration;

    @BeforeEach
    public void setUp() {
        this.configuration = new RedirectConfiguration();
    }

    @Test
    public void testRedirectProcessor() {
        assertNotNull(this.configuration.redirectProcessor());
    }
}
