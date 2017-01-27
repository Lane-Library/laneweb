package edu.stanford.irt.laneweb.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.oxm.Marshaller;

import edu.stanford.irt.cocoon.pipeline.Generator;
import edu.stanford.irt.cocoon.sitemap.select.Selector;
import edu.stanford.irt.laneweb.bookmarks.BookmarkDAO;
import edu.stanford.irt.laneweb.bookmarks.BookmarkGenerator;
import edu.stanford.irt.laneweb.bookmarks.SQLBookmarkDAO;
import edu.stanford.irt.laneweb.bookmarks.StanfordDomainStrippingBookmarkDAO;
import edu.stanford.irt.laneweb.cocoon.ActionSelector;
import edu.stanford.irt.laneweb.servlet.binding.BookmarkDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.DataBinder;

@Configuration
public class BookmarksConfiguration {

    private DataSource dataSource;

    private Marshaller marshaller;

    @Autowired
    public BookmarksConfiguration(@Qualifier("javax.sql.DataSource/bookmarks") final DataSource dataSource,
            final Marshaller marshaller) {
        this.dataSource = dataSource;
        this.marshaller = marshaller;
    }

    @Bean(name = "edu.stanford.irt.cocoon.sitemap.select.Selector/action")
    public Selector actionSelector() {
        return new ActionSelector();
    }

    @Bean(name = "edu.stanford.irt.laneweb.bookmarks.BookmarkDAO")
    public BookmarkDAO bookmarkDAO() {
        return new StanfordDomainStrippingBookmarkDAO(new SQLBookmarkDAO(this.dataSource));
    }

    @Bean(name = "edu.stanford.irt.laneweb.servlet.binding.DataBinder/bookmark")
    public DataBinder bookmarkDataBinder() {
        BookmarkDataBinder dataBinder = new BookmarkDataBinder();
        dataBinder.setBookmarkDAO(bookmarkDAO());
        return dataBinder;
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/bookmark")
    @Scope("prototype")
    public Generator bookmarkGenerator() {
        return new BookmarkGenerator(this.marshaller);
    }
}