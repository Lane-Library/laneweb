package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.anyInt;
import static org.easymock.EasyMock.anyLong;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.servlet.PersistentLoginToken;
import edu.stanford.irt.laneweb.servlet.SunetIdCookieCodec;

public class PersistentLoginCookieValidatorControllerTest {

    private SunetIdCookieCodec codec;

    private PersistentLoginCookieValidatorController controller;

    private Cookie cookie;

    private HttpServletRequest request;

    private HttpServletResponse response;

    private PersistentLoginToken token;

    private PrintWriter writer;

    @Before
    public void setUp() throws Exception {
        this.codec = createMock(SunetIdCookieCodec.class);
        this.controller = new PersistentLoginCookieValidatorController(this.codec);
        this.request = createMock(HttpServletRequest.class);
        this.response = createMock(HttpServletResponse.class);
        this.cookie = createMock(Cookie.class);
        this.token = createMock(PersistentLoginToken.class);
        this.writer = createMock(PrintWriter.class);
    }

    @Test
    public void testValidateCookie() throws IOException {
        expect(this.request.getHeader("User-Agent")).andReturn("user agent");
        expect(this.request.getCookies()).andReturn(new Cookie[] { this.cookie });
        expect(this.cookie.getName()).andReturn(SunetIdCookieCodec.LANE_COOKIE_NAME);
        expect(this.cookie.getValue()).andReturn("value");
        expect(this.codec.restoreLoginToken("value")).andReturn(this.token);
        expect(this.token.isValidFor(anyLong(), anyInt())).andReturn(true).times(2);
        this.response.setHeader("Content-Type", "application/x-javascript");
        expect(this.response.getWriter()).andReturn(this.writer);
        this.writer.write("callback(true,true);");
        replay(this.codec, this.request, this.response, this.cookie, this.token, this.writer);
        this.controller.validateCookie(this.request, this.response, "callback");
        verify(this.codec, this.request, this.response, this.cookie, this.token, this.writer);
    }

    @Test
    public void testValidateCookieGracePeriod() throws IOException {
        expect(this.request.getHeader("User-Agent")).andReturn("user agent");
        expect(this.request.getCookies()).andReturn(new Cookie[] { this.cookie });
        expect(this.cookie.getName()).andReturn(SunetIdCookieCodec.LANE_COOKIE_NAME);
        expect(this.cookie.getValue()).andReturn("value");
        expect(this.codec.restoreLoginToken("value")).andReturn(this.token);
        expect(this.token.isValidFor(anyLong(), anyInt())).andReturn(true);
        expect(this.token.isValidFor(anyLong(), anyInt())).andReturn(false);
        this.response.setHeader("Content-Type", "application/x-javascript");
        expect(this.response.getWriter()).andReturn(this.writer);
        this.writer.write("callback(true,false);");
        replay(this.codec, this.request, this.response, this.cookie, this.token, this.writer);
        this.controller.validateCookie(this.request, this.response, "callback");
        verify(this.codec, this.request, this.response, this.cookie, this.token, this.writer);
    }

    @Test
    public void testValidateCookieNoCallback() throws IOException {
        expect(this.request.getHeader("User-Agent")).andReturn("user agent");
        expect(this.request.getCookies()).andReturn(new Cookie[] { this.cookie });
        expect(this.cookie.getName()).andReturn(SunetIdCookieCodec.LANE_COOKIE_NAME);
        expect(this.cookie.getValue()).andReturn("value");
        expect(this.codec.restoreLoginToken("value")).andReturn(this.token);
        expect(this.token.isValidFor(anyLong(), anyInt())).andReturn(true).times(2);
        this.response.setHeader("Content-Type", "application/x-javascript");
        expect(this.response.getWriter()).andReturn(this.writer);
        this.writer.write("true,true");
        replay(this.codec, this.request, this.response, this.cookie, this.token, this.writer);
        this.controller.validateCookie(this.request, this.response, null);
        verify(this.codec, this.request, this.response, this.cookie, this.token, this.writer);
    }

    @Test
    public void testValidateCookieNoCookies() throws IOException {
        expect(this.request.getCookies()).andReturn(null);
        this.response.setHeader("Content-Type", "application/x-javascript");
        expect(this.response.getWriter()).andReturn(this.writer);
        this.writer.write("callback(false,false);");
        replay(this.codec, this.request, this.response, this.cookie, this.token, this.writer);
        this.controller.validateCookie(this.request, this.response, "callback");
        verify(this.codec, this.request, this.response, this.cookie, this.token, this.writer);
    }

    @Test
    public void testValidateCookieNotRightCookie() throws IOException {
        expect(this.request.getHeader("User-Agent")).andReturn("user agent");
        expect(this.request.getCookies()).andReturn(new Cookie[] { this.cookie });
        expect(this.cookie.getName()).andReturn("name");
        this.response.setHeader("Content-Type", "application/x-javascript");
        expect(this.response.getWriter()).andReturn(this.writer);
        this.writer.write("callback(false,false);");
        replay(this.codec, this.request, this.response, this.cookie, this.token, this.writer);
        this.controller.validateCookie(this.request, this.response, "callback");
        verify(this.codec, this.request, this.response, this.cookie, this.token, this.writer);
    }

    @Test
    public void testValidateCookieNotValid() throws IOException {
        expect(this.request.getHeader("User-Agent")).andReturn("user agent");
        expect(this.request.getCookies()).andReturn(new Cookie[] { this.cookie });
        expect(this.cookie.getName()).andReturn(SunetIdCookieCodec.LANE_COOKIE_NAME);
        expect(this.cookie.getValue()).andReturn("value");
        expect(this.codec.restoreLoginToken("value")).andReturn(this.token);
        expect(this.token.isValidFor(anyLong(), anyInt())).andReturn(false);
        this.response.setHeader("Content-Type", "application/x-javascript");
        expect(this.response.getWriter()).andReturn(this.writer);
        this.writer.write("callback(false,false);");
        replay(this.codec, this.request, this.response, this.cookie, this.token, this.writer);
        this.controller.validateCookie(this.request, this.response, "callback");
        verify(this.codec, this.request, this.response, this.cookie, this.token, this.writer);
    }
}
