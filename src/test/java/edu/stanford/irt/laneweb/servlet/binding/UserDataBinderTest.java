package edu.stanford.irt.laneweb.servlet.binding;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.geq;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.codec.PersistentLoginToken;
import edu.stanford.irt.laneweb.codec.UserCookieCodec;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.user.LDAPData;
import edu.stanford.irt.laneweb.user.LDAPDataAccess;
import edu.stanford.irt.laneweb.user.User;

public class UserDataBinderTest {

    private UserDataBinder binder;

    private UserCookieCodec codec;

    private Cookie cookie;

    private LDAPDataAccess ldap;

    private LDAPData ldapData;

    private Logger log;

    private HttpServletRequest request;

    private HttpSession session;

    private PersistentLoginToken token;

    private User user;

    @Before
    public void setUp() {
        this.codec = createMock(UserCookieCodec.class);
        this.log = createMock(Logger.class);
        this.ldap = createMock(LDAPDataAccess.class);
        this.binder = new UserDataBinder(this.codec, this.log, "key", this.ldap);
        this.request = createMock(HttpServletRequest.class);
        this.session = createMock(HttpSession.class);
        this.user = createMock(User.class);
        this.cookie = createMock(Cookie.class);
        this.token = createMock(PersistentLoginToken.class);
        this.ldapData = createMock(LDAPData.class);
    }

