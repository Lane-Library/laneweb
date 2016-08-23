package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

public class PersistentLoginHandlerInterceptorTest {

    private Cookie cookie;

    private Object handler;

    private PersistentLoginHandlerInterceptor interceptor;

    private HttpServletRequest request;

    private HttpServletResponse response;

    @Before
    public void setUp() {
        this.interceptor = new PersistentLoginHandlerInterceptor();
        this.request = createMock(HttpServletRequest.class);
        this.response = createMock(HttpServletResponse.class);
        this.handler = null;
        this.cookie = createMock(Cookie.class);
    }

    @Test
    public void testPreHandleIsPersistentCookieValueFoo() throws IOException {
        expect(this.request.getRequestURI()).andReturn("uri").times(2);
        expect(this.request.getQueryString()).andReturn("query-string").times(2);
        expect(this.request.getCookies()).andReturn(new Cookie[] { this.cookie });
        expect(this.cookie.getName()).andReturn("isPersistent");
        expect(this.cookie.getValue()).andReturn("foo");
        replay(this.request, this.response, this.cookie);
        assertTrue(this.interceptor.preHandle(this.request, this.response, this.handler));
        verify(this.request, this.response, this.cookie);
    }

    @Test
    public void testPreHandleIsPersistentCookieValueYes() throws IOException {
        expect(this.request.getRequestURI()).andReturn("uri");
        expect(this.request.getQueryString()).andReturn("query-string").times(2);
        expect(this.request.getCookies()).andReturn(new Cookie[] { this.cookie });
        expect(this.cookie.getName()).andReturn("isPersistent");
        expect(this.cookie.getValue()).andReturn("yes");
        this.response.addCookie(isA(Cookie.class));
        expect(this.request.getContextPath()).andReturn("/stage");
        this.response.sendRedirect("/stage/secure/persistentLogin.html?pl=true&url=uri%3Fquery-string");
        replay(this.request, this.response, this.cookie);
        assertFalse(this.interceptor.preHandle(this.request, this.response, this.handler));
        verify(this.request, this.response, this.cookie);
    }

    @Test
    public void testPreHandleNoQueryString() throws IOException {
        expect(this.request.getRequestURI()).andReturn("uri").times(2);
        expect(this.request.getQueryString()).andReturn(null);
        expect(this.request.getCookies()).andReturn(null);
        replay(this.request, this.response, this.cookie);
        assertTrue(this.interceptor.preHandle(this.request, this.response, this.handler));
        verify(this.request, this.response, this.cookie);
    }

    @Test
    public void testPreHandleNotCookie() throws IOException {
        expect(this.request.getRequestURI()).andReturn("uri").times(2);
        expect(this.request.getQueryString()).andReturn("query-string").times(2);
        expect(this.request.getCookies()).andReturn(new Cookie[] { this.cookie });
        expect(this.cookie.getName()).andReturn("name");
        replay(this.request, this.response, this.cookie);
        assertTrue(this.interceptor.preHandle(this.request, this.response, this.handler));
        verify(this.request, this.response, this.cookie);
    }

    @Test
    public void testPreHandleNullCookies() throws IOException {
        expect(this.request.getRequestURI()).andReturn("uri").times(2);
        expect(this.request.getQueryString()).andReturn("query-string").times(2);
        expect(this.request.getCookies()).andReturn(null);
        replay(this.request, this.response, this.cookie);
        assertTrue(this.interceptor.preHandle(this.request, this.response, this.handler));
        verify(this.request, this.response, this.cookie);
    }

    @Test
    public void testPreHandleSecureLogin() throws IOException {
        expect(this.request.getRequestURI()).andReturn("/secure/login.html").times(2);
        expect(this.request.getParameter("url")).andReturn("url");
        expect(this.request.getCookies()).andReturn(new Cookie[] { this.cookie });
        expect(this.cookie.getName()).andReturn("name");
        expect(this.request.getContextPath()).andReturn("/stage");
        this.response.sendRedirect("/stage/secure/persistentLogin.html?pl=false&url=url");
        replay(this.request, this.response, this.cookie);
        assertFalse(this.interceptor.preHandle(this.request, this.response, this.handler));
        verify(this.request, this.response, this.cookie);
    }

    @Test
    public void testPreHandleSecureLoginNullURL() throws IOException {
        expect(this.request.getRequestURI()).andReturn("/secure/login.html").times(2);
        expect(this.request.getParameter("url")).andReturn(null);
        expect(this.request.getCookies()).andReturn(new Cookie[] { this.cookie });
        expect(this.cookie.getName()).andReturn("name");
        expect(this.request.getContextPath()).andReturn("/stage");
        this.response.sendRedirect("/stage/secure/persistentLogin.html?pl=false&url=%2Findex.html");
        replay(this.request, this.response, this.cookie);
        assertFalse(this.interceptor.preHandle(this.request, this.response, this.handler));
        verify(this.request, this.response, this.cookie);
    }
}
