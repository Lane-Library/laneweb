package edu.stanford.irt.laneweb.user;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.verify;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.cocoon.environment.Session;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ldap.core.LdapTemplate;

import edu.stanford.irt.laneweb.Cryptor;
import edu.stanford.irt.laneweb.LanewebConstants;

public class UserDaoTest {

    private Cookie[] cookies = new Cookie[1];

    private Cryptor cryptor;

    private String ezproxyKey = "boguskey";

    private String ip;

    private HttpServletRequest request;

    private Session session;

    private SubjectSource subjectSource;

    private String sunetid;

    private UserDao userDao;

    @Before
    public void setUp() {
        this.request = createMock(HttpServletRequest.class);
        this.session = createMock(Session.class);
        this.subjectSource = createMock(SubjectSource.class);
        this.ip = "171.65.28.124";
        this.sunetid = "ceyates";
        expect(this.request.getSession(true)).andReturn(this.session);
        expect(this.session.getAttribute(LanewebConstants.USER)).andReturn(null);
        this.session.setAttribute(isA(String.class), isA(User.class));
        replay(this.session);
        expect(this.request.getHeader(LanewebConstants.X_FORWARDED_FOR)).andReturn(null);
        expect(this.request.getParameter(LanewebConstants.PROXY_LINKS)).andReturn("true");
        expect(this.request.getParameter(User.EMRID)).andReturn(null);
        this.userDao = new UserDao();
        this.userDao.setSubjectSource(this.subjectSource);
        this.userDao.setLdapTemplate(createMock(LdapTemplate.class));
        this.userDao.setEzproxyKey(this.ezproxyKey);
        this.cryptor = new Cryptor();
        this.cryptor.setKey("testtesttesttesttestt");
        this.userDao.setCryptor(this.cryptor);
    }

    @Test
    public void testCookieUserInfo() {
        resetCookieTest();
        StringBuffer cookieValue = new StringBuffer(128);
        cookieValue.append("ceyates");
        cookieValue.append(LanewebConstants.COOKIE_VALUE_SEPARATOR);
        cookieValue.append(String.valueOf(new Date().getTime()));
        cookieValue.append(LanewebConstants.COOKIE_VALUE_SEPARATOR);
        cookieValue.append("firefox test".hashCode());
        Cookie sunetIdCookie = new Cookie(LanewebConstants.LANE_COOKIE_NAME, this.cryptor.encrypt(cookieValue.toString()));
        this.cookies[0] = sunetIdCookie;
        expect(this.request.getHeader("User-Agent")).andReturn("firefox test");
        replay(this.request);
        User user = this.userDao.createOrUpdateUser(this.request);
        assertEquals(IPGroup.SOM_OTHER, user.getIPGroup());
        assertTrue(user.getProxyLinks());
        assertEquals(this.sunetid, user.getSunetId());
        assertNotNull(user.getTicket());
        assertEquals(46, user.getTicket().toString().length());
        verify(this.session);
        verify(this.request);
    }

    @Test
    public final void testGetSunetIdCookieModified() {
        resetCookieTest();
        StringBuffer cookieValue = new StringBuffer(128);
        cookieValue.append("ceyates");
        cookieValue.append(LanewebConstants.COOKIE_VALUE_SEPARATOR);
        cookieValue.append(String.valueOf(new Date().getTime()));
        cookieValue.append(LanewebConstants.COOKIE_VALUE_SEPARATOR);
        cookieValue.append("firefox test".hashCode());
        String value = this.cryptor.encrypt(cookieValue.toString());
        Cookie sunetIdCookie = new Cookie(LanewebConstants.LANE_COOKIE_NAME, value.substring(1));
        this.cookies[0] = sunetIdCookie;
        expect(this.request.getHeader(LanewebConstants.X_FORWARDED_FOR)).andReturn(null);
        replay(this.request);
        String sunetid = null;
        User user = this.userDao.createOrUpdateUser(this.request);
        sunetid = user.getSunetId();
        assertEquals(sunetid, null);
        verify(this.session);
        verify(this.request);
    }

    @Test
    public final void testGetSunetIdCookieNull() {
        this.cookies = new Cookie[0];
        resetCookieTest();
        replay(this.request);
        User user = this.userDao.createOrUpdateUser(this.request);
        this.sunetid = user.getSunetId();
        assertEquals(this.sunetid, null);
        verify(this.session);
        verify(this.request);
    }

    @Test
    public final void testGetSunetIdLimitTime() {
        resetCookieTest();
        GregorianCalendar gc = new GregorianCalendar();
        gc.add(Calendar.DAY_OF_YEAR, -13);
        String createdDate = String.valueOf(gc.getTime().getTime());
        StringBuffer cookieValue = new StringBuffer();
        cookieValue.append("ceyates");
        cookieValue.append(LanewebConstants.COOKIE_VALUE_SEPARATOR);
        cookieValue.append(createdDate);
        cookieValue.append(LanewebConstants.COOKIE_VALUE_SEPARATOR);
        cookieValue.append("firefox test".hashCode());
        String value = this.cryptor.encrypt(cookieValue.toString());
        Cookie sunetIdCookie = new Cookie(LanewebConstants.LANE_COOKIE_NAME, value);
        this.cookies[0] = sunetIdCookie;
        expect(this.request.getHeader("User-Agent")).andReturn("firefox test");
        replay(this.request);
        User user = this.userDao.createOrUpdateUser(this.request);
        String sunetid = user.getSunetId();
        assertEquals(sunetid, this.sunetid);
        verify(this.session);
        verify(this.request);
    }

