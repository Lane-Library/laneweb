package edu.stanford.laneweb;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

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

		UserInfo userInfo = new UserInfo(); 
		expect(this.request.getAttribute(LanewebConstants.USER_INFO)).andReturn(null);
		expect(this.session.getAttribute(LanewebConstants.USER_INFO)).andReturn(userInfo);
		this.request.setAttribute(LanewebConstants.USER_INFO, userInfo);
		
		expect(this.request.getSession(true)).andReturn(this.session);
		expect(this.request.getRemoteAddr()).andReturn(this.ip);
		expect(this.request.getHeader(LanewebConstants.X_FORWARDED_FOR)).andReturn(null);
		expect(this.request.getAttribute(LanewebConstants.WEBAUTH_USER)).andReturn(this.sunetid);
		expect(this.request.getParameter(LanewebConstants.PROXY_LINKS)).andReturn("true");
		expect(this.request.getParameter(LanewebConstants.PROXY_LINKS)).andReturn("true");
		replay(ldapClient);
		replay(this.session);
		replay(this.request);	
		
	}



	public void testGetUserInfo() {
		
		UserInfoHelper userInfoHelper = new UserInfoHelperImpl();
	
		userInfoHelper.setLdapClient(this.ldapClient);
		UserInfo userInfo =  userInfoHelper.getUserInfo(this.request);
		assertEquals(Affiliation.SOM, userInfo.getAffiliation());
		assertTrue(userInfo.getProxyLinks());
		assertEquals(this.sunetid, userInfo.getSunetId());
		assertEquals(46,userInfo.getTicket().toString().length());
		assertEquals(this.sunetid, userInfo.getLdapPerson().getSunetId()[0]);
		verify(this.session);
		verify(this.request);
		}


}
