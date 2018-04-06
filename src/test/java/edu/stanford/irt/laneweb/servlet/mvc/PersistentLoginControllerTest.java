package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.Duration;

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
import edu.stanford.irt.laneweb.servlet.binding.ActiveSunetidDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.UserDataBinder;
import edu.stanford.irt.laneweb.user.User;

public class PersistentLoginControllerTest {

    private UserCookieCodec codec;

    private ActiveSunetidDataBinder ldap;

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
        this.userSource = mock(UserDataBinder.class);
        this.ldap = mock(ActiveSunetidDataBinder.class);
        this.codec = mock(UserCookieCodec.class);
        this.persistenLoginController = new PersistentLoginController(this.userSource, this.ldap, this.codec);
        this.request = mock(HttpServletRequest.class);
        this.response = mock(HttpServletResponse.class);
        this.session = mock(HttpSession.class);
        this.user = mock(User.class);
        this.token = mock(PersistentLoginToken.class);
    }

    @Test
    public void testDisablePersistentLoginUrlNotNull() {
        Capture<Cookie> cookie1 = newCapture();
        Capture<Cookie> cookie2 = newCapture();
        this.response.addCookie(capture(cookie1));
        this.response.addCookie(capture(cookie2));
        replay(this.userSource, this.ldap, this.codec, this.request, this.response, this.session, this.user,
                this.token);
        String redirect = this.persistenLoginController.disablePersistentLogin(null, this.user, this.url,
                this.response);
        assertEquals("redirect:/test.html", redirect);
        assertCookieDeleted(cookie1.getValue());
        assertCookieDeleted(cookie2.getValue());
        verify(this.userSource, this.ldap, this.codec, this.request, this.response, this.session, this.user,
                this.token);
    }

    @Test
    public void testDisablePersistentLoginUrlNull() {
        Capture<Cookie> cookie1 = newCapture();
        Capture<Cookie> cookie2 = newCapture();
        this.response.addCookie(capture(cookie1));
        this.response.addCookie(capture(cookie2));
        replay(this.userSource, this.ldap, this.codec, this.request, this.response, this.session, this.user,
                this.token);
        String redirect = this.persistenLoginController.disablePersistentLogin(null, this.user, null, this.response);
        assertEquals("redirect:/myaccounts.html", redirect);
        assertCookieDeleted(cookie1.getValue());
        assertCookieDeleted(cookie2.getValue());
        verify(this.userSource, this.ldap, this.codec, this.request, this.response, this.session, this.user,
                this.token);
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
                this.token);
        String redirect = this.persistenLoginController.enablePersistentLogin(null, this.user, this.url, this.request,
                this.response);
        assertEquals("redirect:/test.html", redirect);
        assertEquals(1209600, cookie1.getValue().getMaxAge());
        assertEquals("encryptedValue", cookie1.getValue().getValue());
        assertTrue(System.currentTimeMillis() + Duration.ofDays(14).minus(Duration.ofMillis(100)).toMillis() < Long
                .valueOf(cookie2.getValue().getValue()));
        assertEquals(CookieName.EXPIRATION.toString(), cookie2.getValue().getName());
        verify(this.userSource, this.ldap, this.codec, this.request, this.response, this.session, this.user,
                this.token);
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
                this.token);
        String redirect = this.persistenLoginController.enablePersistentLogin(null, this.user, null, this.request,
                this.response);
        assertEquals("redirect:/myaccounts.html", redirect);
        assertEquals(1209600, cookie1.getValue().getMaxAge());
        assertEquals("encryptedValue", cookie1.getValue().getValue());
        assertTrue(System.currentTimeMillis() + Duration.ofDays(14).minus(Duration.ofMillis(100)).toMillis() < Long
                .valueOf(cookie2.getValue().getValue()));
        assertEquals(CookieName.EXPIRATION.toString(), cookie2.getValue().getName());
        verify(this.userSource, this.ldap, this.codec, this.request, this.response, this.session, this.user,
                this.token);
    }

    @Test
    public void testEnablePersistentLoginNullUser() {
        Capture<Cookie> cookie1 = newCapture();
        Capture<Cookie> cookie2 = newCapture();
        this.response.addCookie(capture(cookie1));
        this.response.addCookie(capture(cookie2));
        replay(this.userSource, this.ldap, this.codec, this.request, this.response, this.session, this.user,
                this.token);
        this.persistenLoginController.enablePersistentLogin(null, null, this.url, this.request, this.response);
        assertCookieDeleted(cookie1.getValue());
        assertCookieDeleted(cookie2.getValue());
        verify(this.userSource, this.ldap, this.codec, this.request, this.response, this.session, this.user,
                this.token);
    }

    @Test
    public void testRenewPersistentLogin() {
        Capture<Cookie> cookie1 = newCapture();
        Capture<Cookie> cookie2 = newCapture();
        this.response.addCookie(capture(cookie1));
        this.response.addCookie(capture(cookie2));
        expect(this.request.getHeader("User-Agent")).andReturn("firefox");
        expect(this.codec.createLoginToken(this.user, "firefox".hashCode())).andReturn(this.token);
        expect(this.token.getEncryptedValue()).andReturn("encryptedValue");
        replay(this.userSource, this.ldap, this.codec, this.request, this.response, this.session, this.user,
                this.token);
        assertEquals("redirect:/test.html", this.persistenLoginController.renewPersistentLogin(null, Boolean.TRUE,
                this.user, this.url, this.request, this.response));
        assertEquals(1209600, cookie1.getValue().getMaxAge());
        assertEquals("encryptedValue", cookie1.getValue().getValue());
        assertTrue(System.currentTimeMillis() + Duration.ofDays(14).minus(Duration.ofMillis(100)).toMillis() < Long
                .valueOf(cookie2.getValue().getValue()));
        assertEquals(CookieName.EXPIRATION.toString(), cookie2.getValue().getName());
        verify(this.userSource, this.ldap, this.codec, this.request, this.response, this.session, this.user,
                this.token);
    }

    @Test
    public void testRenewPersistentLoginNotActive() {
        Capture<Cookie> cookie1 = newCapture();
        Capture<Cookie> cookie2 = newCapture();
        this.response.addCookie(capture(cookie1));
        this.response.addCookie(capture(cookie2));
        replay(this.userSource, this.ldap, this.codec, this.request, this.response, this.session, this.user,
                this.token);
        assertEquals("redirect:/test.html", this.persistenLoginController.renewPersistentLogin(null, Boolean.FALSE,
                this.user, this.url, this.request, this.response));
        assertCookieDeleted(cookie1.getValue());
        assertCookieDeleted(cookie2.getValue());
        verify(this.userSource, this.ldap, this.codec, this.request, this.response, this.session, this.user,
                this.token);
    }

    @Test
    public void testRenewPersistentLoginNotStanford() {
        Capture<Cookie> cookie1 = newCapture();
        Capture<Cookie> cookie2 = newCapture();
        this.response.addCookie(capture(cookie1));
        this.response.addCookie(capture(cookie2));
        replay(this.userSource, this.ldap, this.codec, this.request, this.response, this.session, this.user,
                this.token);
        assertEquals("redirect:/test.html", this.persistenLoginController.renewPersistentLogin(null, Boolean.FALSE,
                this.user, this.url, this.request, this.response));
        assertCookieDeleted(cookie1.getValue());
        assertCookieDeleted(cookie2.getValue());
        verify(this.userSource, this.ldap, this.codec, this.request, this.response, this.session, this.user,
                this.token);
    }

    @Test
    public void testRenewPersistentLoginNullUser() {
        Capture<Cookie> cookie1 = newCapture();
        Capture<Cookie> cookie2 = newCapture();
        this.response.addCookie(capture(cookie1));
        this.response.addCookie(capture(cookie2));
        replay(this.userSource, this.ldap, this.codec, this.request, this.response, this.session, this.user,
                this.token);
        assertEquals("redirect:/test.html", this.persistenLoginController.renewPersistentLogin(null, Boolean.FALSE,
                null, this.url, this.request, this.response));
        assertCookieDeleted(cookie1.getValue());
        assertCookieDeleted(cookie2.getValue());
        verify(this.userSource, this.ldap, this.codec, this.request, this.response, this.session, this.user,
                this.token);
    }

    private void assertCookieDeleted(final Cookie cookie) {
        assertEquals("/", cookie.getPath());
        assertEquals(0, cookie.getMaxAge());
        assertNull(cookie.getValue());
    }
}
