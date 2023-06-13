package edu.stanford.irt.laneweb.config;

import jakarta.servlet.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import edu.stanford.irt.laneweb.servlet.NOOPFilter;

@Configuration
@Profile("!gce")
public class NotGCESessionConfiguration {

    @Bean
    public Filter springSessionRepositoryFilter() {
        return new NOOPFilter();
    }
}
