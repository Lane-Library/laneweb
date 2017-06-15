package edu.stanford.irt.laneweb.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.oxm.Marshaller;

import edu.stanford.irt.cocoon.pipeline.Generator;
import edu.stanford.irt.cocoon.sitemap.select.Selector;
import edu.stanford.irt.laneweb.bookmarks.BookmarkGenerator;
import edu.stanford.irt.laneweb.bookmarks.BookmarkService;
import edu.stanford.irt.laneweb.bookmarks.JDBCBookmarkService;
import edu.stanford.irt.laneweb.bookmarks.StanfordDomainStrippingBookmarkService;
import edu.stanford.irt.laneweb.cocoon.ActionSelector;
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

    @Bean(name = "edu.stanford.irt.laneweb.bookmarks.BookmarkService")
    public BookmarkService bookmarkService(@Qualifier("javax.sql.DataSource/bookmarks") final DataSource dataSource) {
        return new StanfordDomainStrippingBookmarkService(new JDBCBookmarkService(dataSource));
    }
}
