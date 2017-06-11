package edu.stanford.irt.laneweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import edu.stanford.irt.laneweb.email.EMailSender;

@Configuration
public class EmailConfiguration {

    @Bean
    public EMailSender eMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("medmail.stanford.edu");
        return new EMailSender(mailSender);
    }
}
