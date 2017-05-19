package edu.stanford.irt.laneweb.config;

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

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;

import edu.stanford.irt.bookcovers.BookCoverService;
import edu.stanford.irt.bookcovers.CacheBookCoverService;
import edu.stanford.irt.bookcovers.CompositeBookCoverService;
import edu.stanford.irt.bookcovers.GoogleBookCoverService;
import edu.stanford.irt.bookcovers.JDBCBookCoverService;
import edu.stanford.irt.bookcovers.JDBCISBNService;

@Configuration
public class BookCoversConfiguration {

    private String apiKey;

    private DataSource bookCoverDataSource;

    private DataSource voyagerDataSource;

    @Autowired
    public BookCoversConfiguration(@Qualifier("javax.sql.DataSource/bookcovers") final DataSource bookCoverDataSource,
            @Qualifier("javax.sql.DataSource/catalog") final DataSource voyagerDataSource,
            @Value("${edu.stanford.irt.laneweb.bookcovers.google-api-key}") final String apiKey) {
        this.bookCoverDataSource = bookCoverDataSource;
        this.voyagerDataSource = voyagerDataSource;
        this.apiKey = apiKey;
    }

    @Bean
    public BookCoverService bookCoverService() {
        List<BookCoverService> services = new ArrayList<>();
        services.add(new CacheBookCoverService(new ConcurrentHashMap<Integer, Optional<String>>()));
        services.add(new JDBCBookCoverService(this.bookCoverDataSource));
        services.add(new GoogleBookCoverService(new JDBCISBNService(this.voyagerDataSource), new NetHttpTransport(),
                new JsonObjectParser(new JacksonFactory()), this.apiKey));
        return new CompositeBookCoverService(services);
    }
}
