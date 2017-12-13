package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class BookCoversConfigurationTest {

    private BookCoversConfiguration configuration;

    @Before
    public void setUp() {
        this.configuration = new BookCoversConfiguration();
    }

    @Test
    public void testBookCoverService() {
        assertNotNull(this.configuration.bookCoverService(null, null, null));
    }

    @Test
    public void testHttpISBNService() {
        assertNotNull(this.configuration.httpISBNService(null, null));
    }
}
