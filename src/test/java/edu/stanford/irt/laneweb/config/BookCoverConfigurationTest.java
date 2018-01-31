package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;

public class BookCoverConfigurationTest {

    private BookCoverConfiguration configuration;

    @Before
    public void setUp() {
        this.configuration = new BookCoverConfiguration();
    }

    @Test
    public void testBookCoverService() {
        assertNotNull(this.configuration.bookCoverService(null, null));
    }

    @Test
    public void testBookcoverServiceURI() throws URISyntaxException {
        URI uri = this.configuration.bookcoverServiceURI("http", "localhost", 80, "/");
        assertEquals("http://localhost:80/", uri.toString());
    }
}
