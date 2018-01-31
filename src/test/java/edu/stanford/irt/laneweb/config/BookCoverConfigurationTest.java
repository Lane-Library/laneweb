package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertNotNull;

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
}
