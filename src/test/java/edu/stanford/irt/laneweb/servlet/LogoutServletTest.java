package edu.stanford.irt.laneweb.servlet;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

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
    public void testServiceHttpServletRequestHttpServletResponse() throws ServletException, IOException {
        this.request = createMock(HttpServletRequest.class);
        this.response = createMock(HttpServletResponse.class);
        this.session = createMock(HttpSession.class);
        expect(this.request.getSession(false)).andReturn(this.session);
        this.response.addCookie(isA(Cookie.class));
        expectLastCall().times(3);
        this.session.invalidate();
        this.response.sendRedirect("https://weblogin.stanford.edu/logout");
        replay(this.request, this.response, this.session);
        this.servlet.service(this.request, this.response);
        verify(this.request, this.response, this.session);
    }
}
