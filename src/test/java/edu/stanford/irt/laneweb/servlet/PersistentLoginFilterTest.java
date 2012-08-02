package edu.stanford.irt.laneweb.servlet;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.Model;

//TODO: use the captures to assert things about cookies
public class PersistentLoginFilterTest {

    private FilterChain chain;

    private PersistentLoginFilter filter;

    private FilterConfig filterConfig;

    private HttpServletRequest request;

    private HttpServletResponse response;

    private HttpSession session;

    @Before
    public void setUp() throws Exception {
        this.filter = new PersistentLoginFilter();
        this.filterConfig = createMock(FilterConfig.class);
        this.request = createMock(HttpServletRequest.class);
        this.response = createMock(HttpServletResponse.class);
        this.chain = createMock(FilterChain.class);
        this.session = createMock(HttpSession.class);
    }

    @Test
    public void testInternalDoFilterNotActive() throws IOException, ServletException {
        expect(this.filterConfig.getInitParameter("laneweb.sunetidcookiecodec.key")).andReturn("key");
        expect(this.request.getSession()).andReturn(this.session).times(2);
        expect(this.session.getAttribute(Model.SUNETID)).andReturn("ditenus");
        expect(this.session.getAttribute(Model.IS_ACTIVE_SUNETID)).andReturn(Boolean.FALSE);
        expect(this.request.getParameter("pl")).andReturn("renew");
        Capture<Cookie> cookie1 = new Capture<Cookie>();
        this.response.addCookie(capture(cookie1));
        expect(this.request.getParameter("url")).andReturn(null);
        this.chain.doFilter(this.request, this.response);
        replay(this.filterConfig, this.request, this.response, this.chain, this.session);
        this.filter.init(this.filterConfig);
        this.filter.internalDoFilter(this.request, this.response, this.chain);
        verify(this.filterConfig, this.request, this.response, this.chain, this.session);
    }

    @Test
    public void testInternalDoFilterNullActive() throws IOException, ServletException {
        expect(this.filterConfig.getInitParameter("laneweb.sunetidcookiecodec.key")).andReturn("key");
        expect(this.request.getSession()).andReturn(this.session).times(2);
        expect(this.session.getAttribute(Model.SUNETID)).andReturn("ditenus");
        expect(this.session.getAttribute(Model.IS_ACTIVE_SUNETID)).andReturn(null);
        expect(this.request.getParameter("pl")).andReturn("renew");
        Capture<Cookie> cookie1 = new Capture<Cookie>();
        this.response.addCookie(capture(cookie1));
        expect(this.request.getParameter("url")).andReturn(null);
        this.chain.doFilter(this.request, this.response);
        replay(this.filterConfig, this.request, this.response, this.chain, this.session);
        this.filter.init(this.filterConfig);
        this.filter.internalDoFilter(this.request, this.response, this.chain);
        verify(this.filterConfig, this.request, this.response, this.chain, this.session);
    }

