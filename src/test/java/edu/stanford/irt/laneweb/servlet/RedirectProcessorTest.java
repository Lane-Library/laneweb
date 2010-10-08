package edu.stanford.irt.laneweb.servlet;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.ServletException;

import org.junit.Before;
import org.junit.Test;

public class RedirectProcessorTest {

    private RedirectProcessor redirectProcessor;

    @Test
    public void foo() {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("((?!.*/biomed-resources.*).*)/bassett/(.*)");
        java.util.regex.Matcher matcher = pattern.matcher("/biomed-resources/bassett/index.html");
        org.junit.Assert.assertFalse(matcher.matches());
    }

    @Before
    public void setUp() throws Exception {
        this.redirectProcessor = new RedirectProcessor();
    }

    @Test
    public void testHandleRequestRedirect() throws ServletException, IOException {
        this.redirectProcessor.setRedirectMap(Collections.singletonMap("(.*)/", "$1/index.html"));
        assertEquals("/foo/index.html", this.redirectProcessor.getRedirectURL("/foo/"));
    }

    @Test
    public void testHandleRequestRedirectClasses() throws ServletException, IOException {
        this.redirectProcessor.setRedirectMap(Collections.singletonMap("(.*)/classes/index.html",
                "$1/services/workshops/laneclasses.html"));
        assertEquals("/services/workshops/laneclasses.html",
                this.redirectProcessor.getRedirectURL("/classes/index.html"));
    }

    @Test
    public void testHandleRequestRedirectClinician() throws ServletException, IOException {
        this.redirectProcessor.setRedirectMap(Collections.singletonMap("(.*)/clinician/index.html",
                "$1/portals/clinical.html"));
        assertEquals("/foo/bar/portals/clinical.html",
                this.redirectProcessor.getRedirectURL("/foo/bar/clinician/index.html"));
    }

    @Test
    public void testHandleRequestRedirectSlash() throws ServletException, IOException {
        this.redirectProcessor.setRedirectMap(Collections.singletonMap("((?!.*\\?.*).*)/", "$1/index.html"));
        assertEquals("/index.html", this.redirectProcessor.getRedirectURL("/"));
    }
    
    @Test
    public void testHandleParameterEndsWithSlash() {
        this.redirectProcessor.setRedirectMap(Collections.singletonMap("((?!.*\\?.*).*)/","$1/index.html"));
        assertEquals(RedirectProcessor.NO_REDIRECT, this.redirectProcessor.getRedirectURL("/l?u=/r/"));
    }
    
//    @Test
//    public void testUniprintRedirect() {
//        this.redirectProcessor.setRedirectMap(Collections.singletonMap("((?!.*/secure.*).*)/uniprint/(.*)", "$1/secure/uniprint/$2"));
//        assertEquals("/secure/uniprint/index.html", this.redirectProcessor.getRedirectURL("/uniprint/index.html"));
//        assertEquals(RedirectProcessor.NO_REDIRECT, this.redirectProcessor.getRedirectURL("/secure/uniprint/index.html"));
//        assertEquals("/foo/secure/uniprint/index.html", this.redirectProcessor.getRedirectURL("/foo/uniprint/index.html"));
//    }
}
