package edu.stanford.irt.laneweb.config;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;

import edu.stanford.irt.bookcovers.BookCoverService;
import edu.stanford.irt.bookcovers.CacheBookCoverService;
import edu.stanford.irt.bookcovers.CompositeBookCoverService;
import edu.stanford.irt.bookcovers.GoogleBookCoverService;
import edu.stanford.irt.bookcovers.JDBCBookCoverService;
import edu.stanford.irt.laneweb.bookcovers.HTTPISBNService;

@Configuration
public class BookCoversConfiguration {

    private String apiKey;

    private DataSource bookCoverDataSource;

    private URI catalogServiceURI;

    private ObjectMapper objectMapper;

    @Autowired
    public BookCoversConfiguration(@Qualifier("javax.sql.DataSource/bookcovers") final DataSource bookCoverDataSource,
            @Value("${edu.stanford.irt.laneweb.bookcovers.google-api-key}") final String apiKey,
            final ObjectMapper objectMapper, final URI catalogServiceURI) {
        this.bookCoverDataSource = bookCoverDataSource;
        this.apiKey = apiKey;
        this.objectMapper = objectMapper;
        this.catalogServiceURI = catalogServiceURI;
    }

    @Bean
    public BookCoverService bookCoverService() {
        List<BookCoverService> services = new ArrayList<>();
        services.add(new CacheBookCoverService(new ConcurrentHashMap<Integer, Optional<String>>()));
        services.add(new JDBCBookCoverService(this.bookCoverDataSource));
        services.add(new GoogleBookCoverService(new HTTPISBNService(this.objectMapper, this.catalogServiceURI),
                new NetHttpTransport(), new JsonObjectParser(new JacksonFactory()), this.apiKey));
        return new CompositeBookCoverService(services);
    }
}
