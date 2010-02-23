package edu.stanford.irt.laneweb.servlet;

import static org.easymock.EasyMock.and;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.LanewebConstants;
import edu.stanford.irt.laneweb.model.LanewebObjectModel;

/**
 * 
 * @author ceyates
 *
 * $Id$
 */
public class SunetIdFilterTest {
    
    private Filter filter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain chain;
    private HttpSession session;
    private Cookie cookie;
    private SunetIdCookieCodec codec = new SunetIdCookieCodec();
    private Capture<Cookie> cookieCapture;

    @Before
    public void setUp() throws Exception {
        this.filter = new SunetIdFilter();
        this.filter.init(null);
        this.request = createMock(HttpServletRequest.class);
        this.response = createMock(HttpServletResponse.class);
        this.chain = createMock(FilterChain.class);
        this.session = createMock(HttpSession.class);
        this.cookie = createMock(Cookie.class);
        this.cookieCapture = new Capture<Cookie>();
    }
    
    @Test
    public void testIsInRemoteUser() throws IOException, ServletException {
        expect(this.request.getRemoteUser()).andReturn("ditenus");
        expect(this.request.getParameter(isA(String.class))).andReturn(null).times(2);
        this.request.setAttribute(LanewebObjectModel.SUNETID, "ditenus");
        this.chain.doFilter(this.request, this.response);
        replayMocks();
        this.filter.doFilter(this.request, this.response, this.chain);
        verifyMocks();
    }
    
    @Test
    public void testIsInXWEBAUTHUSER() throws IOException, ServletException {
        expect(this.request.getRemoteUser()).andReturn(null);
        expect(this.request.getHeader("X-WEBAUTH-USER")).andReturn("ditenus");
        expect(this.request.getParameter(isA(String.class))).andReturn(null).times(2);
        this.request.setAttribute(LanewebObjectModel.SUNETID, "ditenus");
        this.chain.doFilter(this.request, this.response);
        replayMocks();
        this.filter.doFilter(this.request, this.response, this.chain);
        verifyMocks();
    }
    
    @Test
    public void testIsInUser() throws IOException, ServletException {
        expect(this.request.getRemoteUser()).andReturn(null);
        expect(this.request.getHeader("X-WEBAUTH-USER")).andReturn(null);
        expect(this.request.getSession(false)).andReturn(this.session);
        this.request.setAttribute(LanewebObjectModel.SUNETID, "ditenus");
        expect(this.request.getParameter(isA(String.class))).andReturn(null).times(2);
        expect(this.session.getAttribute(LanewebObjectModel.SUNETID)).andReturn("ditenus");
        this.chain.doFilter(this.request, this.response);
        replayMocks();
        this.filter.doFilter(this.request, this.response, this.chain);
        verifyMocks();
    }
    
    @Test
    public void testNoSessionNoCookies() throws IOException, ServletException {
        expect(this.request.getRemoteUser()).andReturn(null);
        expect(this.request.getHeader("X-WEBAUTH-USER")).andReturn(null);
        expect(this.request.getSession(false)).andReturn(null);
        expect(this.request.getCookies()).andReturn(new Cookie[0]);
        expect(this.request.getHeader("User-Agent")).andReturn(null);
        this.chain.doFilter(this.request, this.response);
        replayMocks();
        this.filter.doFilter(this.request, this.response, this.chain);
        verifyMocks();
    }
    
    @Test
    public void testNoUserNoCookies() throws IOException, ServletException {
        expect(this.request.getRemoteUser()).andReturn(null);
        expect(this.request.getHeader("X-WEBAUTH-USER")).andReturn(null);
        expect(this.request.getSession(false)).andReturn(this.session);
        expect(this.request.getCookies()).andReturn(new Cookie[0]);
        expect(this.request.getHeader("User-Agent")).andReturn(null);
        expect(this.session.getAttribute(LanewebObjectModel.SUNETID)).andReturn(null);
        this.chain.doFilter(this.request, this.response);
        replayMocks();
        this.filter.doFilter(this.request, this.response, this.chain);
        verifyMocks();
    }
    
    @Test
    public void testInCookie() throws IOException, ServletException {
        expect(this.request.getRemoteUser()).andReturn(null);
        expect(this.request.getHeader("X-WEBAUTH-USER")).andReturn(null);
        expect(this.request.getSession(false)).andReturn(null);
        expect(this.request.getCookies()).andReturn(new Cookie[]{this.cookie});
        expect(this.request.getHeader("User-Agent")).andReturn("user agent");
        expect(this.request.getParameter(isA(String.class))).andReturn(null).times(2);
        expect(this.cookie.getName()).andReturn(LanewebConstants.LANE_COOKIE_NAME);
        String value = this.codec.createLoginToken("ditenus", "user agent".hashCode()).getEncryptedValue();
        expect(this.cookie.getValue()).andReturn(value);
        this.request.setAttribute(LanewebObjectModel.SUNETID, "ditenus");
        this.chain.doFilter(this.request, this.response);
        replayMocks();
        this.filter.doFilter(this.request, this.response, this.chain);
        verifyMocks();
    }
    
    @Test
    public void testAddUserCookie() throws IOException, ServletException {
        expect(this.request.getRemoteUser()).andReturn("ditenus");
        expect(this.request.getParameter("pl")).andReturn("true");
        this.request.setAttribute(LanewebObjectModel.SUNETID, "ditenus");
        expect(this.request.getHeader("User-Agent")).andReturn("user agent");
        this.response.addCookie(and(isA(Cookie.class), capture(this.cookieCapture)));
        this.chain.doFilter(this.request, this.response);
        replayMocks();
        this.filter.doFilter(this.request, this.response, this.chain);
        assertEquals(LanewebConstants.LANE_COOKIE_NAME, this.cookieCapture.getValue().getName());
        assertEquals(3600 * 24 * 7 * 2, this.cookieCapture.getValue().getMaxAge());
        verifyMocks();
    }
    
    @Test
    public void testRemoveUserCookie() throws IOException, ServletException {
        expect(this.request.getRemoteUser()).andReturn("ditenus");
        expect(this.request.getParameter("pl")).andReturn("false");
        expect(this.request.getParameter("remove-pl")).andReturn("true");
        this.request.setAttribute(LanewebObjectModel.SUNETID, "ditenus");
        this.response.addCookie(and(isA(Cookie.class), capture(this.cookieCapture)));
        this.chain.doFilter(this.request, this.response);
        replayMocks();
        this.filter.doFilter(this.request, this.response, this.chain);
        assertEquals(LanewebConstants.LANE_COOKIE_NAME, this.cookieCapture.getValue().getName());
        assertEquals(0, this.cookieCapture.getValue().getMaxAge());
        verifyMocks();
    }
    
    private void replayMocks() {
        replay(this.request);
        replay(this.response);
        replay(this.chain);
        replay(this.session);
        replay(this.cookie);
    }
    
    private void verifyMocks() {
        verify(this.request);
        verify(this.response);
        verify(this.chain);
        verify(this.session);
        verify(this.cookie);
    }
}
