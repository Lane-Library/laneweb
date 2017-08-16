package edu.stanford.irt.laneweb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import edu.stanford.irt.laneweb.email.EMailSender;

@Configuration
public class EmailConfiguration {

    @Bean
    public EMailSender eMailSender(@Value("${edu.stanford.irt.laneweb.email.smtpHost}") final String smtpHost,
            @Value("${edu.stanford.irt.laneweb.email.smtpPort}") final int smtpPort) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(smtpHost);
        mailSender.setPort(smtpPort);
        return new EMailSender(mailSender);
    }
}
