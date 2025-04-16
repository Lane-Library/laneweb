package edu.stanford.irt.laneweb.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BookmarksConfigurationTest {

    private BookmarksConfiguration configuration;

    @BeforeEach
    public void setUp() {
        this.configuration = new BookmarksConfiguration();
    }

    @Test
    public void testActionSelector() {
        assertNotNull(this.configuration.actionSelector());
    }

    @Test
    public void testBookmarkDataBinder() {
        assertNotNull(this.configuration.bookmarkDataBinder(null));
    }

    @Test
    public void testBookmarkGenerator() {
        assertNotNull(this.configuration.bookmarkGenerator(null));
    }

    @Test
    public void testBookmarkServiceURI() throws URISyntaxException {
        assertNotNull(this.configuration.bookmarkServiceURI(null, null, 0, null));
    }

    @Test
    public void testRestBookmarkService() throws URISyntaxException {
        assertNotNull(this.configuration.restBookmarkService(new URI("/"), null));
    }
}
