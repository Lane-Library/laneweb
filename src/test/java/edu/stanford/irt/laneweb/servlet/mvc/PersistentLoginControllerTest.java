package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.easymock.Capture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.laneweb.codec.PersistentLoginToken;
import edu.stanford.irt.laneweb.codec.UserCookieCodec;
import edu.stanford.irt.laneweb.servlet.CookieName;
import edu.stanford.irt.laneweb.servlet.binding.UserDataBinder;
import edu.stanford.irt.laneweb.user.User;

public class PersistentLoginControllerTest {

    private static final String LANE_PROXY_URL = "https://login.laneproxy.stanford.edu/login";

    private UserCookieCodec codec;

    private PersistentLoginController persistenLoginController;

    private HttpServletRequest request;

    private HttpServletResponse response;

    private HttpSession session;

    private PersistentLoginToken token;

    private String url = LANE_PROXY_URL + "/test.html";

    private User user;

    private UserDataBinder userSource;

    @BeforeEach
    public void setUp() throws Exception {
        this.userSource = mock(UserDataBinder.class);
        this.codec = mock(UserCookieCodec.class);
        this.persistenLoginController = new PersistentLoginController(this.userSource, this.codec);
        this.request = mock(HttpServletRequest.class);
        this.response = mock(HttpServletResponse.class);
        this.session = mock(HttpSession.class);
        this.user = mock(User.class);
        this.token = mock(PersistentLoginToken.class);
    }

    @Test
    public void testEnablePersistentLoginNotNullUrl() {
        Capture<Cookie> cookie1 = newCapture();
        Capture<Cookie> cookie2 = newCapture();
        this.response.addCookie(capture(cookie1));
        this.response.addCookie(capture(cookie2));
        this.response.setHeader("Set-Cookie", "cookie");
        expect(this.request.getHeader("User-Agent")).andReturn("firefox");
        expect(this.codec.createLoginToken(this.user, "firefox".hashCode())).andReturn(this.token);
        expect(this.token.getEncryptedValue()).andReturn("encryptedValue");
        expect(this.response.getHeaders("Set-Cookie")).andReturn(Collections.singleton("cookie"));
        replay(this.userSource, this.codec, this.request, this.response, this.session, this.user, this.token);
        String redirect = this.persistenLoginController.enablePersistentLogin(null, this.user, this.url, this.request,
                this.response);
        assertEquals("redirect:https://login.laneproxy.stanford.edu/login/test.html", redirect);
        assertEquals(2419200, cookie1.getValue().getMaxAge());
        assertEquals("encryptedValue", cookie1.getValue().getValue());
        assertTrue(System.currentTimeMillis() + Duration.ofDays(28).minus(Duration.ofMillis(100)).toMillis() < Long
                .valueOf(cookie2.getValue().getValue()));
        assertEquals(CookieName.EXPIRATION.toString(), cookie2.getValue().getName());
        verify(this.userSource, this.codec, this.request, this.response, this.session, this.user, this.token);
    }

    @Test
    public void testEnablePersistentLoginNullUser() {
        Capture<Cookie> cookie1 = newCapture();
        Capture<Cookie> cookie2 = newCapture();
        this.response.addCookie(capture(cookie1));
        this.response.addCookie(capture(cookie2));
        replay(this.userSource, this.codec, this.request, this.response, this.session, this.user, this.token);
        this.persistenLoginController.enablePersistentLogin(null, null, this.url, this.request, this.response);
        assertCookieDeleted(cookie1.getValue());
        assertCookieDeleted(cookie2.getValue());
        verify(this.userSource, this.codec, this.request, this.response, this.session, this.user, this.token);
    }

    @Test
    public void testEnablePersistentLoginNullUrl() {
        Capture<Cookie> cookie1 = newCapture();
        Capture<Cookie> cookie2 = newCapture();
        this.response.addCookie(capture(cookie1));
        this.response.addCookie(capture(cookie2));
        this.response.setHeader("Set-Cookie", "cookie");
        expect(this.response.getHeaders("Set-Cookie")).andReturn(Collections.singleton("cookie"));
        expect(this.request.getHeader("User-Agent")).andReturn("firefox");
        expect(this.codec.createLoginToken(this.user, "firefox".hashCode())).andReturn(this.token);
        expect(this.token.getEncryptedValue()).andReturn("encryptedValue");
        replay(this.userSource, this.codec, this.request, this.response, this.session, this.user, this.token);
        String redirect = this.persistenLoginController.enablePersistentLogin(null, this.user, null, this.request,
                this.response);
        assertEquals("redirect:/index.html", redirect);
        assertEquals(2419200, cookie1.getValue().getMaxAge());
        assertEquals("encryptedValue", cookie1.getValue().getValue());
        assertTrue(System.currentTimeMillis() + Duration.ofDays(28).minus(Duration.ofMillis(100)).toMillis() < Long
                .valueOf(cookie2.getValue().getValue()));
        assertEquals(CookieName.EXPIRATION.toString(), cookie2.getValue().getName());
        verify(this.userSource, this.codec, this.request, this.response, this.session, this.user, this.token);
    }

