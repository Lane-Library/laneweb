package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.codec.PersistentLoginToken;
import edu.stanford.irt.laneweb.codec.UserCookieCodec;
import edu.stanford.irt.laneweb.servlet.CookieName;
import edu.stanford.irt.laneweb.servlet.binding.UserDataBinder;
import edu.stanford.irt.laneweb.user.LDAPData;
import edu.stanford.irt.laneweb.user.LDAPDataAccess;
import edu.stanford.irt.laneweb.user.User;

public class PersistentLoginControllerTest {

    private UserCookieCodec codec;

    private LDAPData data;

    private LDAPDataAccess ldap;

    private PersistentLoginController persistenLoginController;

    private HttpServletRequest request;

    private HttpServletResponse response;

    private HttpSession session;

    private PersistentLoginToken token;

    private String url = "/test.html";

    private User user;

    private UserDataBinder userSource;

    @Before
    public void setUp() throws Exception {
        this.userSource = createMock(UserDataBinder.class);
        this.ldap = createMock(LDAPDataAccess.class);
        this.codec = createMock(UserCookieCodec.class);
        this.persistenLoginController = new PersistentLoginController(this.userSource, this.ldap, this.codec);
        this.request = createMock(HttpServletRequest.class);
        this.response = createMock(HttpServletResponse.class);
        this.session = createMock(HttpSession.class);
        this.user = createMock(User.class);
        this.token = createMock(PersistentLoginToken.class);
        this.data = createMock(LDAPData.class);
    }

    @Test
    public void testDisablePersistentLoginUrlNotNull() {
        Capture<Cookie> cookie1 = newCapture();
        Capture<Cookie> cookie2 = newCapture();
        this.response.addCookie(capture(cookie1));
        this.response.addCookie(capture(cookie2));
        replay(this.userSource, this.ldap, this.codec, this.request, this.response, this.session, this.user,
                this.token, this.data);
        String redirect = this.persistenLoginController.disablePersistentLogin(null, this.user, this.url, this.request,
                this.response);
        assertEquals(redirect, "redirect:/test.html");
        assertCookieDeleted(cookie1.getValue());
        assertCookieDeleted(cookie2.getValue());
        verify(this.userSource, this.ldap, this.codec, this.request, this.response, this.session, this.user,
                this.token, this.data);
    }

    @Test
    public void testDisablePersistentLoginUrlNull() {
        Capture<Cookie> cookie1 = newCapture();
        Capture<Cookie> cookie2 = newCapture();
        this.response.addCookie(capture(cookie1));
        this.response.addCookie(capture(cookie2));
        replay(this.userSource, this.ldap, this.codec, this.request, this.response, this.session, this.user,
                this.token, this.data);
        String redirect = this.persistenLoginController.disablePersistentLogin(null, this.user, null, this.request,
                this.response);
        assertEquals(redirect, "redirect:/myaccounts.html");
        assertCookieDeleted(cookie1.getValue());
        assertCookieDeleted(cookie2.getValue());
        verify(this.userSource, this.ldap, this.codec, this.request, this.response, this.session, this.user,
                this.token, this.data);
    }

    @Test
    public void testEnablePersistentLoginNotNullUrl() {
        Capture<Cookie> cookie1 = newCapture();
        Capture<Cookie> cookie2 = newCapture();
        this.response.addCookie(capture(cookie1));
        this.response.addCookie(capture(cookie2));
        expect(this.request.getHeader("User-Agent")).andReturn("firefox");
        expect(this.codec.createLoginToken(this.user, "firefox".hashCode())).andReturn(this.token);
        expect(this.token.getEncryptedValue()).andReturn("encryptedValue");
        replay(this.userSource, this.ldap, this.codec, this.request, this.response, this.session, this.user,
                this.token, this.data);
        String redirect = this.persistenLoginController.enablePersistentLogin(null, this.user, this.url, this.request,
                this.response);
        assertEquals(redirect, "redirect:/test.html");
        assertEquals(1209600, cookie1.getValue().getMaxAge());
        assertEquals("encryptedValue", cookie1.getValue().getValue());
        assertTrue(System.currentTimeMillis() + (3600 * 24 * 7 * 2 * 1000) - 100 < Long.valueOf(cookie2.getValue()
                .getValue()));
        assertEquals(CookieName.EXPIRATION.toString(), cookie2.getValue().getName());
        verify(this.userSource, this.ldap, this.codec, this.request, this.response, this.session, this.user,
                this.token, this.data);
    }

    @Test
    public void testEnablePersistentLoginNullUrl() {
        Capture<Cookie> cookie1 = newCapture();
        Capture<Cookie> cookie2 = newCapture();
        this.response.addCookie(capture(cookie1));
        this.response.addCookie(capture(cookie2));
        expect(this.request.getHeader("User-Agent")).andReturn("firefox");
        expect(this.codec.createLoginToken(this.user, "firefox".hashCode())).andReturn(this.token);
        expect(this.token.getEncryptedValue()).andReturn("encryptedValue");
        replay(this.userSource, this.ldap, this.codec, this.request, this.response, this.session, this.user,
                this.token, this.data);
        String redirect = this.persistenLoginController.enablePersistentLogin(null, this.user, null, this.request,
                this.response);
        assertEquals(redirect, "redirect:/myaccounts.html");
        assertEquals(1209600, cookie1.getValue().getMaxAge());
        assertEquals("encryptedValue", cookie1.getValue().getValue());
        assertTrue(System.currentTimeMillis() + (3600 * 24 * 7 * 2 * 1000) - 100 < Long.valueOf(cookie2.getValue()
                .getValue()));
        assertEquals(CookieName.EXPIRATION.toString(), cookie2.getValue().getName());
        verify(this.userSource, this.ldap, this.codec, this.request, this.response, this.session, this.user,
                this.token, this.data);
    }