    @Test
    public void testInternalDoFilterNullSunetid() throws IOException, ServletException {
        expect(this.filterConfig.getInitParameter("laneweb.sunetidcookiecodec.key")).andReturn("key");
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.SUNETID)).andReturn(null);
        expect(this.request.getRemoteUser()).andReturn(null);
        expect(this.request.getCookies()).andReturn(null);
        expect(this.request.getHeader("User-Agent")).andReturn(null);
        Capture<Cookie> cookie1 = new Capture<Cookie>();
        this.response.addCookie(capture(cookie1));
        expect(this.request.getParameter("url")).andReturn(null);
        this.chain.doFilter(this.request, this.response);
        replay(this.filterConfig, this.request, this.response, this.chain, this.session);
        this.filter.init(this.filterConfig);
        this.filter.internalDoFilter(this.request, this.response, this.chain);
        verify(this.filterConfig, this.request, this.response, this.chain, this.session);
    }

    @Test
    public void testInternalDoFilterNullSunetidURL() throws IOException, ServletException {
        expect(this.filterConfig.getInitParameter("laneweb.sunetidcookiecodec.key")).andReturn("key");
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.SUNETID)).andReturn(null);
        expect(this.request.getRemoteUser()).andReturn(null);
        expect(this.request.getCookies()).andReturn(null);
        expect(this.request.getHeader("User-Agent")).andReturn(null);
        Capture<Cookie> cookie1 = new Capture<Cookie>();
        this.response.addCookie(capture(cookie1));
        expect(this.request.getParameter("url")).andReturn("url");
        this.response.sendRedirect("url");
        replay(this.filterConfig, this.request, this.response, this.chain, this.session);
        this.filter.init(this.filterConfig);
        this.filter.internalDoFilter(this.request, this.response, this.chain);
        verify(this.filterConfig, this.request, this.response, this.chain, this.session);
    }

    @Test
    public void testInternalDoFilterNullSunetidURLEmpty() throws IOException, ServletException {
        expect(this.filterConfig.getInitParameter("laneweb.sunetidcookiecodec.key")).andReturn("key");
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.SUNETID)).andReturn(null);
        expect(this.request.getRemoteUser()).andReturn(null);
        expect(this.request.getCookies()).andReturn(null);
        expect(this.request.getHeader("User-Agent")).andReturn(null);
        Capture<Cookie> cookie1 = new Capture<Cookie>();
        this.response.addCookie(capture(cookie1));
        expect(this.request.getParameter("url")).andReturn("");
        this.chain.doFilter(this.request, this.response);
        replay(this.filterConfig, this.request, this.response, this.chain, this.session);
        this.filter.init(this.filterConfig);
        this.filter.internalDoFilter(this.request, this.response, this.chain);
        verify(this.filterConfig, this.request, this.response, this.chain, this.session);
    }

    @Test
    public void testInternalDoFilterPLFalse() throws IOException, ServletException {
        expect(this.filterConfig.getInitParameter("laneweb.sunetidcookiecodec.key")).andReturn("key");
        expect(this.request.getSession()).andReturn(this.session).times(2);
        expect(this.session.getAttribute(Model.SUNETID)).andReturn("ditenus");
        expect(this.session.getAttribute(Model.IS_ACTIVE_SUNETID)).andReturn(Boolean.FALSE);
        expect(this.request.getParameter("pl")).andReturn("false");
        Capture<Cookie> cookie1 = new Capture<Cookie>();
        this.response.addCookie(capture(cookie1));
        expect(this.request.getParameter("url")).andReturn(null);
        this.chain.doFilter(this.request, this.response);
        replay(this.filterConfig, this.request, this.response, this.chain, this.session);
        this.filter.init(this.filterConfig);
        this.filter.internalDoFilter(this.request, this.response, this.chain);
        verify(this.filterConfig, this.request, this.response, this.chain, this.session);
    }

    @Test
    public void testInternalDoFilterPLNull() throws IOException, ServletException {
        expect(this.filterConfig.getInitParameter("laneweb.sunetidcookiecodec.key")).andReturn("key");
        expect(this.request.getSession()).andReturn(this.session).times(2);
        expect(this.session.getAttribute(Model.SUNETID)).andReturn("ditenus");
        expect(this.session.getAttribute(Model.IS_ACTIVE_SUNETID)).andReturn(Boolean.FALSE);
        expect(this.request.getParameter("pl")).andReturn(null);
        Capture<Cookie> cookie1 = new Capture<Cookie>();
        this.response.addCookie(capture(cookie1));
        expect(this.request.getParameter("url")).andReturn(null);
        this.chain.doFilter(this.request, this.response);
        replay(this.filterConfig, this.request, this.response, this.chain, this.session);
        this.filter.init(this.filterConfig);
        this.filter.internalDoFilter(this.request, this.response, this.chain);
        verify(this.filterConfig, this.request, this.response, this.chain, this.session);
    }

    @Test
    public void testInternalDoFilterPLTrue() throws IOException, ServletException {
        expect(this.filterConfig.getInitParameter("laneweb.sunetidcookiecodec.key")).andReturn("key");
        expect(this.request.getSession()).andReturn(this.session).times(2);
        expect(this.session.getAttribute(Model.SUNETID)).andReturn("ditenus");
        expect(this.session.getAttribute(Model.IS_ACTIVE_SUNETID)).andReturn(Boolean.FALSE);
        expect(this.request.getParameter("pl")).andReturn("true");
        expect(this.request.getHeader("User-Agent")).andReturn(null);
        expect(this.request.getParameter("url")).andReturn(null);
        this.chain.doFilter(this.request, this.response);
        replay(this.filterConfig, this.request, this.response, this.chain, this.session);
        this.filter.init(this.filterConfig);
        this.filter.internalDoFilter(this.request, this.response, this.chain);
        verify(this.filterConfig, this.request, this.response, this.chain, this.session);
    }

    @Test
    public void testInternalDoFilterRenew() throws IOException, ServletException {
        expect(this.filterConfig.getInitParameter("laneweb.sunetidcookiecodec.key")).andReturn("key");
        expect(this.request.getSession()).andReturn(this.session).times(2);
        expect(this.session.getAttribute(Model.SUNETID)).andReturn("ditenus");
        expect(this.session.getAttribute(Model.IS_ACTIVE_SUNETID)).andReturn(Boolean.TRUE);
        expect(this.request.getParameter("pl")).andReturn("renew");
        expect(this.request.getHeader("User-Agent")).andReturn("user agent");
        Capture<Cookie> cookie1 = new Capture<Cookie>();
        this.response.addCookie(capture(cookie1));
        Capture<Cookie> cookie2 = new Capture<Cookie>();
        this.response.addCookie(capture(cookie2));
        expect(this.request.getParameter("url")).andReturn(null);
        this.chain.doFilter(this.request, this.response);
        replay(this.filterConfig, this.request, this.response, this.chain, this.session);
        this.filter.init(this.filterConfig);
        this.filter.internalDoFilter(this.request, this.response, this.chain);
        verify(this.filterConfig, this.request, this.response, this.chain, this.session);
    }
}
