package edu.stanford.irt.laneweb.servlet.redirect;

import static org.junit.Assert.*;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;


public class NewPageRedirectProcessorTest {
    
    private NewPageRedirectProcessor redirectProcessor;

    @Before
    public void setUp() throws Exception {
        this.redirectProcessor = new NewPageRedirectProcessor();
    }

    @Test
    public void testGetRedirectURL() {
        this.redirectProcessor.setRedirectMap(Collections.singletonMap("/online/ej.html(.*)", "/newpage.html?page=/biomed-resources/ej.html$1"));
        assertEquals("/newpage.html?page=/biomed-resources/ej.html", this.redirectProcessor.getRedirectURL("/online/ej.html", "", null));
        assertEquals("/stage/newpage.html?page=/stage/biomed-resources/ej.html", this.redirectProcessor.getRedirectURL("/online/ej.html", "/stage", null));
        assertEquals("/rzwies/newpage.html?page=/rzwies/biomed-resources/ej.html?a=z", this.redirectProcessor.getRedirectURL("/online/ej.html", "/rzwies", "a=z"));
    }
}
