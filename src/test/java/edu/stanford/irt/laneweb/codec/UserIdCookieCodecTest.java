package edu.stanford.irt.laneweb.codec;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.LanewebException;

public class UserIdCookieCodecTest {

    private UserIdCookieCodec codec;

    @Test
    public void createAndRestore() {
        PersistentLoginToken token = this.codec.createLoginToken("ditenus", 12345);
        assertEquals(this.codec.restoreLoginToken(token.getEncryptedValue()).getUserId(), "ditenus");
    }

    @Before
    public void setUp() {
        this.codec = new UserIdCookieCodec("key");
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
            this.codec.restoreLoginToken("abc");
            fail();
        } catch (LanewebException e) {
        }
    }

    @Test
    public void testRestoreLoginTokenNullValue() {
        try {
            this.codec.restoreLoginToken(null);
            fail();
        } catch (LanewebException e) {
        }
    }
}
