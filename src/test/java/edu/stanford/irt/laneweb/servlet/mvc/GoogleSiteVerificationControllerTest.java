package edu.stanford.irt.laneweb.servlet.mvc;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GoogleSiteVerificationControllerTest {

    @Test
    public void testGetVerificationString() {
        assertEquals("google-site-verification: google708f1eef3c6d1e52.html",
                new GoogleSiteVerificationController().getVerificationString());
    }
}
