package edu.stanford.irt.laneweb.servlet.redirect;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.servlet.ServletException;

import org.junit.jupiter.api.Test;

public class DefaultRedirectProcessorTest {

    @Test
    public void testBiomedResourcesAndBassett() {
        Pattern pattern = Pattern.compile("((?!.*/biomed-resources.*).*)/bassett/(.*)");
        Matcher matcher = pattern.matcher("/biomed-resources/bassett/index.html");
        assertFalse(matcher.matches());
    }

    @Test
    public void testHandleRequestRedirect() throws ServletException, IOException {
        DefaultRedirectProcessor redirectProcessor = new DefaultRedirectProcessor(
                Collections.singletonMap("(.*)/", "$1/index.html"));
        assertEquals("/foo/index.html", redirectProcessor.getRedirectURL("/foo/", "", null));
    }

    @Test
    public void testHandleRequestRedirectClasses() throws ServletException, IOException {
        DefaultRedirectProcessor redirectProcessor = new DefaultRedirectProcessor(
                Collections.singletonMap("(.*)/classes/index.html", "$1/services/workshops/laneclasses.html"));
        assertEquals("/services/workshops/laneclasses.html",
                redirectProcessor.getRedirectURL("/classes/index.html", "", null));
    }

    @Test
    public void testHandleRequestRedirectClinician() throws ServletException, IOException {
        DefaultRedirectProcessor redirectProcessor = new DefaultRedirectProcessor(
                Collections.singletonMap("(.*)/clinician/index.html", "$1/portals/clinical.html"));
        assertEquals("/foo/bar/portals/clinical.html",
                redirectProcessor.getRedirectURL("/foo/bar/clinician/index.html", "", null));
    }
}
