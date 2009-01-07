package edu.stanford.irt.laneweb;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.verify;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;

import edu.stanford.irt.directory.LDAPPerson;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.Cookie;

import junit.framework.TestCase;

import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Response;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.http.HttpResponse;
import org.apache.commons.codec.DecoderException;
import org.junit.Test;

public class UserInfoHelperTest extends TestCase {

    private Request request;

    private String ip;

    private String sunetid;

    private String ezproxyKey = "boguskey";

    private LdapClient ldapClient;

    private Session session;

    private UserInfoHelper userInfoHelper;

    private Cryptor cryptor;

    private HashMap<String, Cookie> map;

    @Override
    protected void setUp() throws Exception {
	super.setUp();
	this.ldapClient = createMock(LdapClient.class);

	this.request = createMock(Request.class);
	this.session = createMock(Session.class);
	this.ldapClient = createMock(LdapClient.class);
	this.ip = "171.65.28.124";
	this.sunetid = "ceyates";
	LDAPPerson ldapPerson = new LDAPPerson();
	String[] sunetIds = new String[1];
	sunetIds[0] = this.sunetid;
	ldapPerson.setSunetId(sunetIds);

	expect(this.ldapClient.getLdapPerson(this.sunetid)).andReturn(ldapPerson);
	expect(this.request.getAttribute(LanewebConstants.USER_INFO)).andReturn(null);
	expect(this.session.getAttribute(LanewebConstants.USER_INFO)).andReturn(null);

	this.session.setAttribute(isA(String.class), isA(UserInfo.class));
	this.request.setAttribute(isA(String.class), isA(UserInfo.class));

	expect(this.request.getSession(true)).andReturn(this.session);
	expect(this.request.getHeader(LanewebConstants.X_FORWARDED_FOR)).andReturn(null);
	expect(this.request.getParameter(LanewebConstants.PROXY_LINKS)).andReturn("true");
	expect(this.request.getParameter(LanewebConstants.PROXY_LINKS)).andReturn("true");
	replay(this.ldapClient);
	replay(this.session);

	this.userInfoHelper = new UserInfoHelper();
	this.userInfoHelper.setLdapClient(this.ldapClient);
	this.userInfoHelper.setEzproxyKey(this.ezproxyKey);

    }

    public void testGetUserInfo() {
	expect(this.request.getRemoteUser()).andReturn(this.sunetid);
	expect(this.request.getRemoteAddr()).andReturn(this.ip);
	replay(this.request);
	UserInfo userInfo = this.userInfoHelper.getUserInfo(this.request);
	assertEquals(Affiliation.SOM, userInfo.getAffiliation());
	assertTrue(userInfo.getProxyLinks());
	assertEquals(this.sunetid, userInfo.getSunetId());
	assertNotNull(userInfo.getTicket());
	assertEquals(46, userInfo.getTicket().toString().length());
	assertEquals(this.sunetid, userInfo.getPerson().getSunetId()[0]);
	verify(this.session);
	verify(this.request);
    }

    public void testAffiliation() {
	String ip = this.ip.concat("FAIL_TEST");
	expect(this.request.getRemoteAddr()).andReturn(ip);
	expect(this.request.getRemoteUser()).andReturn(this.sunetid);
	replay(this.request);
	UserInfo userInfo = this.userInfoHelper.getUserInfo(this.request);
	assertEquals(Affiliation.ERR, userInfo.getAffiliation());
	verify(this.session);
	verify(this.request);
    }

