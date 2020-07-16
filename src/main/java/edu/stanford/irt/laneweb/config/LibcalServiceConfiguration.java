package edu.stanford.irt.laneweb.config;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LibcalServiceConfiguration {

    @Bean("java.net.URI/libcal-service")
    public URI classesServiceURI(@Value("${edu.stanford.irt.laneweb.libcal-service.scheme}") String scheme,
            @Value("${edu.stanford.irt.laneweb.libcal-service.host}") String host,
            @Value("${edu.stanford.irt.laneweb.libcal-service.path}") String path,
            @Value("${edu.stanford.irt.laneweb.libcal-service.port}") int port) throws URISyntaxException {
        return new URI(scheme, null, host, port, path, null, null);
    }
}
