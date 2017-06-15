package edu.stanford.irt.laneweb.config;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;

import edu.stanford.irt.bookcovers.BookCoverService;
import edu.stanford.irt.bookcovers.CacheBookCoverService;
import edu.stanford.irt.bookcovers.CompositeBookCoverService;
import edu.stanford.irt.bookcovers.GoogleBookCoverService;
import edu.stanford.irt.bookcovers.ISBNService;
import edu.stanford.irt.bookcovers.JDBCBookCoverService;
import edu.stanford.irt.bookcovers.JDBCISBNService;
import edu.stanford.irt.laneweb.bookcovers.HTTPISBNService;

@Configuration
public class BookCoversConfiguration {

    @Bean
    public BookCoverService bookCoverService(
            @Qualifier("javax.sql.DataSource/bookcovers") final DataSource bookCoverDataSource,
            final ISBNService isbnService,
            @Value("${edu.stanford.irt.laneweb.bookcovers.google-api-key}") final String apiKey) {
        List<BookCoverService> services = new ArrayList<>();
        services.add(new CacheBookCoverService(new ConcurrentHashMap<Integer, Optional<String>>()));
        services.add(new JDBCBookCoverService(bookCoverDataSource));
        services.add(new GoogleBookCoverService(isbnService, new NetHttpTransport(),
                new JsonObjectParser(new JacksonFactory()), apiKey));
        return new CompositeBookCoverService(services);
    }

    @Bean
    @Profile("gce")
    public ISBNService httpISBNService(final ObjectMapper objectMapper, final URI catalogServiceURI) {
        return new HTTPISBNService(objectMapper, catalogServiceURI);
    }

    @Bean
    @Profile("!gce")
    public ISBNService jdbcISBNService(@Qualifier("javax.sql.DataSource/catalog") final DataSource voyagerDataSource) {
        return new JDBCISBNService(voyagerDataSource);
    }
}
