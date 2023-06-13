package edu.stanford.irt.laneweb.servlet;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

public class LogoutServletTest {

    private HttpServletRequest request;

    private HttpServletResponse response;

    private HttpServlet servlet;

    private HttpSession session;

    @Before
    public void setUp() throws Exception {
        this.servlet = new LogoutServlet();
    }

    @Test
    public void testNullReferePage() throws ServletException, IOException {
        this.request = mock(HttpServletRequest.class);
        this.response = mock(HttpServletResponse.class);
        this.session = mock(HttpSession.class);
        expect(this.request.getHeader("referer")).andReturn(null);
        expect(this.request.getLocalName()).andReturn("localhost");
        expect(this.request.getSession(false)).andReturn(this.session);
        this.response.addCookie(isA(Cookie.class));
        expectLastCall().times(3);
        this.session.invalidate();
        this.response.sendRedirect("https://localhost/Shibboleth.sso/Logout?return=%2Findex.html");
        replay(this.request, this.response, this.session);
        this.servlet.service(this.request, this.response);
        verify(this.request, this.response, this.session);
    }

    @Test
    public void testService() throws ServletException, IOException {
        this.request = mock(HttpServletRequest.class);
        this.response = mock(HttpServletResponse.class);
        this.session = mock(HttpSession.class);
        expect(this.request.getHeader("referer")).andReturn("/logout.html");
        expect(this.request.getSession(false)).andReturn(this.session);
        expect(this.request.getLocalName()).andReturn("localhost");
        this.response.addCookie(isA(Cookie.class));
        expectLastCall().times(3);
        this.session.invalidate();
        this.response.sendRedirect("https://localhost/Shibboleth.sso/Logout?return=%2Flogout.html");
        replay(this.request, this.response, this.session);
        this.servlet.service(this.request, this.response);
        verify(this.request, this.response, this.session);
    }

    @Test
    public void testServiceNullCookiesAndSession() throws ServletException, IOException {
        this.request = mock(HttpServletRequest.class);
        this.response = mock(HttpServletResponse.class);
        this.session = mock(HttpSession.class);
        expect(this.request.getHeader("referer")).andReturn("/logout.html");
        expect(this.request.getLocalName()).andReturn("localhost");
        expect(this.request.getSession(false)).andReturn(null);
        this.response.addCookie(isA(Cookie.class));
        expectLastCall().times(3);
        this.response.sendRedirect("https://localhost/Shibboleth.sso/Logout?return=%2Flogout.html");
        replay(this.request, this.response, this.session);
        this.servlet.service(this.request, this.response);
        verify(this.request, this.response, this.session);
    }

    @Test
    public void testServicePersistent() throws ServletException, IOException {
        this.request = mock(HttpServletRequest.class);
        this.response = mock(HttpServletResponse.class);
        this.session = mock(HttpSession.class);
        expect(this.request.getHeader("referer")).andReturn("/logout.html");
        expect(this.request.getLocalName()).andReturn("localhost");
        expect(this.request.getSession(false)).andReturn(this.session);
        this.response.addCookie(isA(Cookie.class));
        expectLastCall().times(3);
        this.session.invalidate();
        this.response.sendRedirect("https://localhost/Shibboleth.sso/Logout?return=%2Flogout.html");
        replay(this.request, this.response, this.session);
        this.servlet.service(this.request, this.response);
        verify(this.request, this.response, this.session);
    }

    @Test
    public void testServicePersistentDenied() throws ServletException, IOException {
        this.request = mock(HttpServletRequest.class);
        this.response = mock(HttpServletResponse.class);
        this.session = mock(HttpSession.class);
        expect(this.request.getHeader("referer")).andReturn("/index.html");
        expect(this.request.getLocalName()).andReturn("localhost");
        expect(this.request.getSession(false)).andReturn(this.session);
        this.response.addCookie(isA(Cookie.class));
        expectLastCall().times(3);
        this.session.invalidate();
        this.response.sendRedirect("https://localhost/Shibboleth.sso/Logout?return=%2Findex.html");
        replay(this.request, this.response, this.session);
        this.servlet.service(this.request, this.response);
        verify(this.request, this.response, this.session);
    }

    @Test
    public void testServiceRemoteReferer() throws ServletException, IOException {
        this.request = mock(HttpServletRequest.class);
        this.response = mock(HttpServletResponse.class);
        this.session = mock(HttpSession.class);
        expect(this.request.getHeader("referer")).andReturn("http://evil.server.com/");
        expect(this.request.getSession(false)).andReturn(this.session);
        expect(this.request.getLocalName()).andReturn("localhost");
        this.response.addCookie(isA(Cookie.class));
        expectLastCall().times(3);
        this.session.invalidate();
        this.response.sendRedirect("https://localhost/Shibboleth.sso/Logout?return=%2Findex.html");
        replay(this.request, this.response, this.session);
        this.servlet.service(this.request, this.response);
        verify(this.request, this.response, this.session);
    }
}