    public void testCookieUserInfo() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
	    IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, IOException, DecoderException {
	HashMap<String, Cookie> map = new HashMap<String, Cookie>();
	expect(this.request.getHeader("User-Agent")).andReturn("firefox test");
	expect(this.request.getRemoteAddr()).andReturn(this.ip);
	expect(this.request.getRemoteUser()).andReturn(null);
	expect(this.request.getCookieMap()).andReturn(map);
	replay(this.request);

	Cryptor cryptor = new Cryptor();
	cryptor.setKey("testtesttesttesttestt");

	String cryptedUserName = cryptor.encrypt(sunetid);
	Cookie sunetIdCookie = new Cookie(LanewebConstants.USER_COOKIE_NAME, cryptedUserName);
	String createdDate = String.valueOf(new Date().getTime());
	String cryptedDate = cryptor.encrypt(createdDate);
	Cookie dateCookie = new Cookie(LanewebConstants.DATE_COOKIE_NAME, cryptedDate);
	String encryptedSecurity = cryptor.encrypt(createdDate.concat(sunetid).concat("firefox test"));
	Cookie securityCookie = new Cookie(LanewebConstants.SECURITY_COOKIE_NAME, encryptedSecurity);
	map.put(LanewebConstants.USER_COOKIE_NAME, sunetIdCookie);
	map.put(LanewebConstants.DATE_COOKIE_NAME, dateCookie);
	map.put(LanewebConstants.SECURITY_COOKIE_NAME, securityCookie);
	this.userInfoHelper.setCryptor(cryptor);
	UserInfo userInfo = this.userInfoHelper.getUserInfo(this.request);

	assertEquals(Affiliation.SOM, userInfo.getAffiliation());
	assertTrue(userInfo.getProxyLinks());
	assertEquals(this.sunetid, userInfo.getSunetId());
	assertNotNull(userInfo.getTicket());
	assertEquals(46, userInfo.getTicket().toString().length());
	assertEquals(this.sunetid, userInfo.getPerson().getSunetId()[0]);
	verify(this.session);
	verify(this.request);
    }

    private void resetCookieTest() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
	    BadPaddingException, InvalidKeySpecException, IOException, DecoderException {
	this.map = new HashMap<String, Cookie>();
	expect(this.request.getHeader("User-Agent")).andReturn("firefox test");
	expect(this.request.getRemoteAddr()).andReturn(this.ip);
	expect(this.request.getRemoteUser()).andReturn(null);
	expect(this.request.getCookieMap()).andReturn(map);
	replay(this.request);

	this.cryptor = new Cryptor();
	cryptor.setKey("testtesttesttesttestt");

	String createdDate = String.valueOf(new Date().getTime());
	String cryptedDate = cryptor.encrypt(createdDate);
	Cookie dateCookie = new Cookie(LanewebConstants.DATE_COOKIE_NAME, cryptedDate);
	String encryptedSecurity = cryptor.encrypt(createdDate.concat(sunetid).concat("firefox test"));
	Cookie securityCookie = new Cookie(LanewebConstants.SECURITY_COOKIE_NAME, encryptedSecurity);
	String cryptedUserName = cryptor.encrypt(sunetid);
	Cookie sunetIdCookie = new Cookie(LanewebConstants.USER_COOKIE_NAME, cryptedUserName);
	map.put(LanewebConstants.USER_COOKIE_NAME, sunetIdCookie);
	map.put(LanewebConstants.DATE_COOKIE_NAME, dateCookie);
	map.put(LanewebConstants.SECURITY_COOKIE_NAME, securityCookie);
	this.userInfoHelper.setCryptor(cryptor);

    }

    public final void testGetSunetIdUserCookieNull() throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException,
	    NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, DecoderException, IOException {
	resetCookieTest();
	map.put(LanewebConstants.USER_COOKIE_NAME, null);
	UserInfo userInfo = this.userInfoHelper.getUserInfo(this.request);
	String sunetid = userInfo.getSunetId();
	assertEquals(sunetid, null);
    }

    public final void testGetSunetIdDateCookieNull() throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException,
	    NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, DecoderException, IOException {
	resetCookieTest();
	map.put(LanewebConstants.DATE_COOKIE_NAME, null);
	this.userInfoHelper.setCryptor(cryptor);
	UserInfo userInfo = this.userInfoHelper.getUserInfo(this.request);
	String sunetid = userInfo.getSunetId();
	assertEquals(sunetid, null);
    }

