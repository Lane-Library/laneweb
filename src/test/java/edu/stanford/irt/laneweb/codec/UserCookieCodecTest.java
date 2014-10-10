package edu.stanford.irt.laneweb.codec;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.user.User;

public class UserCookieCodecTest {

    private UserCookieCodec codec;

    @Test
    public void createAndRestore() {
        User user = new User("id", "", "", "");
        PersistentLoginToken token = this.codec.createLoginToken(user, 12345);
        assertEquals(this.codec.restoreLoginToken(token.getEncryptedValue(), "").getUser(), user);
    }

    @Before
    public void setUp() {
        this.codec = new UserCookieCodec("key");
    }

    @Test
    public void testCreateLoginTokenNullUserId() {
        try {
            this.codec.createLoginToken(null, 0);
            fail();
        } catch (LanewebException e) {
        }
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
        } catch (LanewebException e) {
        }
    }
}
