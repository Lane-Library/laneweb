package edu.stanford.irt.laneweb.codec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.user.User;

public class PersistentLoginTokenTest {

    private User user = new User("id@domain", "name", "email", "hashKey");

    @Test
    public void testGetEncryptedValue() {
        assertEquals("encryptedValue",
                new PersistentLoginToken(this.user, 0L, 0, "encryptedValue").getEncryptedValue());
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
        long oneDay = Duration.ofDays(1).toMillis();
        assertTrue(new PersistentLoginToken(this.user, 0L, 0, "encryptedValue").isValidFor(oneDay, 0));
    }

    @Test
    public void testIsValidForFourWeeksAndADay() {
        long twoWeeksAndADay = Duration.ofDays(28).plus(Duration.ofDays(1)).toMillis();
        assertFalse(new PersistentLoginToken(this.user, 0L, 0, "encryptedValue").isValidFor(twoWeeksAndADay, 0));
    }

    @Test
    public void testNullEncryptedValue() {
        assertThrows(NullPointerException.class, () -> {
            new PersistentLoginToken(this.user, 0L, 0, null);
        });
    }

    @Test
    public void testNullUser() {
        assertThrows(NullPointerException.class, () -> {
            new PersistentLoginToken(null, 0L, 0, "");
        });
    }
}
