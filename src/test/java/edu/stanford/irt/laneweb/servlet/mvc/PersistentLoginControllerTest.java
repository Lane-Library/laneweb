package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.servlet.PersistentLoginFilter;
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
    public void testCreateCookieNotNullUrl() {
        expect(this.sunetIdSource.getSunetid(this.request)).andReturn("alainb");
        expect(this.request.getHeader("User-Agent")).andReturn("firefox");
        expect(this.request.getScheme()).andReturn("http");
        this.response.addCookie(isA(Cookie.class));
        replay(this.sunetIdSource, this.request, this.response, this.session);
        String viewUrl = this.persistenLoginController.createCookie(this.url, this.request, this.response);
        assertEquals(viewUrl, "redirect:/test.html");
        verify(this.sunetIdSource, this.request, this.session, this.response);
    }

    @Test
    public void testCreateCookieNotNullUrlHttps() {
        expect(this.sunetIdSource.getSunetid(this.request)).andReturn("alainb");
        expect(this.request.getHeader("User-Agent")).andReturn("firefox");
        expect(this.request.getScheme()).andReturn("https");
        this.response.addCookie(isA(Cookie.class));
        replay(this.sunetIdSource, this.request, this.response, this.session);
        String viewUrl = this.persistenLoginController.createCookie(this.url, this.request, this.response);
        assertEquals(viewUrl, "redirect:/test.html?pca=true");
        verify(this.sunetIdSource, this.request, this.session, this.response);
    }

    @Test
    public void testCreateCookieNullUrl() {
        expect(this.sunetIdSource.getSunetid(this.request)).andReturn("alainb");
        expect(this.request.getHeader("User-Agent")).andReturn("firefox");
        expect(this.request.getScheme()).andReturn("http");
        this.response.addCookie(isA(Cookie.class));
        this.response.setCharacterEncoding(isA(String.class));
        replay(this.sunetIdSource, this.request, this.response, this.session);
        String viewUrl = this.persistenLoginController.createCookie(null, this.request, this.response);
        assertEquals(viewUrl, "redirect:/myaccounts.html");
        verify(this.sunetIdSource, this.request, this.session, this.response);
    }

    @Test
    public void testCreateCookieNullUrlHttps() {
        expect(this.sunetIdSource.getSunetid(this.request)).andReturn("alainb");
        expect(this.request.getHeader("User-Agent")).andReturn("firefox");
        expect(this.request.getScheme()).andReturn("https");
        this.response.addCookie(isA(Cookie.class));
        this.response.setCharacterEncoding(isA(String.class));
        replay(this.sunetIdSource, this.request, this.response, this.session);
        String viewUrl = this.persistenLoginController.createCookie(null, this.request, this.response);
        assertEquals(viewUrl, "redirect:/myaccounts.html?pca=true");
        verify(this.sunetIdSource, this.request, this.session, this.response);
    }

    @Test
    public void testCreateCookieSunetIdNull() {
        expect(this.sunetIdSource.getSunetid(this.request)).andReturn(null);
        expect(this.request.getScheme()).andReturn("http");
        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie(PersistentLoginFilter.PERSISTENT_LOGIN_PREFERENCE, null);
        this.response.addCookie(isA(Cookie.class));
        this.response.addCookie(isA(Cookie.class));
        expect(this.request.getCookies()).andReturn(cookies);
        replay(this.sunetIdSource, this.request, this.session, this.response);
        this.persistenLoginController.createCookie(this.url, this.request, this.response);
        verify(this.sunetIdSource, this.request, this.session, this.response);
    }

    @Test
    public void testRemoveCookieUrlNotNull() {
        expect(this.sunetIdSource.getSunetid(this.request)).andReturn("alainb");
        expect(this.request.getScheme()).andReturn("http");
        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie(PersistentLoginFilter.PERSISTENT_LOGIN_PREFERENCE, "234890");
        expect(this.request.getCookies()).andReturn(cookies);
        this.response.addCookie(isA(Cookie.class));
        replay(this.sunetIdSource, this.request, this.session, this.response);
        String viewUrl = this.persistenLoginController.removeCookieAndView(this.url, this.request, this.response);
        assertEquals(viewUrl, "redirect:/test.html");
        verify(this.sunetIdSource, this.request, this.session, this.response);
    }

    @Test
    public void testRemoveCookieUrlNotNullHttps() {
        expect(this.sunetIdSource.getSunetid(this.request)).andReturn("alainb");
        expect(this.request.getScheme()).andReturn("https");
        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie(PersistentLoginFilter.PERSISTENT_LOGIN_PREFERENCE, "234890");
        expect(this.request.getCookies()).andReturn(cookies);
        this.response.addCookie(isA(Cookie.class));
        replay(this.sunetIdSource, this.request, this.session, this.response);
        String viewUrl = this.persistenLoginController.removeCookieAndView(this.url, this.request, this.response);
        assertEquals(viewUrl, "redirect:/test.html?pca=false");
        verify(this.sunetIdSource, this.request, this.session, this.response);
    }

    @Test
    public void testRemoveCookieUrlNull() {
        expect(this.sunetIdSource.getSunetid(this.request)).andReturn("alainb");
        expect(this.request.getScheme()).andReturn("http");
        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie(PersistentLoginFilter.PERSISTENT_LOGIN_PREFERENCE, "234033");
        expect(this.request.getCookies()).andReturn(cookies);
        this.response.addCookie(isA(Cookie.class));
        this.response.setCharacterEncoding(isA(String.class));
        replay(this.sunetIdSource, this.request, this.session, this.response);
        String viewUrl = this.persistenLoginController.removeCookieAndView(null, this.request, this.response);
        assertEquals(viewUrl, "redirect:/myaccounts.html");
        verify(this.sunetIdSource, this.request, this.session, this.response);
    }

    @Test
    public void testRemoveCookieUrlNullHttps() {
        expect(this.sunetIdSource.getSunetid(this.request)).andReturn("alainb");
        expect(this.request.getScheme()).andReturn("https");
        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie(PersistentLoginFilter.PERSISTENT_LOGIN_PREFERENCE, "234033");
        expect(this.request.getCookies()).andReturn(cookies);
        this.response.addCookie(isA(Cookie.class));
        this.response.setCharacterEncoding(isA(String.class));
        replay(this.sunetIdSource, this.request, this.session, this.response);
        String viewUrl = this.persistenLoginController.removeCookieAndView(null, this.request, this.response);
        assertEquals(viewUrl, "redirect:/myaccounts.html?pca=false");
        verify(this.sunetIdSource, this.request, this.session, this.response);
    }

    @Test
    public void testRemoveWithDeniedCookie() {
        expect(this.sunetIdSource.getSunetid(this.request)).andReturn("alainb");
        expect(this.request.getScheme()).andReturn("http");
        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie(PersistentLoginFilter.PERSISTENT_LOGIN_PREFERENCE, "denied");
        expect(this.request.getCookies()).andReturn(cookies);
        replay(this.sunetIdSource, this.request, this.session, this.response);
        String viewUrl = this.persistenLoginController.removeCookieAndView(this.url, this.request, this.response);
        assertEquals(viewUrl, "redirect:/test.html");
        verify(this.sunetIdSource, this.request, this.session, this.response);
    }

    @Test
    public void testRemoveWithDeniedCookieHttps() {
        expect(this.sunetIdSource.getSunetid(this.request)).andReturn("alainb");
        expect(this.request.getScheme()).andReturn("https");
        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie(PersistentLoginFilter.PERSISTENT_LOGIN_PREFERENCE, "denied");
        expect(this.request.getCookies()).andReturn(cookies);
        replay(this.sunetIdSource, this.request, this.session, this.response);
        String viewUrl = this.persistenLoginController.removeCookieAndView(this.url, this.request, this.response);
        assertEquals(viewUrl, "redirect:/test.html?pca=false");
        verify(this.sunetIdSource, this.request, this.session, this.response);
    }

    @Test
    public void testRenewCookieActiveSunetId() {
        expect(this.sunetIdSource.getSunetid(this.request)).andReturn("alainb");
        expect(this.request.getScheme()).andReturn("http");
        expect(this.session.getAttribute("isActiveSunetID")).andReturn(true);
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.request.getHeader("User-Agent")).andReturn("firefox");
        this.response.addCookie(isA(Cookie.class));
        replay(this.sunetIdSource, this.request, this.response, this.session);
        String viewUrl = this.persistenLoginController.renewCookieAndRedirect(this.url, this.request, this.response);
        assertEquals(viewUrl, "redirect:/test.html");
        verify(this.sunetIdSource, this.request, this.session, this.response);
    }

    @Test
    public void testRenewCookieActiveSunetIdHttps() {
        expect(this.sunetIdSource.getSunetid(this.request)).andReturn("alainb");
        expect(this.request.getScheme()).andReturn("https");
        expect(this.session.getAttribute("isActiveSunetID")).andReturn(true);
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.request.getHeader("User-Agent")).andReturn("firefox");
        this.response.addCookie(isA(Cookie.class));
        replay(this.sunetIdSource, this.request, this.response, this.session);
        String viewUrl = this.persistenLoginController.renewCookieAndRedirect(this.url, this.request, this.response);
        assertEquals(viewUrl, "redirect:/test.html?pca=renew");
        verify(this.sunetIdSource, this.request, this.session, this.response);
    }

    @Test
    public void testRenewCookieNotActiveSunetId() {
        expect(this.session.getAttribute("isActiveSunetID")).andReturn(false);
        expect(this.request.getScheme()).andReturn("http");
        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie(PersistentLoginFilter.PERSISTENT_LOGIN_PREFERENCE, null);
        expect(this.request.getCookies()).andReturn(cookies);
        expect(this.request.getSession()).andReturn(this.session);
        this.response.addCookie(isA(Cookie.class));
        this.response.addCookie(isA(Cookie.class));
        replay(this.sunetIdSource, this.request, this.session, this.response);
        String viewUrl = this.persistenLoginController.renewCookieAndRedirect(this.url, this.request, this.response);
        assertEquals(viewUrl, "redirect:/test.html");
        verify(this.sunetIdSource, this.request, this.session, this.response);
    }

    @Test
    public void testRenewCookieNotActiveSunetIdHttps() {
        expect(this.session.getAttribute("isActiveSunetID")).andReturn(false);
        expect(this.request.getScheme()).andReturn("https");
        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie(PersistentLoginFilter.PERSISTENT_LOGIN_PREFERENCE, null);
        expect(this.request.getCookies()).andReturn(cookies);
        expect(this.request.getSession()).andReturn(this.session);
        this.response.addCookie(isA(Cookie.class));
        this.response.addCookie(isA(Cookie.class));
        replay(this.sunetIdSource, this.request, this.session, this.response);
        String viewUrl = this.persistenLoginController.renewCookieAndRedirect(this.url, this.request, this.response);
        assertEquals(viewUrl, "redirect:/test.html?pca=renew");
        verify(this.sunetIdSource, this.request, this.session, this.response);
    }
}
