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

import edu.stanford.irt.laneweb.codec.PersistentLoginToken;
import edu.stanford.irt.laneweb.codec.UserCookieCodec;
import edu.stanford.irt.laneweb.servlet.binding.UserDataBinder;
import edu.stanford.irt.laneweb.user.User;

public class PersistentLoginControllerTest {

    private UserCookieCodec codec;

    private PersistentLoginController persistenLoginController;

    private HttpServletRequest request;

    private HttpServletResponse response;

    private HttpSession session;

    private UserDataBinder userSource;

    private String url = "/test.html";
    
    private User user;

    private PersistentLoginToken token;

    @Before
    public void setUp() throws Exception {
        this.userSource = createMock(UserDataBinder.class);
        this.codec = createMock(UserCookieCodec.class);
        this.persistenLoginController = new PersistentLoginController(this.userSource, this.codec);
        this.request = createMock(HttpServletRequest.class);
        this.response = createMock(HttpServletResponse.class);
        this.session = createMock(HttpSession.class);
        this.user = createMock(User.class);
        this.token = createMock(PersistentLoginToken.class);
        this.response.addCookie(isA(Cookie.class));
        this.response.addCookie(isA(Cookie.class));
    }

    @Test
    public void testCreateCookieNotNullUrl() throws UnsupportedEncodingException {
        expect(this.request.getHeader("User-Agent")).andReturn("firefox");
        this.response.addCookie(isA(Cookie.class));
        expect(this.codec.createLoginToken(this.user, "firefox".hashCode())).andReturn(this.token);
        expect(this.token.getEncryptedValue()).andReturn("encryptedValue");
        replay(this.userSource, this.request, this.response, this.session, this.user, this.codec);
        RedirectView view = (RedirectView) this.persistenLoginController.createCookie(this.user, this.url, this.request, this.response);
        assertEquals(view.getUrl(), "/test.html");
        verify(this.userSource, this.request, this.session, this.response, this.user, this.codec);
    }

    @Test
    public void testCreateCookieNullUrl() throws UnsupportedEncodingException {
        expect(this.request.getHeader("User-Agent")).andReturn("firefox");
        this.response.addCookie(isA(Cookie.class));
        this.response.setCharacterEncoding(isA(String.class));
        expect(this.codec.createLoginToken(this.user, "firefox".hashCode())).andReturn(this.token);
        expect(this.token.getEncryptedValue()).andReturn("encryptedValue");
        replay(this.userSource, this.request, this.response, this.session, this.user, this.codec, this.token);
        RedirectView view = (RedirectView) this.persistenLoginController.createCookie(this.user, null, this.request, this.response);
        assertEquals(view.getUrl(), "/myaccounts.html");
        verify(this.userSource, this.request, this.session, this.response, this.user, this.codec, this.token);
    }

    @Test
    public void testCreateCookieUserIdNull() throws UnsupportedEncodingException {
        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie(PersistentLoginController.PERSISTENT_LOGIN_PREFERENCE, null);
        this.response.addCookie(isA(Cookie.class));
        this.response.addCookie(isA(Cookie.class));
        expect(this.request.getCookies()).andReturn(cookies);
        replay(this.userSource, this.request, this.session, this.response, this.user, this.codec);
        this.persistenLoginController.createCookie(null, this.url, this.request, this.response);
        verify(this.userSource, this.request, this.session, this.response, this.user, this.codec);
    }

    @Test
    public void testRemoveCookieUrlNotNull() throws UnsupportedEncodingException {
        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie(PersistentLoginController.PERSISTENT_LOGIN_PREFERENCE, "234890");
        expect(this.request.getCookies()).andReturn(cookies);
        this.response.addCookie(isA(Cookie.class));
        replay(this.userSource, this.request, this.session, this.response, this.user, this.codec);
        RedirectView view = (RedirectView) this.persistenLoginController.removeCookieAndView(this.user, this.url, this.request, this.response);
        assertEquals(view.getUrl(), "/test.html");
        verify(this.userSource, this.request, this.session, this.response, this.user, this.codec);
    }

    @Test
    public void testRemoveCookieUrlNull() throws UnsupportedEncodingException {
        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie(PersistentLoginController.PERSISTENT_LOGIN_PREFERENCE, "234033");
        expect(this.request.getCookies()).andReturn(cookies);
        this.response.addCookie(isA(Cookie.class));
        this.response.setCharacterEncoding(isA(String.class));
        replay(this.userSource, this.request, this.session, this.response, this.user, this.codec);
        RedirectView view = (RedirectView) this.persistenLoginController.removeCookieAndView(this.user, null, this.request, this.response);
        assertEquals(view.getUrl(), "/myaccounts.html");
        verify(this.userSource, this.request, this.session, this.response, this.user, this.codec);
    }

    @Test
    public void testRemoveWithDeniedCookie() throws UnsupportedEncodingException {
        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie(PersistentLoginController.PERSISTENT_LOGIN_PREFERENCE, "denied");
        expect(this.request.getCookies()).andReturn(cookies);
        replay(this.userSource, this.request, this.session, this.response, this.user, this.codec);
        RedirectView view = (RedirectView) this.persistenLoginController.removeCookieAndView(this.user, this.url, this.request, this.response);
        assertEquals(view.getUrl(), "/test.html");
        verify(this.userSource, this.request, this.session, this.response, this.user, this.codec);
    }
}
