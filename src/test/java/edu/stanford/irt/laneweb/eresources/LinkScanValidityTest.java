package edu.stanford.irt.laneweb.eresources;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LinkScanValidityTest {

    @Test
    public void testIsValid() {
        assertTrue(new LinkScanValidity().isValid());
    }

    @Test
    public void testIsValidExpired() {
        LinkScanValidity v = new LinkScanValidity(1);
        assertFalse(v.isValid());
        assertTrue(v.isValid());
    }
}
