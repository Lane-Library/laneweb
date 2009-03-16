package edu.stanford.irt.laneweb.user;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.verify;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import junit.framework.TestCase;

import org.apache.cocoon.environment.Session;
import org.springframework.ldap.core.LdapTemplate;

import edu.stanford.irt.laneweb.Cryptor;
import edu.stanford.irt.laneweb.LanewebConstants;

public class UserDaoTest extends TestCase {

    private Cookie[] cookies = new Cookie[3];

    private Cryptor cryptor;

    private String ezproxyKey = "boguskey";

    private String ip;

    private HashMap<String, Cookie> map;

    private HttpServletRequest request;

    private Session session;

    private SubjectSource subjectSource;

    private String sunetid;

    private UserDao userDao;

    public void testCookieUserInfo() throws Exception {
        new HashMap<String, Cookie>();
        expect(this.request.getHeader("User-Agent")).andReturn("firefox test");
        expect(this.request.getRemoteAddr()).andReturn(this.ip);
        expect(this.request.getRemoteUser()).andReturn(null);
        expect(this.request.getHeader("x-webauth-user")).andReturn(null);
        // expect(this.request.getCookieMap()).andReturn(map);
        expect(this.request.getCookies()).andReturn(this.cookies);
        replay(this.request);
        Cryptor cryptor = new Cryptor();
        cryptor.setKey("testtesttesttesttestt");
        String cryptedUserName = cryptor.encrypt(this.sunetid);
        Cookie sunetIdCookie = new Cookie(LanewebConstants.USER_COOKIE_NAME, cryptedUserName);
        String createdDate = String.valueOf(new Date().getTime());
        String cryptedDate = cryptor.encrypt(createdDate);
        Cookie dateCookie = new Cookie(LanewebConstants.DATE_COOKIE_NAME, cryptedDate);
        String encryptedSecurity = cryptor.encrypt(createdDate.concat(this.sunetid).concat("firefox test"));
        Cookie securityCookie = new Cookie(LanewebConstants.SECURITY_COOKIE_NAME, encryptedSecurity);
        this.cookies[0] = sunetIdCookie;
        this.cookies[1] = dateCookie;
        this.cookies[2] = securityCookie;
        this.userDao.setCryptor(cryptor);
        User user = this.userDao.createOrUpdateUser(this.request);
        assertEquals(IPGroup.SOM, user.getIPGroup());
        assertTrue(user.getProxyLinks());
        assertEquals(this.sunetid, user.getSunetId());
        assertNotNull(user.getTicket());
        assertEquals(46, user.getTicket().toString().length());
        verify(this.session);
        verify(this.request);
    }

    public final void testGetSunetIdDateCookieModified() throws Exception {
        resetCookieTest();
        Cookie cookie = new Cookie(LanewebConstants.DATE_COOKIE_NAME, "456448989");
        this.map.put(LanewebConstants.DATE_COOKIE_NAME, cookie);
        this.map.values().toArray(this.cookies);
        String sunetid = null;
        User user = this.userDao.createOrUpdateUser(this.request);
        sunetid = user.getSunetId();
        assertEquals(sunetid, null);
    }

    public final void testGetSunetIdDateCookieNull() throws Exception {
        resetCookieTest();
        this.map.remove(LanewebConstants.DATE_COOKIE_NAME);
        this.map.values().toArray(this.cookies);
        this.userDao.setCryptor(this.cryptor);
        User user = this.userDao.createOrUpdateUser(this.request);
        String sunetid = user.getSunetId();
        assertEquals(sunetid, null);
    }

    public final void testGetSunetIdSecurityCookieModified() throws Exception {
        resetCookieTest();
        Cookie cookie = new Cookie(LanewebConstants.SECURITY_COOKIE_NAME, "48784+91+894agag7a7+89910d+h789b10addfs+79adfbv");
        this.map.put(LanewebConstants.SECURITY_COOKIE_NAME, cookie);
        this.map.values().toArray(this.cookies);
        String sunetid = null;
        User user = this.userDao.createOrUpdateUser(this.request);
        sunetid = user.getSunetId();
        assertEquals(sunetid, null);
    }

    public final void testGetSunetIdSecurityCookieNull() throws Exception {
        resetCookieTest();
        this.map.remove(LanewebConstants.SECURITY_COOKIE_NAME);
        this.map.values().toArray(this.cookies);
        User user = this.userDao.createOrUpdateUser(this.request);
        String sunetid = user.getSunetId();
        assertEquals(sunetid, null);
    }

    public final void testGetSunetIdTime() throws Exception {
        resetCookieTest();
        GregorianCalendar gc = new GregorianCalendar();
        gc.add(Calendar.DAY_OF_YEAR, -13);
        String createdDate = String.valueOf(gc.getTime().getTime());
        String cryptedDate = this.cryptor.encrypt(createdDate);
        Cookie dateCookie = new Cookie(LanewebConstants.DATE_COOKIE_NAME, cryptedDate);
        String encryptedSecurity = this.cryptor.encrypt(createdDate.concat(this.sunetid).concat("firefox test"));
        Cookie securityCookie = new Cookie(LanewebConstants.SECURITY_COOKIE_NAME, encryptedSecurity);
        this.map.put(LanewebConstants.DATE_COOKIE_NAME, dateCookie);
        this.map.put(LanewebConstants.SECURITY_COOKIE_NAME, securityCookie);
        this.map.values().toArray(this.cookies);
        User user = this.userDao.createOrUpdateUser(this.request);
        String sunetid = user.getSunetId();
        assertEquals(sunetid, this.sunetid);
    }