    @Test
    public void testSimpleSameSite() {
        Capture<Cookie> cookie1 = newCapture();
        Capture<Cookie> cookie2 = newCapture();
        this.response.addCookie(capture(cookie1));
        this.response.addCookie(capture(cookie2));
        expect(this.response.getHeaders("Set-Cookie"))
                .andReturn(Collections.singleton(CookieName.EXPIRATION.toString() + "cookie"));
        this.response.setHeader("Set-Cookie", CookieName.EXPIRATION.toString() + "cookie; SameSite=Lax");
        expect(this.request.getHeader("User-Agent")).andReturn("firefox");
        expect(this.codec.createLoginToken(this.user, "firefox".hashCode())).andReturn(this.token);
        expect(this.token.getEncryptedValue()).andReturn("encryptedValue");
        replay(this.userSource, this.codec, this.request, this.response, this.session, this.user, this.token);
        String redirect = this.persistenLoginController.enablePersistentLogin(null, this.user, null, this.request,
                this.response);
        assertEquals("redirect:/index.html", redirect);
        assertEquals(2419200, cookie1.getValue().getMaxAge());
        assertEquals("encryptedValue", cookie1.getValue().getValue());
        assertTrue(System.currentTimeMillis() + Duration.ofDays(28).minus(Duration.ofMillis(100)).toMillis() < Long
                .valueOf(cookie2.getValue().getValue()));
        assertEquals(CookieName.EXPIRATION.toString(), cookie2.getValue().getName());
        verify(this.userSource, this.codec, this.request, this.response, this.session, this.user, this.token);
    }

    @Test
    public void testSameSiteMutipleCookies() {
        Capture<Cookie> cookie1 = newCapture();
        Capture<Cookie> cookie2 = newCapture();
        this.response.addCookie(capture(cookie1));
        this.response.addCookie(capture(cookie2));
        Collection<String> cookieFromHttpResponse = new ArrayList<String>();
        cookieFromHttpResponse.add(CookieName.EXPIRATION.toString() + "_cookie");
        cookieFromHttpResponse.add(CookieName.USER.toString() + "_cookie");
        cookieFromHttpResponse.add("SESSION_cookie");
        expect(this.response.getHeaders("Set-Cookie")).andReturn(cookieFromHttpResponse);
        this.response.setHeader("Set-Cookie", CookieName.EXPIRATION.toString() + "_cookie; SameSite=Lax");
        this.response.addHeader("Set-Cookie", CookieName.USER.toString() + "_cookie; SameSite=Lax");
        this.response.addHeader("Set-Cookie", "SESSION_cookie");
        expect(this.request.getHeader("User-Agent")).andReturn("firefox");
        expect(this.codec.createLoginToken(this.user, "firefox".hashCode())).andReturn(this.token);
        expect(this.token.getEncryptedValue()).andReturn("encryptedValue");
        replay(this.userSource, this.codec, this.request, this.response, this.session, this.user, this.token);
        String redirect = this.persistenLoginController.enablePersistentLogin(null, this.user, null, this.request,
                this.response);
        assertEquals("redirect:/index.html", redirect);
        assertEquals(2419200, cookie1.getValue().getMaxAge());
        assertEquals("encryptedValue", cookie1.getValue().getValue());
        assertTrue(System.currentTimeMillis() + Duration.ofDays(28).minus(Duration.ofMillis(100)).toMillis() < Long
                .valueOf(cookie2.getValue().getValue()));
        assertEquals(CookieName.EXPIRATION.toString(), cookie2.getValue().getName());
        verify(this.userSource, this.codec, this.request, this.response, this.session, this.user, this.token);
    }

    @Test
    public void testPersistentMyaccountsLoginNull() {
        Capture<Cookie> cookie1 = newCapture();
        Capture<Cookie> cookie2 = newCapture();
        this.response.addCookie(capture(cookie1));
        this.response.addCookie(capture(cookie2));
        replay(this.userSource, this.codec, this.request, this.response, this.session, this.user, this.token);
        String redirect = this.persistenLoginController.myaccount(null, null, "true", this.request, this.response);
        assertEquals("redirect:/myaccounts.html", redirect);
        assertCookieDeleted(cookie1.getValue());
        assertCookieDeleted(cookie2.getValue());
        verify(this.userSource, this.codec, this.request, this.response, this.session, this.user, this.token);
    }

    private void assertCookieDeleted(final Cookie cookie) {
        assertEquals("/", cookie.getPath());
        assertEquals(0, cookie.getMaxAge());
        assertNull(cookie.getValue());
    }
}
