package edu.stanford.irt.laneweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

import edu.stanford.irt.laneweb.email.EMailSender;

@Configuration
public class EmailConfiguration {

    @Bean
    EMailSender eMailSender(JavaMailSender mailSender) {
        return new EMailSender(mailSender);
    }
}
