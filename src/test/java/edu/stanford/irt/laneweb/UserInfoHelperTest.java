package edu.stanford.irt.laneweb;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.verify;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;

import edu.stanford.irt.directory.LDAPPerson;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.Cookie;

import junit.framework.TestCase;

import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.commons.codec.DecoderException;

public class UserInfoHelperTest extends TestCase {

    private Request request;

    private String ip;

    private String sunetid;

    private String ezproxyKey = "boguskey";

    private LdapClient ldapClient;

    private Session session;

    private UserInfoHelper userInfoHelper;

    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // this.ldapClient = createMock(LdapClient.class);

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

    public void testCookieUserInfo() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, IOException, DecoderException {
        Cryptor cryptor = new Cryptor();
        cryptor.setKey("testtesttesttesttestt");
        String cookieValue = cryptor.encrypt(this.sunetid);
	Cookie cookie = new Cookie(LanewebConstants.USER_COOKIE_NAME, cookieValue);
	HashMap<String, Cookie> map = new HashMap<String, Cookie>();
	map.put(LanewebConstants.USER_COOKIE_NAME, cookie);
	expect(this.request.getRemoteAddr()).andReturn(this.ip);
	expect(this.request.getRemoteUser()).andReturn(null);
	expect(this.request.getCookieMap()).andReturn(map);
	replay(this.request);
	this.userInfoHelper.setDecryptor(cryptor);
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

}
