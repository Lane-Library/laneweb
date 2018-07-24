package edu.stanford.irt.laneweb.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.stanford.irt.laneweb.crm.CRMService;
import edu.stanford.irt.laneweb.crm.RESTCRMService;
import edu.stanford.irt.laneweb.rest.RESTService;

@Configuration
public class CRMConfiguration {

    @Bean
    public CRMService restCrmService(
            @Value("${edu.stanford.irt.laneweb.acquisition-api.url}") final URI uri,
            RESTService restService) {
        return new RESTCRMService(uri, restService);
    }
}
