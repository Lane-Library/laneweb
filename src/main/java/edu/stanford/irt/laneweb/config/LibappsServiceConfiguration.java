package edu.stanford.irt.laneweb.config;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.stanford.irt.laneweb.rest.RESTService;
import edu.stanford.irt.laneweb.status.ClassesStatusService;
import edu.stanford.irt.status.StatusService;

@Configuration
public class LibappsServiceConfiguration {

    @Bean("java.net.URI/libcal-service")
    public URI classesServiceURI(@Value("${edu.stanford.irt.laneweb.libcal-service.scheme}") final String scheme,
            @Value("${edu.stanford.irt.laneweb.libcal-service.host}") final String host,
            @Value("${edu.stanford.irt.laneweb.libcal-service.path}") final String path,
            @Value("${edu.stanford.irt.laneweb.libcal-service.port}") final int port) throws URISyntaxException {
        return new URI(scheme, null, host, port, path, null, null);
    }

    @Bean
    public StatusService classesStatusService(@Qualifier("java.net.URI/libcal-service") final URI classesServiceURI,
            final RESTService restService) {
        return new ClassesStatusService(classesServiceURI, restService);
    }

    @Bean("java.net.URI/libguide-service")
    public URI guideServiceURI(@Value("${edu.stanford.irt.laneweb.libguide-service.scheme}") final String scheme,
            @Value("${edu.stanford.irt.laneweb.libguide-service.host}") final String host,
            @Value("${edu.stanford.irt.laneweb.libguide-service.port}") final int port) throws URISyntaxException {
        return new URI(scheme, null, host, port, null, null, null);
    }
}