    @Test
    public void testEnablePersistentLoginNullUser() {
        Capture<Cookie> cookie1 = newCapture();
        Capture<Cookie> cookie2 = newCapture();
        this.response.addCookie(capture(cookie1));
        this.response.addCookie(capture(cookie2));
        replay(this.userSource, this.ldap, this.codec, this.request, this.response, this.session, this.user,
                this.token, this.data);
        this.persistenLoginController.enablePersistentLogin(null, null, this.url, this.request, this.response);
        assertCookieDeleted(cookie1.getValue());
        assertCookieDeleted(cookie2.getValue());
        verify(this.userSource, this.ldap, this.codec, this.request, this.response, this.session, this.user,
                this.token, this.data);
    }

    @Test
    public void testRenewPersistentLogin() {
        expect(this.user.isStanfordUser()).andReturn(true);
        expect(this.user.getId()).andReturn("user@stanford.edu");
        expect(this.ldap.getLdapDataForSunetid("user")).andReturn(this.data);
        expect(this.data.isActive()).andReturn(true);
        Capture<Cookie> cookie1 = newCapture();
        Capture<Cookie> cookie2 = newCapture();
        this.response.addCookie(capture(cookie1));
        this.response.addCookie(capture(cookie2));
        expect(this.request.getHeader("User-Agent")).andReturn("firefox");
        expect(this.codec.createLoginToken(this.user, "firefox".hashCode())).andReturn(this.token);
        expect(this.token.getEncryptedValue()).andReturn("encryptedValue");
        replay(this.userSource, this.ldap, this.codec, this.request, this.response, this.session, this.user,
                this.token, this.data);
        assertEquals("redirect:/test.html", this.persistenLoginController.renewPersistentLogin(null, this.user,
                this.url, this.request, this.response));
        assertEquals(1209600, cookie1.getValue().getMaxAge());
        assertEquals("encryptedValue", cookie1.getValue().getValue());
        assertTrue(System.currentTimeMillis() + (3600 * 24 * 7 * 2 * 1000) - 100 < Long.valueOf(cookie2.getValue()
                .getValue()));
        assertEquals(CookieName.EXPIRATION.toString(), cookie2.getValue().getName());
        verify(this.userSource, this.ldap, this.codec, this.request, this.response, this.session, this.user,
                this.token, this.data);
    }

    @Test
    public void testRenewPersistentLoginNotActive() {
        expect(this.user.isStanfordUser()).andReturn(true);
        expect(this.user.getId()).andReturn("user@stanford.edu");
        expect(this.ldap.getLdapDataForSunetid("user")).andReturn(this.data);
        expect(this.data.isActive()).andReturn(false);
        Capture<Cookie> cookie1 = newCapture();
        Capture<Cookie> cookie2 = newCapture();
        this.response.addCookie(capture(cookie1));
        this.response.addCookie(capture(cookie2));
        replay(this.userSource, this.ldap, this.codec, this.request, this.response, this.session, this.user,
                this.token, this.data);
        assertEquals("redirect:/test.html", this.persistenLoginController.renewPersistentLogin(null, this.user,
                this.url, this.request, this.response));
        assertCookieDeleted(cookie1.getValue());
        assertCookieDeleted(cookie2.getValue());
        verify(this.userSource, this.ldap, this.codec, this.request, this.response, this.session, this.user,
                this.token, this.data);
    }

    @Test
    public void testRenewPersistentLoginNotStanford() {
        expect(this.user.isStanfordUser()).andReturn(false);
        Capture<Cookie> cookie1 = newCapture();
        Capture<Cookie> cookie2 = newCapture();
        this.response.addCookie(capture(cookie1));
        this.response.addCookie(capture(cookie2));
        replay(this.userSource, this.ldap, this.codec, this.request, this.response, this.session, this.user,
                this.token, this.data);
        assertEquals("redirect:/test.html", this.persistenLoginController.renewPersistentLogin(null, this.user,
                this.url, this.request, this.response));
        assertCookieDeleted(cookie1.getValue());
        assertCookieDeleted(cookie2.getValue());
        verify(this.userSource, this.ldap, this.codec, this.request, this.response, this.session, this.user,
                this.token, this.data);
    }

    @Test
    public void testRenewPersistentLoginNullUser() {
        Capture<Cookie> cookie1 = newCapture();
        Capture<Cookie> cookie2 = newCapture();
        this.response.addCookie(capture(cookie1));
        this.response.addCookie(capture(cookie2));
        replay(this.userSource, this.ldap, this.codec, this.request, this.response, this.session, this.user,
                this.token, this.data);
        assertEquals("redirect:/test.html",
                this.persistenLoginController.renewPersistentLogin(null, null, this.url, this.request, this.response));
        assertCookieDeleted(cookie1.getValue());
        assertCookieDeleted(cookie2.getValue());
        verify(this.userSource, this.ldap, this.codec, this.request, this.response, this.session, this.user,
                this.token, this.data);
    }

    private void assertCookieDeleted(final Cookie cookie) {
        assertEquals("/", cookie.getPath());
        assertEquals(0, cookie.getMaxAge());
        assertNull(cookie.getValue());
    }
}
