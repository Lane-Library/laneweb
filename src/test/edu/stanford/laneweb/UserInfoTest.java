package edu.stanford.laneweb;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.avalon.framework.logger.Logger;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;

public class UserInfoTest extends TestCase {

	private UserInfo userInfo;
	private Map<String, Object> objectModel;
	private Request request;
	private String ip;
	private String sunetid;
	private Logger log; 
	protected void setUp() throws Exception {
		super.setUp();
		this.userInfo = new UserInfo();
		this.objectModel = new HashMap<String, Object>();
		this.request = createMock(Request.class);
		this.objectModel.put(ObjectModelHelper.REQUEST_OBJECT, this.request);
		this.ip = "171.65.28.124";
		this.sunetid = "ceyates";
		this.log = createMock(Logger.class);
		expect(this.request.getRemoteAddr()).andReturn(this.ip);
		expect(this.request.getHeader(LanewebConstants.X_FORWARDED_FOR)).andReturn(null);
		expect(this.request.getAttribute(LanewebConstants.WEBAUTH_USER)).andReturn(this.sunetid);
		expect(this.request.getParameter(LanewebConstants.PROXY_LINKS)).andReturn("true");
		expect(this.request.getParameter(LanewebConstants.PROXY_LINKS)).andReturn("true");
		replay(this.request);
	}

	public void testUpdate() {
		try {
			this.userInfo.update(null, log);
			fail("expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {}
		try {
			this.userInfo.update(this.objectModel, null);
			fail("expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {}
		this.userInfo.update(this.objectModel, log);
		verify(this.request);
	}

	public void testGetAffiliation() {
		this.userInfo.update(this.objectModel, log);
		assertEquals(Affiliation.SOM, this.userInfo.getAffiliation());
		verify(this.request);
	}

	public void testGetProxyLinks() {
		this.userInfo.update(this.objectModel, log);
		assertTrue(this.userInfo.getProxyLinks());
		verify(this.request);
	}

	public void testGetSunetId() {
		this.userInfo.update(this.objectModel, log);
		assertEquals(this.sunetid, this.userInfo.getSunetId());
		verify(this.request);
	}

	public void testGetTicket() {
		this.userInfo.update(this.objectModel, log);
		assertNotNull(this.userInfo.getTicket());
		assertEquals(46,this.userInfo.getTicket().toString().length());
		verify(this.request);
	}

}
