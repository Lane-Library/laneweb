package edu.stanford.irt.laneweb.servlet.binding;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.CookieName;

public class LoginExpirationCookieDataBinderTest {

    private LoginExpirationCookieDataBinder binder;

    private Cookie cookie;

    private Map<String, Object> model;

    private HttpServletRequest request;

    @Before
    public void setUp() throws Exception {
        this.binder = new LoginExpirationCookieDataBinder();
        this.model = new HashMap<>();
        this.request = mock(HttpServletRequest.class);
        this.cookie = mock(Cookie.class);
    }

    @Test
    public void testBindBothCookies() {
        expect(this.request.getCookies()).andReturn(new Cookie[] { this.cookie, this.cookie, this.cookie });
        expect(this.cookie.getName()).andReturn(CookieName.EXPIRATION.toString());
        expect(this.cookie.getValue()).andReturn(
                Long.toString(System.currentTimeMillis() + Duration.ofDays(1).plus(Duration.ofMillis(100)).toMillis()));
        replay(this.request, this.cookie);
        this.binder.bind(this.model, this.request);
        assertEquals("1", this.model.get(CookieName.EXPIRATION.toString()));
        verify(this.request, this.cookie);
    }

    @Test
    public void testBindExpirationCookieNow() {
        expect(this.request.getCookies()).andReturn(new Cookie[] { this.cookie });
        expect(this.cookie.getName()).andReturn(CookieName.EXPIRATION.toString());
        expect(this.cookie.getValue()).andReturn(Long.toString(System.currentTimeMillis()));
        replay(this.request, this.cookie);
        this.binder.bind(this.model, this.request);
        assertNull(this.model.get(CookieName.EXPIRATION.toString()));
        verify(this.request, this.cookie);
    }

    @Test
    public void testBindExpirationCookieOneDay() {
        expect(this.request.getCookies()).andReturn(new Cookie[] { this.cookie });
        expect(this.cookie.getName()).andReturn(CookieName.EXPIRATION.toString());
        expect(this.cookie.getValue()).andReturn(
                Long.toString(System.currentTimeMillis() + Duration.ofDays(1).plus(Duration.ofMillis(100)).toMillis()));
        replay(this.request, this.cookie);
        this.binder.bind(this.model, this.request);
        assertEquals("1", this.model.get(CookieName.EXPIRATION.toString()));
        verify(this.request, this.cookie);
    }

    @Test
    public void testBindExpirationNumberFormatException() {
        expect(this.request.getCookies()).andReturn(new Cookie[] { this.cookie });
        expect(this.cookie.getName()).andReturn(CookieName.EXPIRATION.toString());
        expect(this.cookie.getValue()).andReturn("bad number");
        replay(this.request, this.cookie);
        this.binder.bind(this.model, this.request);
        assertEquals("ERROR", this.model.get(CookieName.EXPIRATION.toString()));
        verify(this.request, this.cookie);
    }

    @Test
    public void testBindNoCookies() {
        expect(this.request.getCookies()).andReturn(new Cookie[0]);
        replay(this.request);
        this.binder.bind(this.model, this.request);
        assertFalse(this.model.containsKey(CookieName.EXPIRATION.toString()));
        verify(this.request);
    }

    @Test
    public void testBindNullCookies() {
        expect(this.request.getCookies()).andReturn(null);
        replay(this.request);
        this.binder.bind(this.model, this.request);
        assertFalse(this.model.containsKey(CookieName.EXPIRATION.toString()));
        verify(this.request);
    }

    @Test
    public void testBindOneCookie() {
        expect(this.request.getCookies()).andReturn(new Cookie[] { this.cookie });
        expect(this.cookie.getName()).andReturn("name");
        replay(this.request, this.cookie);
        this.binder.bind(this.model, this.request);
        assertFalse(this.model.containsKey(CookieName.EXPIRATION.toString()));
        verify(this.request, this.cookie);
    }

    @Test
    public void testBindUserCookie() {
        expect(this.request.getCookies()).andReturn(new Cookie[] { this.cookie });
        expect(this.cookie.getName()).andReturn(CookieName.USER.toString());
        replay(this.request, this.cookie);
        this.binder.bind(this.model, this.request);
        assertFalse(this.model.containsKey(CookieName.EXPIRATION.toString()));
        verify(this.request, this.cookie);
    }

    @Test
    public void testEmptyValue() {
        expect(this.request.getCookies()).andReturn(new Cookie[] { this.cookie });
        expect(this.cookie.getName()).andReturn(CookieName.EXPIRATION.toString());
        expect(this.cookie.getValue()).andReturn("");
        replay(this.request, this.cookie);
        this.binder.bind(this.model, this.request);
        assertEquals("ERROR", this.model.get(CookieName.EXPIRATION.toString()));
        verify(this.request, this.cookie);
    }
}
