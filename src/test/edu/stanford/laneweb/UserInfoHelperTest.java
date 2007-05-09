package edu.stanford.laneweb;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.easymock.EasyMock.isA;

import edu.stanford.irt.directory.LDAPPerson;

import junit.framework.TestCase;

import org.apache.avalon.framework.logger.Logger;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;

import com.sun.java_cup.internal.version;

public class UserInfoHelperTest extends TestCase {

	private Request request;
	private String ip;
	private String sunetid;
	private Logger log; 
	private LdapClient ldapClient;
	private Session session;
	private UserInfoHelper userInfoHelper;
	
	
	protected void setUp() throws Exception {
		super.setUp();
		//this.ldapClient = createMock(LdapClient.class);

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
		expect(this.request.getAttribute(LanewebConstants.WEBAUTH_USER)).andReturn(this.sunetid);
		expect(this.request.getParameter(LanewebConstants.PROXY_LINKS)).andReturn("true");
		expect(this.request.getParameter(LanewebConstants.PROXY_LINKS)).andReturn("true");
		replay(ldapClient);
		replay(this.session);
	
		userInfoHelper = new UserInfoHelperImpl();
		userInfoHelper.setLdapClient(this.ldapClient);

	}



	public void testGetUserInfo() {
		expect(this.request.getRemoteAddr()).andReturn(this.ip);
		replay(this.request);	
		UserInfo userInfo =  userInfoHelper.getUserInfo(this.request);
		assertEquals(Affiliation.SOM, userInfo.getAffiliation());
		assertTrue(userInfo.getProxyLinks());
		assertEquals(this.sunetid, userInfo.getSunetId());
		assertNotNull(userInfo.getTicket());
		assertEquals(46,userInfo.getTicket().toString().length());
		assertEquals(this.sunetid, userInfo.getLdapPerson().getSunetId()[0]);
		verify(this.session);
		verify(this.request);
		}

	public void testAffiliation() {
		this.ip = this.ip.concat("FAIL_TEST");
		expect(this.request.getRemoteAddr()).andReturn(ip);
		replay(this.request);	
		UserInfo userInfo =  userInfoHelper.getUserInfo(this.request);
		assertEquals(Affiliation.ERR, userInfo.getAffiliation());
		verify(this.session);
		verify(this.request);
	}

}
