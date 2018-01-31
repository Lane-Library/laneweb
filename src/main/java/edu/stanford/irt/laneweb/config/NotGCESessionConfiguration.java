package edu.stanford.irt.laneweb.config;

import javax.servlet.Filter;

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
