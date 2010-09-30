package edu.stanford.irt.laneweb.servlet;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.Model;

/**
 * @author ceyates $Id$
 */
public class SunetIdSourceTest {

    private SunetIdCookieCodec codec = new SunetIdCookieCodec();

    // private Capture<Cookie> cookieCapture;
    private Cookie cookie;

    private HttpServletRequest request;

    private HttpSession session;

    private SunetIdSource sunetidSource;

    @Before
    public void setUp() throws Exception {
        this.sunetidSource = new SunetIdSource();
        this.request = createMock(HttpServletRequest.class);
        this.session = createMock(HttpSession.class);
        this.cookie = createMock(Cookie.class);
        // this.cookieCapture = new Capture<Cookie>();
    }

    @Test
    public void testInCookie() throws IOException, ServletException {
        expect(this.request.getRemoteUser()).andReturn(null);
        expect(this.request.getHeader("X-WEBAUTH-USER")).andReturn(null);
        expect(this.request.getCookies()).andReturn(new Cookie[] { this.cookie });
        expect(this.request.getHeader("User-Agent")).andReturn("user agent");
        expect(this.cookie.getName()).andReturn(SunetIdCookieCodec.LANE_COOKIE_NAME);
        expect(this.session.getAttribute("sunetid")).andReturn(null);
        this.session.setAttribute(Model.SUNETID, "ditenus");
        String value = this.codec.createLoginToken("ditenus", "user agent".hashCode()).getEncryptedValue();
        expect(this.cookie.getValue()).andReturn(value);
        replay(this.request, this.session, this.cookie);
        assertEquals("ditenus", this.sunetidSource.getSunetid(this.request, this.session));
        verify(this.request, this.session, this.cookie);
    }

    @Test
    public void testIsInRemoteUser() throws IOException, ServletException {
        expect(this.session.getAttribute(Model.SUNETID)).andReturn(null);
        expect(this.request.getRemoteUser()).andReturn("ditenus");
        this.session.setAttribute(Model.SUNETID, "ditenus");
        replay(this.request, this.session, this.cookie);
        assertEquals("ditenus", this.sunetidSource.getSunetid(this.request, this.session));
        verify(this.request, this.session, this.cookie);
    }

    @Test
    public void testIsInSession() throws IOException, ServletException {
        expect(this.session.getAttribute(Model.SUNETID)).andReturn("ditenus");
        replay(this.request, this.session, this.cookie);
        assertEquals("ditenus", this.sunetidSource.getSunetid(this.request, this.session));
        verify(this.request, this.session, this.cookie);
    }

    @Test
    public void testIsInXWEBAUTHUSER() throws IOException, ServletException {
        expect(this.session.getAttribute(Model.SUNETID)).andReturn(null);
        expect(this.request.getRemoteUser()).andReturn(null);
        expect(this.request.getHeader("X-WEBAUTH-USER")).andReturn("ditenus");
        this.session.setAttribute(Model.SUNETID, "ditenus");
        replay(this.request, this.session, this.cookie);
        assertEquals("ditenus", this.sunetidSource.getSunetid(this.request, this.session));
        verify(this.request, this.session, this.cookie);
    }

    @Test
    public void testNoUserNoCookies() throws IOException, ServletException {
        expect(this.session.getAttribute(Model.SUNETID)).andReturn(null);
        expect(this.request.getRemoteUser()).andReturn(null);
        expect(this.request.getHeader("X-WEBAUTH-USER")).andReturn(null);
        expect(this.request.getCookies()).andReturn(new Cookie[0]);
        expect(this.request.getHeader("User-Agent")).andReturn(null);
        replay(this.request, this.session, this.cookie);
        this.sunetidSource.getSunetid(this.request, this.session);
        verify(this.request, this.session, this.cookie);
    }
    
    @Test
    public void testBadCookie() {
        expect(this.request.getRemoteUser()).andReturn(null);
        expect(this.request.getHeader("X-WEBAUTH-USER")).andReturn(null);
        expect(this.request.getCookies()).andReturn(new Cookie[] { this.cookie });
        expect(this.request.getHeader("User-Agent")).andReturn("user agent");
        expect(this.cookie.getName()).andReturn(SunetIdCookieCodec.LANE_COOKIE_NAME);
        expect(this.session.getAttribute("sunetid")).andReturn(null);
        expect(this.cookie.getValue()).andReturn("abc").times(2);
        replay(this.request, this.session, this.cookie);
        assertEquals(null, this.sunetidSource.getSunetid(this.request, this.session));
        verify(this.request, this.session, this.cookie);
    }

    // @Test
    // public void testAddUserCookie() throws IOException, ServletException {
    // expect(this.request.getRemoteUser()).andReturn("ditenus");
    // expect(this.request.getParameter("pl")).andReturn("true");
    // expect(this.request.getHeader("User-Agent")).andReturn("user agent");
    // replayMocks();
    // assertEquals("ditenus", this.sunetidSource.getSunetid(this.request, this.session));
    // assertEquals(LanewebConstants.LANE_COOKIE_NAME, this.cookieCapture.getValue().getName());
    // assertEquals(3600 * 24 * 7 * 2, this.cookieCapture.getValue().getMaxAge());
    // verifyMocks();
    // }
    //
    // @Test
    // public void testRemoveUserCookie() throws IOException, ServletException {
    // expect(this.request.getRemoteUser()).andReturn("ditenus");
    // expect(this.request.getParameter("pl")).andReturn("false");
    // expect(this.request.getParameter("remove-pl")).andReturn("true");
    // this.request.setAttribute(Model.SUNETID, "ditenus");
    // replayMocks();
    // this.sunetidSource.getSunetid(this.request, this.session);
    // assertEquals(LanewebConstants.LANE_COOKIE_NAME, this.cookieCapture.getValue().getName());
    // assertEquals(0, this.cookieCapture.getValue().getMaxAge());
    // verifyMocks();
    // }
}
