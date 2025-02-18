package edu.stanford.irt.laneweb.servlet.binding.user;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.gt;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.codec.PersistentLoginToken;
import edu.stanford.irt.laneweb.codec.UserCookieCodec;
import edu.stanford.irt.laneweb.servlet.CookieName;
import edu.stanford.irt.laneweb.user.User;

public class CookieUserFactoryTest {

    private UserCookieCodec codec;

    private Cookie cookie;

    private CookieUserFactory factory;

    private HttpServletRequest request;

    private PersistentLoginToken token;

    private User user;

    private String userIdHashKey;

    @BeforeEach
    public void setUp() {
        this.userIdHashKey = "key";
        this.codec = mock(UserCookieCodec.class);
        this.factory = new CookieUserFactory(this.codec, this.userIdHashKey);
        this.request = mock(HttpServletRequest.class);
        this.cookie = mock(Cookie.class);
        this.token = mock(PersistentLoginToken.class);
        this.user = mock(User.class);
    }

    @Test
    public void testCreateUser() {
        expect(this.request.getCookies()).andReturn(new Cookie[] { this.cookie, this.cookie, this.cookie });
        expect(this.request.getHeader("User-Agent")).andReturn("useragent");
        expect(this.cookie.getName()).andReturn("name");
        expect(this.cookie.getName()).andReturn(CookieName.USER.toString());
        expect(this.cookie.getValue()).andReturn("value");
        expect(this.codec.restoreLoginToken("value", "key")).andReturn(this.token);
        expect(this.token.isValidFor(gt(0L), eq("useragent".hashCode()))).andReturn(true);
        expect(this.token.getUser()).andReturn(this.user);
        replay(this.codec, this.request, this.cookie, this.token, this.user);
        assertSame(this.user, this.factory.createUser(this.request));
        verify(this.codec, this.request, this.cookie, this.token, this.user);
    }

    @Test
    public void testCreateUserCookieCodecException() {
        expect(this.request.getCookies()).andReturn(new Cookie[] { this.cookie, this.cookie, this.cookie });
        expect(this.request.getHeader("User-Agent")).andReturn("useragent");
        expect(this.cookie.getName()).andReturn("name");
        expect(this.cookie.getName()).andReturn(CookieName.USER.toString());
        expect(this.cookie.getValue()).andReturn("value");
        LanewebException ex = new LanewebException("invalid encryptedValue");
        expect(this.codec.restoreLoginToken("value", "key")).andThrow(ex);
        replay(this.codec, this.request, this.cookie, this.token, this.user);
        assertNull(this.factory.createUser(this.request));
        verify(this.codec, this.request, this.cookie, this.token, this.user);
    }

    @Test
    public void testCreateUserCookieTokenNotValid() {
        expect(this.request.getCookies()).andReturn(new Cookie[] { this.cookie, this.cookie, this.cookie });
        expect(this.request.getHeader("User-Agent")).andReturn("useragent");
        expect(this.cookie.getName()).andReturn("name");
        expect(this.cookie.getName()).andReturn(CookieName.USER.toString());
        expect(this.cookie.getValue()).andReturn("value");
        expect(this.codec.restoreLoginToken("value", "key")).andReturn(this.token);
        expect(this.token.isValidFor(gt(0L), eq("useragent".hashCode()))).andReturn(false);
        replay(this.codec, this.request, this.cookie, this.token, this.user);
        assertNull(this.factory.createUser(this.request));
        verify(this.codec, this.request, this.cookie, this.token, this.user);
    }

    @Test
    public void testCreateUserEmptyCookieValue() {
        expect(this.request.getCookies()).andReturn(new Cookie[] { this.cookie });
        expect(this.request.getHeader("User-Agent")).andReturn("useragent");
        expect(this.cookie.getName()).andReturn(CookieName.USER.toString());
        expect(this.cookie.getValue()).andReturn("");
        replay(this.request, this.cookie);
        assertNull(this.factory.createUser(this.request));
        verify(this.request, this.cookie);
    }

    @Test
    public void testCreateUserNullUserAgent() {
        expect(this.request.getCookies()).andReturn(new Cookie[] { this.cookie });
        expect(this.request.getHeader("User-Agent")).andReturn(null);
        expect(this.cookie.getName()).andReturn(CookieName.USER.toString());
        expect(this.cookie.getValue()).andReturn("value");
        replay(this.request, this.cookie);
        assertNull(this.factory.createUser(this.request));
        verify(this.request, this.cookie);
    }

    @Test
    public void testNoCookies() {
        expect(this.request.getCookies()).andReturn(null);
        replay(this.request);
        assertNull(this.factory.createUser(this.request));
        verify(this.request);
    }

    @Test
    public void testNoRelevantCookies() {
        expect(this.request.getCookies()).andReturn(new Cookie[] { this.cookie });
        expect(this.cookie.getName()).andReturn("name");
        replay(this.request, this.cookie);
        assertNull(this.factory.createUser(this.request));
        verify(this.request, this.cookie);
    }
}
