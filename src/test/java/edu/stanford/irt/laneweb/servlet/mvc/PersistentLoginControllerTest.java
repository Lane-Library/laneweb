package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.view.RedirectView;

import edu.stanford.irt.laneweb.servlet.SunetIdCookieCodec;
import edu.stanford.irt.laneweb.servlet.SunetIdSource;

public class PersistentLoginControllerTest {

    private SunetIdCookieCodec codec;

    private PersistentLoginController persistenLoginController;

    private HttpServletRequest request;

    private HttpServletResponse response;

    private HttpSession session;

    private SunetIdSource sunetIdSource;

    private String url = "/test.html";

    @Before
    public void setUp() throws Exception {
        this.persistenLoginController = new PersistentLoginController();
        this.request = createMock(HttpServletRequest.class);
        this.response = createMock(HttpServletResponse.class);
        this.session = createMock(HttpSession.class);
        this.response.addCookie(isA(Cookie.class));
        this.response.addCookie(isA(Cookie.class));
        this.codec = new SunetIdCookieCodec("key");
        this.persistenLoginController.setSunetIdCookieCodec(this.codec);
        this.sunetIdSource = createMock(SunetIdSource.class);
        this.persistenLoginController.setSunetIdSource(this.sunetIdSource);
    }

    @Test
    public void testCreateCookieNotNullUrl() throws UnsupportedEncodingException {
        expect(this.sunetIdSource.getSunetid(this.request)).andReturn("alainb");
        expect(this.request.getHeader("User-Agent")).andReturn("firefox");
        this.response.addCookie(isA(Cookie.class));
        replay(this.sunetIdSource, this.request, this.response, this.session);
        RedirectView view = (RedirectView) this.persistenLoginController.createCookie(this.url, this.request, this.response);
        assertEquals(view.getUrl(), "/test.html");
        verify(this.sunetIdSource, this.request, this.session, this.response);
    }

    @Test
    public void testCreateCookieNullUrl() throws UnsupportedEncodingException {
        expect(this.sunetIdSource.getSunetid(this.request)).andReturn("alainb");
        expect(this.request.getHeader("User-Agent")).andReturn("firefox");
        this.response.addCookie(isA(Cookie.class));
        this.response.setCharacterEncoding(isA(String.class));
        replay(this.sunetIdSource, this.request, this.response, this.session);
        RedirectView view = (RedirectView) this.persistenLoginController.createCookie(null, this.request, this.response);
        assertEquals(view.getUrl(), "/myaccounts.html");
        verify(this.sunetIdSource, this.request, this.session, this.response);
    }

    @Test
    public void testCreateCookieSunetIdNull() throws UnsupportedEncodingException {
        expect(this.sunetIdSource.getSunetid(this.request)).andReturn(null);
        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie(PersistentLoginController.PERSISTENT_LOGIN_PREFERENCE, null);
        this.response.addCookie(isA(Cookie.class));
        this.response.addCookie(isA(Cookie.class));
        expect(this.request.getCookies()).andReturn(cookies);
        replay(this.sunetIdSource, this.request, this.session, this.response);
        this.persistenLoginController.createCookie(this.url, this.request, this.response);
        verify(this.sunetIdSource, this.request, this.session, this.response);
    }

    @Test
    public void testRemoveCookieUrlNotNull() throws UnsupportedEncodingException {
        expect(this.sunetIdSource.getSunetid(this.request)).andReturn("alainb");
        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie(PersistentLoginController.PERSISTENT_LOGIN_PREFERENCE, "234890");
        expect(this.request.getCookies()).andReturn(cookies);
        this.response.addCookie(isA(Cookie.class));
        replay(this.sunetIdSource, this.request, this.session, this.response);
        RedirectView view = (RedirectView) this.persistenLoginController.removeCookieAndView(this.url, this.request, this.response);
        assertEquals(view.getUrl(), "/test.html");
        verify(this.sunetIdSource, this.request, this.session, this.response);
    }

    @Test
    public void testRemoveCookieUrlNull() throws UnsupportedEncodingException {
        expect(this.sunetIdSource.getSunetid(this.request)).andReturn("alainb");
        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie(PersistentLoginController.PERSISTENT_LOGIN_PREFERENCE, "234033");
        expect(this.request.getCookies()).andReturn(cookies);
        this.response.addCookie(isA(Cookie.class));
        this.response.setCharacterEncoding(isA(String.class));
        replay(this.sunetIdSource, this.request, this.session, this.response);
        RedirectView view = (RedirectView) this.persistenLoginController.removeCookieAndView(null, this.request, this.response);
        assertEquals(view.getUrl(), "/myaccounts.html");
        verify(this.sunetIdSource, this.request, this.session, this.response);
    }

    @Test
    public void testRemoveWithDeniedCookie() throws UnsupportedEncodingException {
        expect(this.sunetIdSource.getSunetid(this.request)).andReturn("alainb");
        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie(PersistentLoginController.PERSISTENT_LOGIN_PREFERENCE, "denied");
        expect(this.request.getCookies()).andReturn(cookies);
        replay(this.sunetIdSource, this.request, this.session, this.response);
        RedirectView view = (RedirectView) this.persistenLoginController.removeCookieAndView(this.url, this.request, this.response);
        assertEquals(view.getUrl(), "/test.html");
        verify(this.sunetIdSource, this.request, this.session, this.response);
    }

    @Test
    public void testRenewCookieActiveSunetId() throws UnsupportedEncodingException {
        expect(this.sunetIdSource.getSunetid(this.request)).andReturn("alainb");
        expect(this.session.getAttribute("isActiveSunetID")).andReturn(true);
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.request.getHeader("User-Agent")).andReturn("firefox");
        this.response.addCookie(isA(Cookie.class));
        replay(this.sunetIdSource, this.request, this.response, this.session);
        RedirectView view = (RedirectView) this.persistenLoginController.renewCookieAndRedirect(this.url, this.request,
                this.response);
        assertEquals(view.getUrl(), "/test.html");
        verify(this.sunetIdSource, this.request, this.session, this.response);
    }

    @Test
    public void testRenewCookieNotActiveSunetId() throws UnsupportedEncodingException {
        expect(this.session.getAttribute("isActiveSunetID")).andReturn(false);
        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie(PersistentLoginController.PERSISTENT_LOGIN_PREFERENCE, null);
        expect(this.request.getCookies()).andReturn(cookies);
        expect(this.request.getSession()).andReturn(this.session);
        this.response.addCookie(isA(Cookie.class));
        this.response.addCookie(isA(Cookie.class));
        replay(this.sunetIdSource, this.request, this.session, this.response);
        RedirectView view = (RedirectView) this.persistenLoginController.renewCookieAndRedirect(this.url, this.request,
                this.response);
        assertEquals(view.getUrl(), "/test.html");
        verify(this.sunetIdSource, this.request, this.session, this.response);
    }
}
