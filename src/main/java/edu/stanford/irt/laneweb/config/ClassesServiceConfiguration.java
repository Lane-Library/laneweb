package edu.stanford.irt.laneweb.config;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClassesServiceConfiguration {

    @Bean("java.net.URI/classes-service")
    public URI catalogServiceURI(@Value("${edu.stanford.irt.laneweb.classes-service.scheme}") final String scheme,
            @Value("${edu.stanford.irt.laneweb.classes-service.host}") final String host,
            @Value("${edu.stanford.irt.laneweb.classes-service.port}") final int port,
            @Value("${edu.stanford.irt.laneweb.classes-service.path}") final String path,
            @Value("${edu.stanford.irt.laneweb.classes-service.userInfo}") final String userInfo)
            throws URISyntaxException {
        return new URI(scheme, userInfo, host, port, path, null, null);
    }
}
