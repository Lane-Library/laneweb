package edu.stanford.irt.laneweb.servlet.mvc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class GoogleSiteVerificationControllerTest {

    @Test
    public void testGetVerificationString() {
        assertEquals("google-site-verification: google708f1eef3c6d1e52.html",
                new GoogleSiteVerificationController().getVerificationString());
    }
}
