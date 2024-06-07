package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class EmailConfigurationTest {

    private EmailConfiguration configuration;

    private JavaMailSenderImpl mailSender;

    @Before
    public void setUp() {
        this.configuration = new EmailConfiguration();
    }

    @Test
    public void testEMailSender() {
        assertNotNull(this.configuration.eMailSender(mailSender));
    }
}