    public final void testGetSunetIdTimePassed() throws Exception {
        resetCookieTest();
        GregorianCalendar gc = new GregorianCalendar();
        gc.add(Calendar.DAY_OF_YEAR, -15);
        String createdDate = String.valueOf(gc.getTime().getTime());
        String cryptedDate = this.cryptor.encrypt(createdDate);
        Cookie dateCookie = new Cookie(LanewebConstants.DATE_COOKIE_NAME, cryptedDate);
        String encryptedSecurity = this.cryptor.encrypt(createdDate.concat(this.sunetid).concat("firefox test"));
        Cookie securityCookie = new Cookie(LanewebConstants.SECURITY_COOKIE_NAME, encryptedSecurity);
        this.map.put(LanewebConstants.DATE_COOKIE_NAME, dateCookie);
        this.map.put(LanewebConstants.SECURITY_COOKIE_NAME, securityCookie);
        this.map.values().toArray(this.cookies);
        User user = this.userDao.createOrUpdateUser(this.request);
        String sunetid = user.getSunetId();
        assertEquals(sunetid, null);
    }

    public final void testGetSunetIdUserCookieModified() throws Exception {
        resetCookieTest();
        Cookie cookie = new Cookie(LanewebConstants.USER_COOKIE_NAME, "alainv");
        this.map.put(LanewebConstants.USER_COOKIE_NAME, cookie);
        this.map.values().toArray(this.cookies);
        String sunetid = null;
        User user = this.userDao.createOrUpdateUser(this.request);
        sunetid = user.getSunetId();
        assertEquals(sunetid, null);
    }

    public final void testGetSunetIdUserCookieNull() throws Exception {
        resetCookieTest();
        this.map.remove(LanewebConstants.USER_COOKIE_NAME);
        this.map.values().toArray(this.cookies);
        User user = this.userDao.createOrUpdateUser(this.request);
        String sunetid = user.getSunetId();
        assertEquals(sunetid, null);
    }

    public void testGetUserInfo() {
        expect(this.request.getRemoteUser()).andReturn(this.sunetid);
        expect(this.request.getRemoteAddr()).andReturn(this.ip);
        replay(this.request);
        User user = this.userDao.createOrUpdateUser(this.request);
        assertEquals(IPGroup.SOM, user.getIPGroup());
        assertTrue(user.getProxyLinks());
        assertEquals(this.sunetid, user.getSunetId());
        assertNotNull(user.getTicket());
        assertEquals(46, user.getTicket().toString().length());
        verify(this.session);
        verify(this.request);
    }

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

    private void resetCookieTest() throws Exception {
        this.map = new HashMap<String, Cookie>();
        expect(this.request.getHeader("User-Agent")).andReturn("firefox test");
        expect(this.request.getRemoteAddr()).andReturn(this.ip);
        expect(this.request.getRemoteUser()).andReturn(null);
        expect(this.request.getHeader("x-webauth-user")).andReturn(null);
        expect(this.request.getCookies()).andReturn(this.cookies);
        replay(this.request);
        this.cryptor = new Cryptor();
        this.cryptor.setKey("testtesttesttesttestt");
        String createdDate = String.valueOf(new Date().getTime());
        String cryptedDate = this.cryptor.encrypt(createdDate);
        Cookie dateCookie = new Cookie(LanewebConstants.DATE_COOKIE_NAME, cryptedDate);
        String encryptedSecurity = this.cryptor.encrypt(createdDate.concat(this.sunetid).concat("firefox test"));
        Cookie securityCookie = new Cookie(LanewebConstants.SECURITY_COOKIE_NAME, encryptedSecurity);
        String cryptedUserName = this.cryptor.encrypt(this.sunetid);
        Cookie sunetIdCookie = new Cookie(LanewebConstants.USER_COOKIE_NAME, cryptedUserName);
        this.map.put(LanewebConstants.USER_COOKIE_NAME, sunetIdCookie);
        this.map.put(LanewebConstants.DATE_COOKIE_NAME, dateCookie);
        this.map.put(LanewebConstants.SECURITY_COOKIE_NAME, securityCookie);
        this.userDao.setCryptor(this.cryptor);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.request = createMock(HttpServletRequest.class);
        this.session = createMock(Session.class);
        this.subjectSource = createMock(SubjectSource.class);
        this.ip = "171.65.28.124";
        this.sunetid = "ceyates";
        expect(this.session.getAttribute(LanewebConstants.USER)).andReturn(null);
        this.session.setAttribute(isA(String.class), isA(User.class));
        expect(this.request.getSession(true)).andReturn(this.session);
        expect(this.request.getHeader(LanewebConstants.X_FORWARDED_FOR)).andReturn(null);
        expect(this.request.getParameter(LanewebConstants.PROXY_LINKS)).andReturn("true");
        replay(this.session);
        this.userDao = new UserDao();
        this.userDao.setSubjectSource(this.subjectSource);
        this.userDao.setLdapTemplate(createMock(LdapTemplate.class));
        this.userDao.setEzproxyKey(this.ezproxyKey);
    }
}
