package edu.stanford.irt.laneweb.codec;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Clock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.user.User;

public class UserCookieCodecTest {

    private Clock clock;

    private UserCookieCodec codec;

    @BeforeEach
    public void setUp() {
        this.clock = mock(Clock.class);
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

    @Test
    public void testCreateLoginTokenNullUserId() {
        assertThrows(NullPointerException.class, () -> {
            this.codec.createLoginToken(null, 0);
        });
    }

    @Test
    public void testLongKey() {
        assertNotNull(new UserCookieCodec("keykeykeykeykeykeykeykeykeykeykeykeykeykeykey", this.clock));
    }

    @Test
    public void testRestoreLoginTokenBadValue() {
        assertThrows(LanewebException.class, () -> {
            this.codec.restoreLoginToken("abc", "def");
        });
    }

    @Test
    public void testRestoreLoginTokenNullValue() {
        assertThrows(NullPointerException.class, () -> {
            this.codec.restoreLoginToken(null, null);
        });
    }
}
