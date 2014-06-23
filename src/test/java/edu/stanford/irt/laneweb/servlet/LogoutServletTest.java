package edu.stanford.irt.laneweb.servlet;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
    public void testService() throws ServletException, IOException {
        this.request = createMock(HttpServletRequest.class);
        this.response = createMock(HttpServletResponse.class);
        this.session = createMock(HttpSession.class);
        expect(this.request.getSession(false)).andReturn(this.session);
        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie("test", "test");
        expect(this.request.getCookies()).andReturn(cookies);
        this.response.addCookie(isA(Cookie.class));
        expectLastCall().times(4);
        this.session.invalidate();
        this.response.sendRedirect("https://weblogin.stanford.edu/logout");
        replay(this.request, this.response, this.session);
        this.servlet.service(this.request, this.response);
        verify(this.request, this.response, this.session);
    }

    @Test
    public void testServiceNullCookiesAndSession() throws ServletException, IOException {
        this.request = createMock(HttpServletRequest.class);
        this.response = createMock(HttpServletResponse.class);
        this.session = createMock(HttpSession.class);
        expect(this.request.getSession(false)).andReturn(null);
        expect(this.request.getCookies()).andReturn(null);
        this.response.addCookie(isA(Cookie.class));
        expectLastCall().times(4);
        this.response.sendRedirect("https://weblogin.stanford.edu/logout");
        replay(this.request, this.response, this.session);
        this.servlet.service(this.request, this.response);
        verify(this.request, this.response, this.session);
    }

    @Test
    public void testServicePersistent() throws ServletException, IOException {
        this.request = createMock(HttpServletRequest.class);
        this.response = createMock(HttpServletResponse.class);
        this.session = createMock(HttpSession.class);
        expect(this.request.getSession(false)).andReturn(this.session);
        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie("persistent-preference", "test");
        expect(this.request.getCookies()).andReturn(cookies);
        this.response.addCookie(isA(Cookie.class));
        expectLastCall().times(5);
        this.session.invalidate();
        this.response.sendRedirect("https://weblogin.stanford.edu/logout");
        replay(this.request, this.response, this.session);
        this.servlet.service(this.request, this.response);
        verify(this.request, this.response, this.session);
    }

    @Test
    public void testServicePersistentDenied() throws ServletException, IOException {
        this.request = createMock(HttpServletRequest.class);
        this.response = createMock(HttpServletResponse.class);
        this.session = createMock(HttpSession.class);
        expect(this.request.getSession(false)).andReturn(this.session);
        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie("persistent-preference", "denied");
        expect(this.request.getCookies()).andReturn(cookies);
        this.response.addCookie(isA(Cookie.class));
        expectLastCall().times(4);
        this.session.invalidate();
        this.response.sendRedirect("https://weblogin.stanford.edu/logout");
        replay(this.request, this.response, this.session);
        this.servlet.service(this.request, this.response);
        verify(this.request, this.response, this.session);
    }
}