package edu.stanford.irt.laneweb.servlet.binding.user;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.gt;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.codec.PersistentLoginToken;
import edu.stanford.irt.laneweb.codec.UserCookieCodec;
import edu.stanford.irt.laneweb.servlet.CookieName;
import edu.stanford.irt.laneweb.user.User;

public class CookieUserFactoryTest {

    private UserCookieCodec codec;

    private Cookie cookie;

    private CookieUserFactory factory;

    private Logger log;

    private HttpServletRequest request;

    private PersistentLoginToken token;

    private User user;

    private String userIdHashKey;

    @Before
    public void setUp() {
        this.userIdHashKey = "key";
        this.log = createMock(Logger.class);
        this.codec = createMock(UserCookieCodec.class);
        this.factory = new CookieUserFactory(this.codec, this.log, this.userIdHashKey);
        this.request = createMock(HttpServletRequest.class);
        this.cookie = createMock(Cookie.class);
        this.token = createMock(PersistentLoginToken.class);
        this.user = createMock(User.class);
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
        replay(this.log, this.codec, this.request, this.cookie, this.token, this.user);
        assertSame(this.user, this.factory.createUser(this.request));
        verify(this.log, this.codec, this.request, this.cookie, this.token, this.user);
    }

    @Test
    public void testCreateUserCookieCodecException() {
        expect(this.request.getCookies()).andReturn(new Cookie[] { this.cookie, this.cookie, this.cookie });
        expect(this.request.getHeader("User-Agent")).andReturn("useragent");
        expect(this.cookie.getName()).andReturn("name");
        expect(this.cookie.getName()).andReturn(CookieName.USER.toString());
        expect(this.cookie.getValue()).andReturn("value").times(2);
        LanewebException ex = new LanewebException("invalid encryptedValue");
        expect(this.codec.restoreLoginToken("value", "key")).andThrow(ex);
        this.log.error("failed to decode userid from: value", ex);
        replay(this.log, this.codec, this.request, this.cookie, this.token, this.user);
        assertNull(this.factory.createUser(this.request));
        verify(this.log, this.codec, this.request, this.cookie, this.token, this.user);
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
        replay(this.log, this.codec, this.request, this.cookie, this.token, this.user);
        assertNull(this.factory.createUser(this.request));
        verify(this.log, this.codec, this.request, this.cookie, this.token, this.user);
    }
}
