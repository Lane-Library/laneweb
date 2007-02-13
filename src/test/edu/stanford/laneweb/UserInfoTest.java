package edu.stanford.laneweb;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.cocoon.environment.Context;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;

public class UserInfoTest extends TestCase {

	private UserInfo userInfo;
	private Map<String, Object> objectModel;
	private Request request;
	private Context context;
	private String ip;
	private String ezproxyKey;
	private String sunetid;
	protected void setUp() throws Exception {
		super.setUp();
		this.userInfo = new UserInfo();
		this.objectModel = new HashMap<String, Object>();
		this.request = createMock(Request.class);
		this.context = createMock(Context.class);
		this.objectModel.put(ObjectModelHelper.REQUEST_OBJECT, this.request);
		this.objectModel.put(ObjectModelHelper.CONTEXT_OBJECT, this.context);
		this.ip = "171.65.28.124";
		this.ezproxyKey = "abcdefg";
		this.sunetid = "ceyates";
		expect(this.request.getRemoteAddr()).andReturn(this.ip);
		expect(this.request.getHeader(LanewebConstants.X_FORWARDED_FOR)).andReturn(null);
		expect(this.request.getAttribute(LanewebConstants.WEBAUTH_USER)).andReturn(this.sunetid);
		expect(this.request.getParameter(LanewebConstants.PROXY_LINKS)).andReturn("true");
		expect(this.request.getParameter(LanewebConstants.PROXY_LINKS)).andReturn("true");
		replay(this.request);
		expect(this.context.getAttribute(LanewebConstants.EZPROXY_KEY)).andReturn(this.ezproxyKey);
		replay(this.context);
	}

	public void testUpdate() {
		try {
			this.userInfo.update(null);
			fail("expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {}
		this.userInfo.update(this.objectModel);
		verify(this.request);
		verify(this.context);
	}

	public void testGetAffiliation() {
		this.userInfo.update(this.objectModel);
		assertEquals(Affiliation.SOM, this.userInfo.getAffiliation());
		verify(this.request);
		verify(this.context);
	}

	public void testGetProxyLinks() {
		this.userInfo.update(this.objectModel);
		assertTrue(this.userInfo.getProxyLinks());
		verify(this.request);
		verify(this.context);
	}

	public void testGetSunetId() {
		this.userInfo.update(this.objectModel);
		assertEquals(this.sunetid, this.userInfo.getSunetId());
		verify(this.request);
		verify(this.context);
	}

	public void testGetTicket() {
		this.userInfo.update(this.objectModel);
		assertNotNull(this.userInfo.getTicket());
		assertEquals(46,this.userInfo.getTicket().toString().length());
		verify(this.request);
		verify(this.context);
	}

}
