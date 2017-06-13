package edu.stanford.irt.laneweb.config;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class CatalogServiceConfiguration {

    @Bean("java.net.URI/catalog-service")
    @Profile("gce")
    public URI catalogServiceURI(
            @Value("${edu.stanford.irt.laneweb.catalog-service.scheme}") String scheme,
            @Value("${edu.stanford.irt.laneweb.catalog-service.host}") String host,
            @Value("${edu.stanford.irt.laneweb.catalog-service.port}") int port,
            @Value("${edu.stanford.irt.laneweb.catalog-service.path}") String path,
            @Value("${edu.stanford.irt.laneweb.catalog-service.userInfo}") String userInfo
            ) throws URISyntaxException {
        return new URI(scheme, userInfo, host, port, path, null, null);
    }
}
