package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.bookmarks.BookmarkService;

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

    @Test
    public void testDummyBookmarkService() {
        BookmarkService service = this.configuration.dummyBookmarkService();
        try {
            service.getLinks(null);
        } catch (UnsupportedOperationException e) {
            assertNotNull(e);
        }
        try {
            service.getRowCount();
        } catch (UnsupportedOperationException e) {
            assertNotNull(e);
        }
        try {
            service.saveLinks(null, null);
        } catch (UnsupportedOperationException e) {
            assertNotNull(e);
        }
    }
}
