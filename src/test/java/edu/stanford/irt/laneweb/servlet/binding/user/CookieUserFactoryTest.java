package edu.stanford.irt.laneweb.servlet.binding.user;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.geq;
import static org.easymock.EasyMock.gt;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.codec.PersistentLoginToken;
import edu.stanford.irt.laneweb.codec.UserCookieCodec;
import edu.stanford.irt.laneweb.user.LDAPData;
import edu.stanford.irt.laneweb.user.LDAPDataAccess;
import edu.stanford.irt.laneweb.user.User;
import edu.stanford.irt.laneweb.user.User.Status;

public class CookieUserFactoryTest {

    private UserCookieCodec codec;

    private Cookie cookie;

    private CookieUserFactory factory;

    private LDAPDataAccess ldap;

    private LDAPData ldapData;

    private Logger log;

    private HttpServletRequest request;

    private PersistentLoginToken token;

    private User user;

    private String userIdHashKey;

    @Before
    public void setUp() {
        this.userIdHashKey = "key";
        this.log = createMock(Logger.class);
        this.codec = createMock(UserCookieCodec.class);
        this.ldap = createMock(LDAPDataAccess.class);
        this.factory = new CookieUserFactory(this.codec, this.ldap, this.log, this.userIdHashKey);
        this.request = createMock(HttpServletRequest.class);
        this.cookie = createMock(Cookie.class);
        this.token = createMock(PersistentLoginToken.class);
        this.user = createMock(User.class);
        this.ldapData = createMock(LDAPData.class);
    }

    @Test
    public void testCreateUserCookieCodecException() {
        expect(this.request.getCookies()).andReturn(new Cookie[] { this.cookie, this.cookie, this.cookie });
        expect(this.request.getHeader("User-Agent")).andReturn("useragent");
        expect(this.cookie.getName()).andReturn("name");
        expect(this.cookie.getName()).andReturn(UserCookieCodec.LANE_COOKIE_NAME);
        expect(this.cookie.getValue()).andReturn("value").times(2);
        LanewebException ex = new LanewebException("invalid encryptedValue");
        expect(this.codec.restoreLoginToken("value", "key")).andThrow(ex);
        this.log.error("failed to decode userid from: value", ex);
        replay(this.log, this.codec, this.ldap, this.request, this.cookie, this.token, this.user, this.ldapData);
        assertNull(this.factory.createUser(this.request));
        verify(this.log, this.codec, this.ldap, this.request, this.cookie, this.token, this.user, this.ldapData);
    }

    @Test
    public void testCreateUserCookieNotStanford() {
        expect(this.request.getCookies()).andReturn(new Cookie[] { this.cookie, this.cookie, this.cookie });
        expect(this.request.getHeader("User-Agent")).andReturn("useragent");
        expect(this.cookie.getName()).andReturn("name");
        expect(this.cookie.getName()).andReturn(UserCookieCodec.LANE_COOKIE_NAME);
        expect(this.cookie.getValue()).andReturn("value");
        expect(this.codec.restoreLoginToken("value", "key")).andReturn(this.token);
        expect(this.token.isValidFor(geq(System.currentTimeMillis()), eq("useragent".hashCode()))).andReturn(true);
        expect(this.token.getUser()).andReturn(this.user);
        expect(this.user.isStanfordUser()).andReturn(false);
        replay(this.log, this.codec, this.ldap, this.request, this.cookie, this.token, this.user, this.ldapData);
        User created = this.factory.createUser(this.request);
        assertEquals(this.user, created);
        assertSame(this.user, created);
        verify(this.log, this.codec, this.ldap, this.request, this.cookie, this.token, this.user, this.ldapData);
    }

    @Test
    public void testCreateUserCookieStanford() {
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
        replay(this.log, this.codec, this.ldap, this.request, this.cookie, this.token, this.user, this.ldapData);
        User created = this.factory.createUser(this.request);
        assertEquals("id@stanford.edu", created.getId());
        assertEquals("name", created.getName());
        assertEquals("mail", created.getEmail());
        assertEquals("911531548a5ea68cf13f5e0506367956@stanford.edu", created.getHashedId());
        assertEquals(Status.ACTIVE, created.getStatus());
        verify(this.log, this.codec, this.ldap, this.request, this.cookie, this.token, this.user, this.ldapData);
    }

    @Test
    public void testCreateUserCookieStanfordNotActive() {
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
        replay(this.log, this.codec, this.ldap, this.request, this.cookie, this.token, this.user, this.ldapData);
        User created = this.factory.createUser(this.request);
        assertEquals("id@stanford.edu", created.getId());
        assertEquals("name", created.getName());
        assertEquals("mail", created.getEmail());
        assertEquals("911531548a5ea68cf13f5e0506367956@stanford.edu", created.getHashedId());
        assertEquals(Status.INACTIVE, created.getStatus());
        verify(this.log, this.codec, this.ldap, this.request, this.cookie, this.token, this.user, this.ldapData);
    }

    @Test
    public void testCreateUserCookieTokenNotValid() {
        expect(this.request.getCookies()).andReturn(new Cookie[] { this.cookie, this.cookie, this.cookie });
        expect(this.request.getHeader("User-Agent")).andReturn("useragent");
        expect(this.cookie.getName()).andReturn("name");
        expect(this.cookie.getName()).andReturn(UserCookieCodec.LANE_COOKIE_NAME);
        expect(this.cookie.getValue()).andReturn("value");
        expect(this.codec.restoreLoginToken("value", "key")).andReturn(this.token);
        expect(this.token.isValidFor(gt(0L), eq("useragent".hashCode()))).andReturn(false);
        replay(this.log, this.codec, this.ldap, this.request, this.cookie, this.token, this.user, this.ldapData);
        assertNull(this.factory.createUser(this.request));
        verify(this.log, this.codec, this.ldap, this.request, this.cookie, this.token, this.user, this.ldapData);
    }
}
