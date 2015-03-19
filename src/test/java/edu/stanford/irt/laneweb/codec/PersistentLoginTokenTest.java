package edu.stanford.irt.laneweb.codec;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.user.User;

public class PersistentLoginTokenTest {

    private User user = new User("id@domain", "name", "email", "hashKey");

    @Test
    public void testGetEncryptedValue() {
        assertEquals("encryptedValue", new PersistentLoginToken(this.user, 0L, 0, "encryptedValue").getEncryptedValue());
    }

    @Test
    public void testGetUser() {
        assertSame(this.user, new PersistentLoginToken(this.user, 0L, 0, "encryptedValue").getUser());
    }

    @Test
    public void testIsValidForDifferentUserAgent() {
        assertFalse(new PersistentLoginToken(this.user, 0L, 0, "encryptedValue").isValidFor(0L, 1));
    }

    @Test
    public void testIsValidForOneDay() {
        long oneDay = 1000 * 60 * 60 * 24;
        assertTrue(new PersistentLoginToken(this.user, 0L, 0, "encryptedValue").isValidFor(oneDay, 0));
    }

    @Test
    public void testIsValidForTwoWeeksAndADay() {
        long woWeeksAndADay = 1000 * 60 * 60 * 24 * 7 * 2 + (1000 * 60 * 60 * 24);
        assertFalse(new PersistentLoginToken(this.user, 0L, 0, "encryptedValue").isValidFor(woWeeksAndADay, 0));
    }

    @Test(expected = NullPointerException.class)
    public void testNullEncryptedValue() {
        new PersistentLoginToken(this.user, 0L, 0, null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullUser() {
        new PersistentLoginToken(null, 0L, 0, "");
    }
}
