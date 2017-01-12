package edu.stanford.irt.laneweb.codec;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.user.User;

public class UserCookieCodecTest {

    private UserCookieCodec codec;

    @Before
    public void setUp() {
        this.codec = new UserCookieCodec("key");
    }

    @Test
    public void testCreateAndRestore() {
        User user = new User("id@domain", "", "", "");
        PersistentLoginToken token = this.codec.createLoginToken(user, 12345);
        assertEquals(this.codec.restoreLoginToken(token.getEncryptedValue(), "").getUser(), user);
    }

    @Test
    public void testCreateLoginTokenNullUserId() {
        try {
            this.codec.createLoginToken(null, 0);
            fail();
        } catch (NullPointerException e) {
        }
    }

    @Test
    public void testLongKey() {
        assertNotNull(new UserCookieCodec("keykeykeykeykeykeykeykeykeykeykeykeykeykeykey"));
    }

    @Test
    public void testRestoreLoginTokenBadValue() {
        try {
            this.codec.restoreLoginToken("abc", "def");
            fail();
        } catch (LanewebException e) {
        }
    }

    @Test
    public void testRestoreLoginTokenNullValue() {
        try {
            this.codec.restoreLoginToken(null, null);
            fail();
        } catch (NullPointerException e) {
        }
    }
}
