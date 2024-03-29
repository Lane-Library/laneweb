package edu.stanford.irt.laneweb.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.stanford.irt.laneweb.folio.RESTUserService;
import edu.stanford.irt.laneweb.folio.UserService;
import edu.stanford.irt.laneweb.rest.RESTService;

@Configuration
public class PatronRegistrationConfiguration {

    @Bean
    public UserService userService(@Qualifier("java.net.URI/catalog-service") final URI catalogServiceURI,
            final RESTService restService) {
        return new RESTUserService(catalogServiceURI, restService);
    }
}