    public final void testGetSunetIdSecurityCookieNull() throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException,
	    NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, DecoderException, IOException {
	resetCookieTest();
	map.put(LanewebConstants.SECURITY_COOKIE_NAME, null);
	UserInfo userInfo = this.userInfoHelper.getUserInfo(this.request);
	String sunetid = userInfo.getSunetId();
	assertEquals(sunetid, null);
    }

    public final void testGetSunetIdUserCookieModified() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
	    IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, IOException, DecoderException {
	resetCookieTest();
	Cookie cookie = new Cookie(LanewebConstants.USER_COOKIE_NAME, "alainv");
	map.put(LanewebConstants.USER_COOKIE_NAME, cookie);
	String sunetid = null;
	UserInfo userInfo = this.userInfoHelper.getUserInfo(this.request);
	sunetid = userInfo.getSunetId();
	assertEquals(sunetid, null);
    }

    public final void testGetSunetIdDateCookieModified() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
	    IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, IOException, DecoderException {
	resetCookieTest();
	Cookie cookie = new Cookie(LanewebConstants.DATE_COOKIE_NAME, "456448989");
	map.put(LanewebConstants.DATE_COOKIE_NAME, cookie);
	String sunetid = null;
	UserInfo userInfo = this.userInfoHelper.getUserInfo(this.request);
	sunetid = userInfo.getSunetId();
	assertEquals(sunetid, null);
    }

    public final void testGetSunetIdSecurityCookieModified() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
	    IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, IOException, DecoderException {
	resetCookieTest();
	Cookie cookie = new Cookie(LanewebConstants.SECURITY_COOKIE_NAME, "48784+91+894agag7a7+89910d+h789b10addfs+79adfbv");
	map.put(LanewebConstants.SECURITY_COOKIE_NAME, cookie);
	String sunetid = null;
	UserInfo userInfo = this.userInfoHelper.getUserInfo(this.request);
	sunetid = userInfo.getSunetId();
	assertEquals(sunetid, null);
    }

    public final void testGetSunetIdTime() throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException,
	    NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IOException, DecoderException {
	resetCookieTest();
	GregorianCalendar gc = new GregorianCalendar();
	gc.add(GregorianCalendar.DAY_OF_YEAR, -13);
	String createdDate = String.valueOf(gc.getTime().getTime());
	String cryptedDate = cryptor.encrypt(createdDate);
	Cookie dateCookie = new Cookie(LanewebConstants.DATE_COOKIE_NAME, cryptedDate);
	String encryptedSecurity = cryptor.encrypt(createdDate.concat(sunetid).concat("firefox test"));
	Cookie securityCookie = new Cookie(LanewebConstants.SECURITY_COOKIE_NAME, encryptedSecurity);
	map.put(LanewebConstants.DATE_COOKIE_NAME, dateCookie);
	map.put(LanewebConstants.SECURITY_COOKIE_NAME, securityCookie);
	UserInfo userInfo = this.userInfoHelper.getUserInfo(this.request);
	String sunetid = userInfo.getSunetId();
	assertEquals(sunetid, this.sunetid);
    }

    public final void testGetSunetIdTimePassed() throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException,
	    NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IOException, DecoderException {
	resetCookieTest();
	GregorianCalendar gc = new GregorianCalendar();
	gc.add(GregorianCalendar.DAY_OF_YEAR, -15);
	String createdDate = String.valueOf(gc.getTime().getTime());
	String cryptedDate = cryptor.encrypt(createdDate);
	Cookie dateCookie = new Cookie(LanewebConstants.DATE_COOKIE_NAME, cryptedDate);
	String encryptedSecurity = cryptor.encrypt(createdDate.concat(sunetid).concat("firefox test"));
	Cookie securityCookie = new Cookie(LanewebConstants.SECURITY_COOKIE_NAME, encryptedSecurity);
	map.put(LanewebConstants.DATE_COOKIE_NAME, dateCookie);
	map.put(LanewebConstants.SECURITY_COOKIE_NAME, securityCookie);
	UserInfo userInfo = this.userInfoHelper.getUserInfo(this.request);
	String sunetid = userInfo.getSunetId();
	assertEquals(sunetid, null);
    }

}
