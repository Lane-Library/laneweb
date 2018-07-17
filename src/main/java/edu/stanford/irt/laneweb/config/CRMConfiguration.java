package edu.stanford.irt.laneweb.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.stanford.irt.laneweb.crm.CRMService;
import edu.stanford.irt.laneweb.crm.HTTPCRMService;

@Configuration
public class CRMConfiguration {

    @Bean
    public CRMService httpCrmService(
            @Value("${edu.stanford.irt.laneweb.acquisition-api.url}") final URI acquisitionURI) {
        return new HTTPCRMService(acquisitionURI);
    }
}
