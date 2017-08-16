package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class BookmarksConfigurationTest {

    private BookmarksConfiguration configuration;

    @Before
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
    public void testBookmarkService() {
        assertNotNull(this.configuration.bookmarkService(null));
    }
}
