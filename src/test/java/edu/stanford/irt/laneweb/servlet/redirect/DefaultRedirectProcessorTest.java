package edu.stanford.irt.laneweb.servlet.redirect;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.ServletException;

import org.junit.Before;
import org.junit.Test;

public class DefaultRedirectProcessorTest {

    private DefaultRedirectProcessor redirectProcessor;

    @Test
    public void foo() {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("((?!.*/biomed-resources.*).*)/bassett/(.*)");
        java.util.regex.Matcher matcher = pattern.matcher("/biomed-resources/bassett/index.html");
        org.junit.Assert.assertFalse(matcher.matches());
    }

    @Before
    public void setUp() throws Exception {
        this.redirectProcessor = new DefaultRedirectProcessor();
    }

    @Test
    public void testHandleRequestRedirect() throws ServletException, IOException {
        this.redirectProcessor.setRedirectMap(Collections.singletonMap("(.*)/", "$1/index.html"));
        assertEquals("/foo/index.html", this.redirectProcessor.getRedirectURL("/foo/", "", null));
    }

    @Test
    public void testHandleRequestRedirectClasses() throws ServletException, IOException {
        this.redirectProcessor.setRedirectMap(
                Collections.singletonMap("(.*)/classes/index.html", "$1/services/workshops/laneclasses.html"));
        assertEquals("/services/workshops/laneclasses.html",
                this.redirectProcessor.getRedirectURL("/classes/index.html", "", null));
    }

    @Test
    public void testHandleRequestRedirectClinician() throws ServletException, IOException {
        this.redirectProcessor
                .setRedirectMap(Collections.singletonMap("(.*)/clinician/index.html", "$1/portals/clinical.html"));
        assertEquals("/foo/bar/portals/clinical.html",
                this.redirectProcessor.getRedirectURL("/foo/bar/clinician/index.html", "", null));
    }
}