    @Test
    public final void testGetSunetIdSunetIdNull() {
        resetCookieTest();
        StringBuffer cookieValue = new StringBuffer(128);
        cookieValue.append("");
        cookieValue.append(LanewebConstants.COOKIE_VALUE_SEPARATOR);
        cookieValue.append(String.valueOf(new Date().getTime()));
        cookieValue.append(LanewebConstants.COOKIE_VALUE_SEPARATOR);
        cookieValue.append("firefox test".hashCode());
        String value = this.cryptor.encrypt(cookieValue.toString());
        Cookie sunetIdCookie = new Cookie(LanewebConstants.LANE_COOKIE_NAME, value);
        this.cookies[0] = sunetIdCookie;
        expect(this.request.getHeader("User-Agent")).andReturn("firefox test");
        replay(this.request);
        String sunetid = null;
        User user = this.userDao.createOrUpdateUser(this.request);
        sunetid = user.getSunetId();
        assertEquals(null, sunetid);
        verify(this.session);
        verify(this.request);
    }

    @Test
    public final void testGetSunetIdTimePassed() {
        resetCookieTest();
        GregorianCalendar gc = new GregorianCalendar();
        gc.add(Calendar.DAY_OF_YEAR, -15);
        String createdDate = String.valueOf(gc.getTime().getTime());
        StringBuffer cookieValue = new StringBuffer();
        cookieValue.append("ceyates");
        cookieValue.append(LanewebConstants.COOKIE_VALUE_SEPARATOR);
        cookieValue.append(createdDate);
        cookieValue.append(LanewebConstants.COOKIE_VALUE_SEPARATOR);
        cookieValue.append("firefox test".hashCode());
        String value = this.cryptor.encrypt(cookieValue.toString());
        Cookie sunetIdCookie = new Cookie(LanewebConstants.LANE_COOKIE_NAME, value);
        this.cookies[0] = sunetIdCookie;
        expect(this.request.getHeader("User-Agent")).andReturn("firefox test");
        replay(this.request);
        User user = this.userDao.createOrUpdateUser(this.request);
        String sunetid = user.getSunetId();
        assertEquals(sunetid, null);
        verify(this.session);
        verify(this.request);
    }

    @Test
    public final void testGetSunetIdUserAgent() {
        resetCookieTest();
        StringBuffer cookieValue = new StringBuffer(128);
        cookieValue.append("ceyates");
        cookieValue.append(LanewebConstants.COOKIE_VALUE_SEPARATOR);
        cookieValue.append(String.valueOf(new Date().getTime()));
        cookieValue.append(LanewebConstants.COOKIE_VALUE_SEPARATOR);
        cookieValue.append("firefox test the user agent".hashCode());
        String value = this.cryptor.encrypt(cookieValue.toString());
        Cookie sunetIdCookie = new Cookie(LanewebConstants.LANE_COOKIE_NAME, value);
        this.cookies[0] = sunetIdCookie;
        expect(this.request.getHeader("User-Agent")).andReturn("firefox test");
        replay(this.request);
        String sunetid = null;
        User user = this.userDao.createOrUpdateUser(this.request);
        sunetid = user.getSunetId();
        assertEquals(sunetid, null);
        verify(this.session);
        verify(this.request);
    }

    @Test
    public void testGetUserInfo() {
        expect(this.request.getRemoteUser()).andReturn(this.sunetid);
        expect(this.request.getRemoteAddr()).andReturn(this.ip);
        replay(this.request);
        User user = this.userDao.createOrUpdateUser(this.request);
        assertEquals(IPGroup.SOM_OTHER, user.getIPGroup());
        assertTrue(user.getProxyLinks());
        assertEquals(this.sunetid, user.getSunetId());
        assertNotNull(user.getTicket());
        assertEquals(46, user.getTicket().toString().length());
        verify(this.session);
        verify(this.request);
    }

    @Test
    public void testipGroup() {
        String ip = this.ip.concat("FAIL_TEST");
        expect(this.request.getRemoteAddr()).andReturn(ip);
        expect(this.request.getRemoteUser()).andReturn(this.sunetid);
        replay(this.request);
        User user = this.userDao.createOrUpdateUser(this.request);
        assertEquals(IPGroup.ERR, user.getIPGroup());
        verify(this.session);
        verify(this.request);
    }

    @Test
    public void testNotTimeGetSunetId() {
        resetCookieTest();
        StringBuffer cookieValue = new StringBuffer(128);
        cookieValue.append("ceyates");
        cookieValue.append(LanewebConstants.COOKIE_VALUE_SEPARATOR);
        cookieValue.append("");
        cookieValue.append(LanewebConstants.COOKIE_VALUE_SEPARATOR);
        cookieValue.append("firefox test".hashCode());
        String value = this.cryptor.encrypt(cookieValue.toString());
        Cookie sunetIdCookie = new Cookie(LanewebConstants.LANE_COOKIE_NAME, value);
        this.cookies[0] = sunetIdCookie;
        this.userDao.setCryptor(this.cryptor);
        expect(this.request.getHeader(LanewebConstants.X_FORWARDED_FOR)).andReturn(null);
        replay(this.request);
        User user = this.userDao.createOrUpdateUser(this.request);
        String sunetid = user.getSunetId();
        assertEquals(sunetid, null);
        verify(this.session);
        verify(this.request);
    }

    private void resetCookieTest() {
        expect(this.request.getHeader("x-webauth-user")).andReturn(null);
        expect(this.request.getCookies()).andReturn(this.cookies);
        expect(this.request.getRemoteAddr()).andReturn(this.ip).atLeastOnce();
        expect(this.request.getRemoteUser()).andReturn(null);
    }
}
