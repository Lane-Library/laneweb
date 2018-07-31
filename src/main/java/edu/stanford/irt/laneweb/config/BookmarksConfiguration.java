package edu.stanford.irt.laneweb.config;

import java.net.URI;
import java.net.URISyntaxException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.oxm.Marshaller;

import edu.stanford.irt.cocoon.pipeline.Generator;
import edu.stanford.irt.cocoon.sitemap.select.Selector;
import edu.stanford.irt.laneweb.bookmarks.BookmarkGenerator;
import edu.stanford.irt.laneweb.bookmarks.BookmarkService;
import edu.stanford.irt.laneweb.bookmarks.JDBCBookmarkService;
import edu.stanford.irt.laneweb.bookmarks.RESTBookmarkService;
import edu.stanford.irt.laneweb.bookmarks.StanfordDomainStrippingBookmarkService;
import edu.stanford.irt.laneweb.cocoon.ActionSelector;
import edu.stanford.irt.laneweb.rest.RESTService;
import edu.stanford.irt.laneweb.servlet.binding.BookmarkDataBinder;

@Configuration
public class BookmarksConfiguration {

    @Bean(name = "edu.stanford.irt.cocoon.sitemap.select.Selector/action")
    public Selector actionSelector() {
        return new ActionSelector();
    }

    @Bean
    public BookmarkDataBinder bookmarkDataBinder(final BookmarkService bookmarkService) {
        return new BookmarkDataBinder(bookmarkService);
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/bookmark")
    @Scope("prototype")
    public Generator bookmarkGenerator(final Marshaller marshaller) {
        return new BookmarkGenerator(marshaller);
    }

    @Bean
    @Profile("!gce")
    public BookmarkService bookmarkService(final DataSource dataSource) {
        return new StanfordDomainStrippingBookmarkService(new JDBCBookmarkService(dataSource));
    }

    @Bean("java.net.URI/bookmark-service")
    public URI bookmarkServiceURI(
            @Value("${edu.stanford.irt.laneweb.bookmark-service.scheme}") final String scheme,
            @Value("${edu.stanford.irt.laneweb.bookmark-service.host}") final String host,
            @Value("${edu.stanford.irt.laneweb.bookmark-service.port}") final int port,
            @Value("${edu.stanford.irt.laneweb.bookmark-service.path}") final String path) throws URISyntaxException {
        return new URI(scheme, null, host, port, path, null, null);
    }

    @Bean
    @Profile("gce")
    public BookmarkService restBookmarkService(
            @Qualifier("java.net.URI/bookmark-service") final URI bookmarksURI,
            final RESTService restService) {
        return new RESTBookmarkService(bookmarksURI, restService);
    }
}
