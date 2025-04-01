package edu.stanford.irt.laneweb.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class EmailConfigurationTest {

    private EmailConfiguration configuration;

    private JavaMailSenderImpl mailSender;

    @BeforeEach
    public void setUp() {
        this.configuration = new EmailConfiguration();
    }

    @Test
    public void testEMailSender() {
        assertNotNull(this.configuration.eMailSender(mailSender));
    }
}
