package edu.stanford.irt.laneweb.servlet;

import static org.junit.Assert.*;
import static org.easymock.classextension.EasyMock.*;

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
    
    private HttpServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private Cookie cookie;
    private HttpSession session;

    @Before
    public void setUp() throws Exception {
        this.servlet = new LogoutServlet();
    }

    @Test
    public void testServiceHttpServletRequestHttpServletResponse() throws ServletException, IOException {
        this.request = createMock(HttpServletRequest.class);
        this.response = createMock(HttpServletResponse.class);
        this.cookie = createMock(Cookie.class);
        this.session = createMock(HttpSession.class);
        expect(this.request.getCookies()).andReturn(new Cookie[]{this.cookie, this.cookie, this.cookie});
        expect(this.cookie.getName()).andReturn("user");
        expect(this.cookie.getName()).andReturn("webauth_at");
        expect(this.cookie.getName()).andReturn("something else");
        this.cookie.setMaxAge(0);
        expectLastCall().times(2);
        this.response.addCookie(this.cookie);
        expectLastCall().times(2);
        expect(this.request.getSession(false)).andReturn(this.session);
        this.session.invalidate();
        this.response.sendRedirect("https://weblogin.stanford.edu/logout");
        replay(this.request);
        replay(this.response);
        replay(this.cookie);
        replay(this.session);
        this.servlet.service(this.request, this.response);
        verify(this.request);
        verify(this.response);
        verify(this.cookie);
        verify(this.session);
    }
}
