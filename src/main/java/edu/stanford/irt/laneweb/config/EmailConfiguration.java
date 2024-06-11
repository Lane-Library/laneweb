package edu.stanford.irt.laneweb.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import edu.stanford.irt.laneweb.email.EMailSender;

@Configuration
public class EmailConfiguration {

    @Bean
    EMailSender eMailSender(JavaMailSenderImpl mailSender) {
        return new EMailSender(mailSender);
    }
}
