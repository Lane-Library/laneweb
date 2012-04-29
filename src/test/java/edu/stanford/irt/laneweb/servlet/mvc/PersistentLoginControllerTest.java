package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.servlet.PersistentLoginFilter;


public class PersistentLoginControllerTest {

	private PersistentLoginController persistenLoginController;

	private HttpServletRequest request;

	private HttpServletResponse response;

	 private HttpSession session;
	
	private String url = "/test.html";

	
	@Before
	public void setUp() throws Exception {
		this.persistenLoginController = new PersistentLoginController();
		this.request = createMock(HttpServletRequest.class);
		this.response = createMock(HttpServletResponse.class);
		this.session = createMock(HttpSession.class);
		this.response.addCookie(isA(Cookie.class));
		this.response.addCookie(isA(Cookie.class));
	}

	
	@Test
	public void testCreateCookieNotNullUrl() {
		expect(this.session.getAttribute("sunetid")).andReturn("alainb").times(2);
		expect(this.request.getSession()).andReturn(this.session).times(2);
		expect(this.request.getHeader("User-Agent")).andReturn("firefox");
		this.response.addCookie(isA(Cookie.class));
		replay(this.request, this.response, this.session);
		String viewUrl = this.persistenLoginController.createCookie(this.url, this.request, this.response);
		assertEquals(viewUrl, "redirect:/test.html");
		verify(this.request, this.session, this.response);
	}
	
	@Test
	public void testCreateCookieNullUrl() {
		expect(this.session.getAttribute("sunetid")).andReturn("alainb").times(2);
		expect(this.request.getSession()).andReturn(this.session).times(2);
		expect(this.request.getHeader("User-Agent")).andReturn("firefox");
		this.response.addCookie(isA(Cookie.class));
		this.response.setCharacterEncoding(isA(String.class));
		replay(this.request, this.response, this.session);
		String viewUrl = this.persistenLoginController.createCookie(null, this.request, this.response);
		assertEquals(viewUrl, "redirect:/myaccounts.html");
		verify(this.request, this.session, this.response);
	}
	
	@Test
	public void testCreateCookieSunetIdNull() {
		expect(this.request.getSession()).andReturn(this.session).times(2);
		expect(this.request.getRemoteUser()).andReturn(null).times(2);
		expect(this.request.getHeader("X-WEBAUTH-USER")).andReturn(null).times(2);
		expect(this.request.getHeader("User-Agent")).andReturn("firefox").times(2);
		expect(this.session.getAttribute("sunetid")).andReturn(null).times(2);
		Cookie[] cookies = new Cookie[1];
		cookies[0] = new Cookie(PersistentLoginFilter.PERSISTENT_LOGIN_PREFERENCE, null);
		this.response.addCookie(isA(Cookie.class));
		this.response.addCookie(isA(Cookie.class));
		expect(this.request.getCookies()).andReturn(cookies).times(3);
		replay(this.request, this.session, this.response);
		this.persistenLoginController.createCookie(this.url, this.request, this.response);
		verify(this.request, this.session, this.response);
	}
	

	@Test
	public void testRemoveCookieUrlNotNull() {
		expect(this.session.getAttribute("sunetid")).andReturn("alainb");
		Cookie[] cookies = new Cookie[1];
		cookies[0] = new Cookie(PersistentLoginFilter.PERSISTENT_LOGIN_PREFERENCE, "234890");
		expect(this.request.getCookies()).andReturn(cookies);
		expect(this.request.getSession()).andReturn(this.session);
		this.response.addCookie(isA(Cookie.class));
		replay(this.request, this.session, this.response);
		String viewUrl = this.persistenLoginController.removeCookieAndView(this.url, this.request, this.response);
		assertEquals(viewUrl, "redirect:/test.html");
		verify(this.request, this.session, this.response);
	}
	
	@Test
	public void testRemoveCookieUrlNull() {
		expect(this.session.getAttribute("sunetid")).andReturn("alainb");
		Cookie[] cookies = new Cookie[1];
		cookies[0] = new Cookie(PersistentLoginFilter.PERSISTENT_LOGIN_PREFERENCE, "234033");
		expect(this.request.getCookies()).andReturn(cookies);
		expect(this.request.getSession()).andReturn(this.session);
		this.response.addCookie(isA(Cookie.class));
		this.response.setCharacterEncoding(isA(String.class));
		replay(this.request, this.session, this.response);
		String viewUrl = this.persistenLoginController.removeCookieAndView(null, this.request, this.response);
		assertEquals(viewUrl, "redirect:/myaccounts.html");
		verify(this.request, this.session, this.response);
	}
	
	@Test
	public void testRemoveWithDeniedCookie() {
		expect(this.session.getAttribute("sunetid")).andReturn("alainb");
		Cookie[] cookies = new Cookie[1];
		cookies[0] = new Cookie(PersistentLoginFilter.PERSISTENT_LOGIN_PREFERENCE, "denied");
		expect(this.request.getCookies()).andReturn(cookies);
		expect(this.request.getSession()).andReturn(this.session);
		replay(this.request, this.session, this.response);
		String viewUrl = this.persistenLoginController.removeCookieAndView(this.url, this.request, this.response);
		assertEquals(viewUrl, "redirect:/test.html");
		verify(this.request, this.session, this.response);
	}
	
	@Test
	public void testRenewCookieActiveSunetId() {
		expect(this.session.getAttribute("isActiveSunetID")).andReturn(true);
		expect(this.session.getAttribute("sunetid")).andReturn("alainb");
		expect(this.request.getSession()).andReturn(this.session).times(2);
		expect(this.request.getHeader("User-Agent")).andReturn("firefox");
		this.response.addCookie(isA(Cookie.class));
		replay(this.request, this.response, this.session);
		String viewUrl = this.persistenLoginController.renewCookieAndRedirect(this.url, this.request, this.response);
		assertEquals(viewUrl, "redirect:/test.html");
		verify(this.request, this.session, this.response);
	}
	
	@Test
	public void testRenewCookieNotActiveSunetId() {
		expect(this.session.getAttribute("isActiveSunetID")).andReturn(false);
		Cookie[] cookies = new Cookie[1];
		cookies[0] = new Cookie(PersistentLoginFilter.PERSISTENT_LOGIN_PREFERENCE, null);
		expect(this.request.getCookies()).andReturn(cookies);
		expect(this.request.getSession()).andReturn(this.session);
		this.response.addCookie(isA(Cookie.class));
		this.response.addCookie(isA(Cookie.class));
		replay(this.request, this.session, this.response);
		String viewUrl = this.persistenLoginController.renewCookieAndRedirect(this.url, this.request, this.response);
		assertEquals(viewUrl, "redirect:/test.html");
		verify(this.request, this.session, this.response);		
	}
	
}
