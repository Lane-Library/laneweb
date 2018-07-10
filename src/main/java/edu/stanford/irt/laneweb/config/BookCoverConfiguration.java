package edu.stanford.irt.laneweb.config;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.stanford.irt.laneweb.bookcovers.BookCoverService;
import edu.stanford.irt.laneweb.bookcovers.RESTBookCoverService;
import edu.stanford.irt.laneweb.rest.RESTService;

@Configuration
public class BookCoverConfiguration {

    @Bean
    public BookCoverService bookCoverService(
            @Qualifier("java.net.URI/bookcover-service") final URI bookCoverServiceURI,
            final RESTService restService) {
        return new RESTBookCoverService(bookCoverServiceURI, restService);
    }

    @Bean("java.net.URI/bookcover-service")
    public URI bookcoverServiceURI(
            @Value("${edu.stanford.irt.laneweb.bookcover-service.scheme}") final String scheme,
            @Value("${edu.stanford.irt.laneweb.bookcover-service.host}") final String host,
            @Value("${edu.stanford.irt.laneweb.bookcover-service.port}") final int port,
            @Value("${edu.stanford.irt.laneweb.bookcover-service.path}") final String path)
            throws URISyntaxException {
        return new URI(scheme, null, host, port, path, null, null);
    }
}
