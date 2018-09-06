package edu.stanford.irt.laneweb.config;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.stanford.irt.laneweb.catalog.CatalogStatusService;
import edu.stanford.irt.laneweb.rest.RESTService;
import edu.stanford.irt.status.StatusService;

@Configuration
public class CatalogServiceConfiguration {

    @Bean("java.net.URI/catalog-service")
    public URI catalogServiceURI(
            @Value("${edu.stanford.irt.laneweb.catalog-service.scheme}") final String scheme,
            @Value("${edu.stanford.irt.laneweb.catalog-service.host}") final String host,
            @Value("${edu.stanford.irt.laneweb.catalog-service.port}") final int port,
            @Value("${edu.stanford.irt.laneweb.catalog-service.path}") final String path,
            @Value("${edu.stanford.irt.laneweb.catalog-service.userInfo}") final String userInfo)
            throws URISyntaxException {
        return new URI(scheme, userInfo, host, port, path, null, null);
    }

    @Bean
    public StatusService catalogStatusService(@Qualifier("java.net.URI/catalog-service") final URI catalogServiceURI,
            final RESTService restService) {
        return new CatalogStatusService(catalogServiceURI, restService);
    }
}
