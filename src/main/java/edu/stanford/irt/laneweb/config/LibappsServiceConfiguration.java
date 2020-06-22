package edu.stanford.irt.laneweb.config;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LibappsServiceConfiguration {

    @Bean("java.net.URI/libapps-service")
    public URI classesServiceURI(
            @Value("${edu.stanford.irt.laneweb.libapps-service.scheme}") String scheme,
            @Value("${edu.stanford.irt.laneweb.libapps-service.host}") String host,
            @Value("${edu.stanford.irt.laneweb.libapps-service.port}") int port
            ) throws URISyntaxException {
        return new URI(scheme, null, host, port, null, null, null);
    }
   
}