    @Test
    public void testBind() {
        Map<String, Object> model = new HashMap<String, Object>();
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.USER)).andReturn(null);
        expect(this.request.getRemoteUser()).andReturn("id@domain");
        expect(this.request.getAttribute("displayName")).andReturn("name");
        expect(this.request.getAttribute("mail")).andReturn("mail");
        this.session.setAttribute(eq(Model.USER), isA(User.class));
        replay(this.request, this.session, this.user, this.ldap);
        this.binder.bind(model, this.request);
        assertNotNull(model.get(Model.USER));
        assertEquals("id@domain", model.get(Model.USER_ID));
        assertEquals("mail", model.get(Model.EMAIL));
        assertEquals("name", model.get(Model.NAME));
        assertEquals("911531548a5ea68cf13f5e0506367956@domain", model.get(Model.AUTH));
        assertNull(model.get(Model.IS_ACTIVE_SUNETID));
        verify(this.request, this.session, this.user, this.ldap);
    }

    @Test
    public void testBindMultiValueName() {
        Map<String, Object> model = new HashMap<String, Object>();
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.USER)).andReturn(null);
        expect(this.request.getRemoteUser()).andReturn("id@domain");
        expect(this.request.getAttribute("displayName")).andReturn("first name;another name");
        expect(this.request.getAttribute("mail")).andReturn("mail");
        this.session.setAttribute(eq(Model.USER), isA(User.class));
        replay(this.request, this.session, this.user, this.ldap);
        this.binder.bind(model, this.request);
        assertNotNull(model.get(Model.USER));
        assertEquals("id@domain", model.get(Model.USER_ID));
        assertEquals("mail", model.get(Model.EMAIL));
        assertEquals("first name", model.get(Model.NAME));
        assertEquals("911531548a5ea68cf13f5e0506367956@domain", model.get(Model.AUTH));
        assertNull(model.get(Model.IS_ACTIVE_SUNETID));
        verify(this.request, this.session, this.user, this.ldap);
    }

    @Test
    public void testBindNoEmailOrName() {
        Map<String, Object> model = new HashMap<String, Object>();
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.USER)).andReturn(null);
        expect(this.request.getRemoteUser()).andReturn("id@name.org");
        expect(this.request.getAttribute("displayName")).andReturn(null);
        expect(this.request.getAttribute("mail")).andReturn(null);
        this.session.setAttribute(eq(Model.USER), isA(User.class));
        replay(this.request, this.session, this.user, this.ldap);
        this.binder.bind(model, this.request);
        assertNotNull(model.get(Model.USER));
        assertEquals("id@name.org", model.get(Model.USER_ID));
        assertNull(model.get(Model.EMAIL));
        assertNull(model.get(Model.NAME));
        assertEquals("911531548a5ea68cf13f5e0506367956@name.org", model.get(Model.AUTH));
        assertNull(model.get(Model.IS_ACTIVE_SUNETID));
        verify(this.request, this.session, this.user, this.ldap);
    }

    @Test
    public void testBindNoUser() {
        Map<String, Object> model = new HashMap<String, Object>();
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.USER)).andReturn(null);
        expect(this.request.getRemoteUser()).andReturn(null);
        expect(this.request.getCookies()).andReturn(null);
        expect(this.request.getHeader("User-Agent")).andReturn("useragent");
        replay(this.request, this.session, this.user, this.ldap);
        this.binder.bind(model, this.request);
        assertNull(model.get(Model.USER));
        verify(this.request, this.session, this.user, this.ldap);
    }

    @Test
    public void testBindNoUserAgent() {
        Map<String, Object> model = new HashMap<String, Object>();
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.USER)).andReturn(null);
        expect(this.request.getRemoteUser()).andReturn(null);
        expect(this.request.getCookies()).andReturn(new Cookie[] {});
        expect(this.request.getHeader("User-Agent")).andReturn(null);
        replay(this.request, this.session, this.user, this.ldap);
        this.binder.bind(model, this.request);
        assertNull(model.get(Model.USER));
        verify(this.request, this.session, this.user, this.ldap);
    }

    @Test
    public void testBindUserCookieCodecException() {
        Map<String, Object> model = new HashMap<String, Object>();
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.USER)).andReturn(null);
        expect(this.request.getRemoteUser()).andReturn(null);
        expect(this.request.getCookies()).andReturn(new Cookie[] { this.cookie, this.cookie, this.cookie });
        expect(this.request.getHeader("User-Agent")).andReturn("useragent");
        expect(this.cookie.getName()).andReturn("name");
        expect(this.cookie.getName()).andReturn(UserCookieCodec.LANE_COOKIE_NAME);
        expect(this.cookie.getValue()).andReturn("value");
        expect(this.codec.restoreLoginToken("value", "key")).andReturn(this.token);
        expect(this.token.isValidFor(geq(System.currentTimeMillis()), eq("useragent".hashCode()))).andReturn(false);
        replay(this.request, this.session, this.user, this.cookie, this.codec, this.token);
        this.binder.bind(model, this.request);
        assertNull(model.get(Model.USER));
        verify(this.request, this.session, this.user, this.cookie, this.codec, this.token);
    }

    @Test
    public void testBindUserCookieNotStanford() {
        Map<String, Object> model = new HashMap<String, Object>();
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.USER)).andReturn(null);
        expect(this.request.getRemoteUser()).andReturn(null);
        expect(this.request.getCookies()).andReturn(new Cookie[] { this.cookie, this.cookie, this.cookie });
        expect(this.request.getHeader("User-Agent")).andReturn("useragent");
        expect(this.cookie.getName()).andReturn("name");
        expect(this.cookie.getName()).andReturn(UserCookieCodec.LANE_COOKIE_NAME);
        expect(this.cookie.getValue()).andReturn("value");
        expect(this.codec.restoreLoginToken("value", "key")).andReturn(this.token);
        expect(this.token.isValidFor(geq(System.currentTimeMillis()), eq("useragent".hashCode()))).andReturn(true);
        expect(this.token.getUser()).andReturn(this.user);
        expect(this.user.isStanfordUser()).andReturn(false).times(2);
        this.session.setAttribute(Model.USER, this.user);
        expect(this.user.getId()).andReturn("id");
        expect(this.user.getEmail()).andReturn("mail");
        expect(this.user.getName()).andReturn("name");
        expect(this.user.getHashedId()).andReturn("hash");
        replay(this.request, this.session, this.user, this.cookie, this.codec, this.token);
        this.binder.bind(model, this.request);
        assertEquals(this.user, model.get(Model.USER));
        assertSame(this.user, model.get(Model.USER));
        assertEquals("id", model.get(Model.USER_ID));
        assertEquals("mail", model.get(Model.EMAIL));
        assertEquals("name", model.get(Model.NAME));
        assertEquals("hash", model.get(Model.AUTH));
        assertNull(model.get(Model.IS_ACTIVE_SUNETID));
        verify(this.request, this.session, this.user, this.cookie, this.codec, this.token);
    }

    @Test
    public void testBindUserCookieStanford() {
        Map<String, Object> model = new HashMap<String, Object>();
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.USER)).andReturn(null);
        expect(this.request.getRemoteUser()).andReturn(null);
        expect(this.request.getCookies()).andReturn(new Cookie[] { this.cookie, this.cookie, this.cookie });
        expect(this.request.getHeader("User-Agent")).andReturn("useragent");
        expect(this.cookie.getName()).andReturn("name");
        expect(this.cookie.getName()).andReturn(UserCookieCodec.LANE_COOKIE_NAME);
        expect(this.cookie.getValue()).andReturn("value");
        expect(this.codec.restoreLoginToken("value", "key")).andReturn(this.token);
        expect(this.token.isValidFor(geq(System.currentTimeMillis()), eq("useragent".hashCode()))).andReturn(true);
        expect(this.token.getUser()).andReturn(this.user);
        expect(this.user.isStanfordUser()).andReturn(true);
        expect(this.user.getId()).andReturn("id@stanford.edu");
        expect(this.ldap.getLdapDataForSunetid("id")).andReturn(this.ldapData);
        expect(this.ldapData.isActive()).andReturn(true);
        expect(this.user.getName()).andReturn("name");
        expect(this.user.getEmail()).andReturn("mail");
        Capture<User> capture = new Capture<User>();
        this.session.setAttribute(eq(Model.USER), capture(capture));
        replay(this.request, this.session, this.user, this.cookie, this.codec, this.token, this.ldap, this.ldapData);
        this.binder.bind(model, this.request);
        User captured = capture.getValue();
        assertEquals(captured, model.get(Model.USER));
        assertSame(captured, model.get(Model.USER));
        assertEquals("id@stanford.edu", model.get(Model.USER_ID));
        assertEquals("mail", model.get(Model.EMAIL));
        assertEquals("name", model.get(Model.NAME));
        assertEquals(captured.getHashedId(), model.get(Model.AUTH));
        assertEquals(Boolean.TRUE, model.get(Model.IS_ACTIVE_SUNETID));
        verify(this.request, this.session, this.user, this.cookie, this.codec, this.token, this.ldap, this.ldapData);
    }

    @Test
    public void testBindUserCookieStanfordNotActive() {
        Map<String, Object> model = new HashMap<String, Object>();
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.USER)).andReturn(null);
        expect(this.request.getRemoteUser()).andReturn(null);
        expect(this.request.getCookies()).andReturn(new Cookie[] { this.cookie, this.cookie, this.cookie });
        expect(this.request.getHeader("User-Agent")).andReturn("useragent");
        expect(this.cookie.getName()).andReturn("name");
        expect(this.cookie.getName()).andReturn(UserCookieCodec.LANE_COOKIE_NAME);
        expect(this.cookie.getValue()).andReturn("value");
        expect(this.codec.restoreLoginToken("value", "key")).andReturn(this.token);
        expect(this.token.isValidFor(geq(System.currentTimeMillis()), eq("useragent".hashCode()))).andReturn(true);
        expect(this.token.getUser()).andReturn(this.user);
        expect(this.user.isStanfordUser()).andReturn(true);
        expect(this.user.getId()).andReturn("id@stanford.edu");
        expect(this.ldap.getLdapDataForSunetid("id")).andReturn(this.ldapData);
        expect(this.ldapData.isActive()).andReturn(false);
        expect(this.user.getName()).andReturn("name");
        expect(this.user.getEmail()).andReturn("mail");
        Capture<User> capture = new Capture<User>();
        this.session.setAttribute(eq(Model.USER), capture(capture));
        replay(this.request, this.session, this.user, this.cookie, this.codec, this.token, this.ldap, this.ldapData);
        this.binder.bind(model, this.request);
        User captured = capture.getValue();
        assertEquals(captured, model.get(Model.USER));
        assertSame(captured, model.get(Model.USER));
        assertEquals("id@stanford.edu", model.get(Model.USER_ID));
        assertEquals("mail", model.get(Model.EMAIL));
        assertEquals("name", model.get(Model.NAME));
        assertEquals(Boolean.FALSE, model.get(Model.IS_ACTIVE_SUNETID));
        assertEquals(captured.getHashedId(), model.get(Model.AUTH));
        verify(this.request, this.session, this.user, this.cookie, this.codec, this.token, this.ldap, this.ldapData);
    }

    @Test
    public void testBindUserCookieTokenNotValid() {
        Map<String, Object> model = new HashMap<String, Object>();
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.USER)).andReturn(null);
        expect(this.request.getRemoteUser()).andReturn(null);
        expect(this.request.getCookies()).andReturn(new Cookie[] { this.cookie, this.cookie, this.cookie });
        expect(this.request.getHeader("User-Agent")).andReturn("useragent");
        expect(this.cookie.getName()).andReturn("name");
        expect(this.cookie.getName()).andReturn(UserCookieCodec.LANE_COOKIE_NAME);
        expect(this.cookie.getValue()).andReturn("value").times(2);
        expect(this.codec.restoreLoginToken("value", "key")).andThrow(new LanewebException("oopsie"));
        replay(this.request, this.session, this.user, this.cookie, this.codec, this.token);
        this.binder.bind(model, this.request);
        assertNull(model.get(Model.USER));
        verify(this.request, this.session, this.user, this.cookie, this.codec, this.token);
    }

    @Test
    public void testBindUserInSession() {
        Map<String, Object> model = new HashMap<String, Object>();
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.USER)).andReturn(this.user);
        expect(this.user.getId()).andReturn("id");
        expect(this.user.getEmail()).andReturn("mail");
        expect(this.user.getName()).andReturn("name");
        expect(this.user.getHashedId()).andReturn("hash");
        expect(this.user.isStanfordUser()).andReturn(false);
        replay(this.request, this.session, this.user, this.ldap);
        this.binder.bind(model, this.request);
        assertSame(this.user, model.get(Model.USER));
        assertEquals("id", model.get(Model.USER_ID));
        assertEquals("mail", model.get(Model.EMAIL));
        assertEquals("name", model.get(Model.NAME));
        assertEquals("hash", model.get(Model.AUTH));
        assertNull(model.get(Model.IS_ACTIVE_SUNETID));
        verify(this.request, this.session, this.user, this.ldap);
    }
}
