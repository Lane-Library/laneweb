package edu.stanford.irt.laneweb.codec;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.Clock;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.user.User;

public class UserCookieCodecTest {

    private UserCookieCodec codec;
    private Clock clock;

    @Before
    public void setUp() {
        this.clock = createMock(Clock.class);
        this.codec = new UserCookieCodec("key", this.clock);
    }

    @Test
    public void testCreateAndRestore() {
        expect(this.clock.millis()).andReturn(System.currentTimeMillis());
        replay(this.clock);
        User user = new User("id@domain", "", "", "");
        PersistentLoginToken token = this.codec.createLoginToken(user, 12345);
        assertEquals(this.codec.restoreLoginToken(token.getEncryptedValue(), "").getUser(), user);
        verify(this.clock);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateLoginTokenNullUserId() {
            this.codec.createLoginToken(null, 0);
    }

    @Test
    public void testLongKey() {
        assertNotNull(new UserCookieCodec("keykeykeykeykeykeykeykeykeykeykeykeykeykeykey", this.clock));
    }

    @Test(expected = LanewebException.class)
    public void testRestoreLoginTokenBadValue() {
            this.codec.restoreLoginToken("abc", "def");
    }

    @Test(expected = NullPointerException.class)
    public void testRestoreLoginTokenNullValue() {
            this.codec.restoreLoginToken(null, null);
    }
}
