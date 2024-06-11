package edu.stanford.irt.laneweb.servlet.redirect;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import jakarta.servlet.ServletException;

import org.junit.Before;
import org.junit.Test;

public class TrailingSlashRedirectProcessorTest {

    private TrailingSlashRedirectProcessor redirectProcessor;

    @Before
    public void setUp() throws Exception {
        this.redirectProcessor = new TrailingSlashRedirectProcessor();
    }

    @Test
    public void testHandleParameterEndsWithSlash() {
        assertEquals(null, this.redirectProcessor.getRedirectURL("/l", "", "u=/r/"));
    }

    @Test
    public void testHandleRequestRedirectSlash() throws ServletException, IOException {
        assertEquals("/foo/bar/index.html", this.redirectProcessor.getRedirectURL("/bar/", "/foo", null));
    }
}
