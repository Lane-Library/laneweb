package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class EmailConfigurationTest {

    private EmailConfiguration configuration;

    @Before
    public void setUp() {
        this.configuration = new EmailConfiguration();
    }

    @Test
    public void testEMailSender() {
        assertNotNull(this.configuration.eMailSender());
    }

    @Test
    public void testSpamFilter() {
        assertNotNull(this.configuration.spamFilter());
    }
}
