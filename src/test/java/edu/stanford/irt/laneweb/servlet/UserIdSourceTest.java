package edu.stanford.irt.laneweb.servlet;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import edu.stanford.irt.laneweb.codec.UserIdCookieCodec;
import edu.stanford.irt.laneweb.model.Model;

public class UserIdSourceTest {

    private UserIdCookieCodec codec = new UserIdCookieCodec("key");

    private Cookie cookie;

    private Logger log;

    private HttpServletRequest request;

    private HttpSession session;

    private UserIdSource useridSource;

    @Before
    public void setUp() throws Exception {
        this.codec = new UserIdCookieCodec("key");
        this.log = createMock(Logger.class);
        this.useridSource = new UserIdSource(this.codec, this.log);
        this.request = createMock(HttpServletRequest.class);
        this.session = createMock(HttpSession.class);
        this.cookie = createMock(Cookie.class);
    }

    @Test
    public void testBadCookie() {
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.request.getRemoteUser()).andReturn(null);
        expect(this.request.getCookies()).andReturn(new Cookie[] { this.cookie });
        expect(this.request.getHeader("User-Agent")).andReturn("user agent");
        expect(this.cookie.getName()).andReturn(UserIdCookieCodec.LANE_COOKIE_NAME);
        expect(this.session.getAttribute(Model.USER_ID)).andReturn(null);
        expect(this.cookie.getValue()).andReturn("abc").times(2);
        replay(this.request, this.session, this.cookie);
        assertEquals(null, this.useridSource.getUserId(this.request));
        verify(this.request, this.session, this.cookie);
    }

    @Test
    public void testCookieNotValid() {
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.request.getRemoteUser()).andReturn(null);
        expect(this.request.getCookies()).andReturn(new Cookie[] { this.cookie });
        expect(this.request.getHeader("User-Agent")).andReturn("user agent");
        expect(this.cookie.getName()).andReturn(UserIdCookieCodec.LANE_COOKIE_NAME);
        expect(this.session.getAttribute(Model.USER_ID)).andReturn(null);
        String value = this.codec.createLoginToken("ditenus", "different".hashCode()).getEncryptedValue();
        expect(this.cookie.getValue()).andReturn(value);
        replay(this.request, this.session, this.cookie);
        assertNull(this.useridSource.getUserId(this.request));
        verify(this.request, this.session, this.cookie);
    }

    @Test
    public void testInCookie() {
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.request.getRemoteUser()).andReturn(null);
        expect(this.request.getCookies()).andReturn(new Cookie[] { this.cookie });
        expect(this.request.getHeader("User-Agent")).andReturn("user agent");
        expect(this.cookie.getName()).andReturn(UserIdCookieCodec.LANE_COOKIE_NAME);
        expect(this.session.getAttribute(Model.USER_ID)).andReturn(null);
        this.session.setAttribute(Model.USER_ID, "ditenus");
        String value = this.codec.createLoginToken("ditenus", "user agent".hashCode()).getEncryptedValue();
        expect(this.cookie.getValue()).andReturn(value);
        replay(this.request, this.session, this.cookie);
        assertEquals("ditenus", this.useridSource.getUserId(this.request));
        verify(this.request, this.session, this.cookie);
    }

    @Test
    public void testIsInRemoteUser() {
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.USER_ID)).andReturn(null);
        expect(this.request.getRemoteUser()).andReturn("ditenus");
        this.session.setAttribute(Model.USER_ID, "ditenus");
        replay(this.request, this.session, this.cookie);
        assertEquals("ditenus", this.useridSource.getUserId(this.request));
        verify(this.request, this.session, this.cookie);
    }

    @Test
    public void testIsInSession() {
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.USER_ID)).andReturn("ditenus");
        replay(this.request, this.session, this.cookie);
        assertEquals("ditenus", this.useridSource.getUserId(this.request));
        verify(this.request, this.session, this.cookie);
    }

    @Test
    public void testNotRightCookie() {
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.USER_ID)).andReturn(null);
        expect(this.request.getRemoteUser()).andReturn(null);
        expect(this.request.getCookies()).andReturn(new Cookie[] { this.cookie });
        expect(this.request.getHeader("User-Agent")).andReturn("userAgent");
        expect(this.cookie.getName()).andReturn("not " + UserIdCookieCodec.LANE_COOKIE_NAME);
        replay(this.request, this.session, this.cookie);
        assertNull(this.useridSource.getUserId(this.request));
        verify(this.request, this.session, this.cookie);
    }

    @Test
    public void testNoUserNoCookies() {
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.USER_ID)).andReturn(null);
        expect(this.request.getRemoteUser()).andReturn(null);
        expect(this.request.getCookies()).andReturn(new Cookie[0]);
        expect(this.request.getHeader("User-Agent")).andReturn("userAgent");
        replay(this.request, this.session, this.cookie);
        assertNull(this.useridSource.getUserId(this.request));
        verify(this.request, this.session, this.cookie);
    }

    @Test
    public void testNullCookies() {
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.USER_ID)).andReturn(null);
        expect(this.request.getRemoteUser()).andReturn(null);
        expect(this.request.getCookies()).andReturn(null);
        expect(this.request.getHeader("User-Agent")).andReturn("userAgent");
        replay(this.request, this.session, this.cookie);
        assertNull(this.useridSource.getUserId(this.request));
        verify(this.request, this.session, this.cookie);
    }

    @Test
    public void testNullUserAgent() {
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.USER_ID)).andReturn(null);
        expect(this.request.getRemoteUser()).andReturn(null);
        expect(this.request.getCookies()).andReturn(new Cookie[] { this.cookie });
        expect(this.request.getHeader("User-Agent")).andReturn(null);
        replay(this.request, this.session, this.cookie);
        assertNull(this.useridSource.getUserId(this.request));
        verify(this.request, this.session, this.cookie);
    }
}
