package edu.stanford.irt.laneweb.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import edu.stanford.irt.laneweb.email.EMailSender;
import edu.stanford.irt.laneweb.email.SpamFilter;

@Configuration
public class EmailConfiguration {

    @Bean
    public EMailSender eMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("medmail.stanford.edu");
        return new EMailSender(mailSender, spamFilter());
    }

    @Bean
    public SpamFilter spamFilter() {
        Set<String> recipients = new HashSet<>();
        recipients.add("lane-issue@med.stanford.edu");
        recipients.add("LaneAskUs@stanford.edu");
        recipients.add("lanelibacqs@lists.stanford.edu");
        recipients.add("docxpress@lists.stanford.edu");
        Set<String> spamIps = new HashSet<>();
        spamIps.add("173.44.37.242");
        spamIps.add("96.47.224.218");
        spamIps.add("192.119.144.96");
        spamIps.add("192.119.151.127");
        spamIps.add("178.32.54.240");
        spamIps.add("178.150.15.201");
        spamIps.add("37.57.231.135");
        spamIps.add("208.115.124.60");
        spamIps.add("31.184.238.28");
        Set<String> spamReferrers = new HashSet<>();
        spamReferrers.add("http://lane.stanford.edu/dept/anthropology/cgi-bin/web/index.html?q=user");
        spamReferrers.add("http://lane.stanford.edu/dept/anthropology/cgi-bin/web/index.html?q=user/register");
        return new SpamFilter(recipients, spamIps, spamReferrers);
    }
}
