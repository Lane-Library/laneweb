package edu.stanford.irt.laneweb.servlet.binding;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.CookieName;

public class LoginExpirationCookieDataBinderTest {

    private LoginExpirationCookieDataBinder binder;

    private Cookie cookie;

    private Logger logger;

    private Map<String, Object> model;

    private HttpServletRequest request;

    @Before
    public void setUp() throws Exception {
        this.logger = createMock(Logger.class);
        this.binder = new LoginExpirationCookieDataBinder(this.logger);
        this.model = new HashMap<String, Object>();
        this.request = createMock(HttpServletRequest.class);
        this.cookie = createMock(Cookie.class);
    }

    @Test
    public void testBindBothCookies() {
        expect(this.request.getCookies()).andReturn(new Cookie[] { this.cookie, this.cookie, this.cookie });
        expect(this.cookie.getName()).andReturn(CookieName.EXPIRATION.toString());
        expect(this.cookie.getValue()).andReturn(
                Long.toString(System.currentTimeMillis() + (1000 * 60 * 60 * 24) + 100));
//        expect(this.cookie.getName()).andReturn(CookieName.USER.toString());
//        expect(this.cookie.getValue()).andReturn("value");
        replay(this.request, this.cookie);
        this.binder.bind(this.model, this.request);
        assertEquals("1", this.model.get(Model.PERSISTENT_LOGIN_EXPIRATION_DATE));
        verify(this.request, this.cookie);
    }

    @Test
    public void testBindExpirationCookieNow() {
        expect(this.request.getCookies()).andReturn(new Cookie[] { this.cookie });
        expect(this.cookie.getName()).andReturn(CookieName.EXPIRATION.toString());
        expect(this.cookie.getValue()).andReturn(Long.toString(System.currentTimeMillis()));
        replay(this.request, this.cookie);
        this.binder.bind(this.model, this.request);
        assertNull(this.model.get(Model.PERSISTENT_LOGIN_EXPIRATION_DATE));
        verify(this.request, this.cookie);
    }

    @Test
    public void testBindExpirationCookieOneDay() {
        expect(this.request.getCookies()).andReturn(new Cookie[] { this.cookie });
        expect(this.cookie.getName()).andReturn(CookieName.EXPIRATION.toString());
        expect(this.cookie.getValue()).andReturn(
                Long.toString(System.currentTimeMillis() + (1000 * 60 * 60 * 24) + 100));
        replay(this.request, this.cookie);
        this.binder.bind(this.model, this.request);
        assertEquals("1", this.model.get(Model.PERSISTENT_LOGIN_EXPIRATION_DATE));
        verify(this.request, this.cookie);
    }

    @Test
    public void testBindExpirationNumberFormatException() {
        expect(this.request.getCookies()).andReturn(new Cookie[] { this.cookie });
        expect(this.cookie.getName()).andReturn(CookieName.EXPIRATION.toString());
        expect(this.cookie.getValue()).andReturn("bad number");
        this.logger.error(eq("For input string: \"bad number\""), isA(NumberFormatException.class));
        replay(this.request, this.cookie, this.logger);
        this.binder.bind(this.model, this.request);
        assertEquals("ERROR", this.model.get(Model.PERSISTENT_LOGIN_EXPIRATION_DATE));
        verify(this.request, this.cookie, this.logger);
    }

    @Test
    public void testBindNoCookies() {
        expect(this.request.getCookies()).andReturn(new Cookie[0]);
        replay(this.request);
        this.binder.bind(this.model, this.request);
        verify(this.request);
    }

    @Test
    public void testBindNullCookies() {
        expect(this.request.getCookies()).andReturn(null);
        replay(this.request);
        this.binder.bind(this.model, this.request);
        verify(this.request);
    }

    @Test
    public void testBindOneCookie() {
        expect(this.request.getCookies()).andReturn(new Cookie[] { this.cookie });
        expect(this.cookie.getName()).andReturn("name");
        replay(this.request, this.cookie);
        this.binder.bind(this.model, this.request);
        verify(this.request, this.cookie);
    }

    @Test
    public void testBindUserCookie() {
        expect(this.request.getCookies()).andReturn(new Cookie[] { this.cookie });
        expect(this.cookie.getName()).andReturn(CookieName.USER.toString());
        replay(this.request, this.cookie);
        this.binder.bind(this.model, this.request);
        verify(this.request, this.cookie);
    }
}
