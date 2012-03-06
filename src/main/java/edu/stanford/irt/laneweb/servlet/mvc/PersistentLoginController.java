package edu.stanford.irt.laneweb.servlet.mvc;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.PersistentLoginFilter;
import edu.stanford.irt.laneweb.servlet.PersistentLoginToken;
import edu.stanford.irt.laneweb.servlet.SunetIdCookieCodec;
import edu.stanford.irt.laneweb.servlet.SunetIdSource;

@Controller
public class PersistentLoginController {

	private SunetIdCookieCodec codec = new SunetIdCookieCodec();

	private SunetIdSource sunetIdSource = new SunetIdSource();

	public static final String PERSISTENT_LOGIN_PREFERENCE = "persistent-preference";

	@RequestMapping(value = "/persistentLogin.html", params = { "url", "pl=renew" })
	public String renewCookieAndRedirect(final String url, HttpServletRequest request, HttpServletResponse response){
		Boolean isActiveSunetID = (Boolean) request.getSession().getAttribute(Model.IS_ACTIVE_SUNETID);
		if (null != isActiveSunetID && isActiveSunetID) {
			setCookies(request, response);
		}
		return "redirect:".concat(url);
	}

	@RequestMapping(value = "/secure/persistentLogin.html", params = { "url", "pl=true" })
	public String loginAndRedirect(final String url, HttpServletRequest request, HttpServletResponse response){
		setCookies(request, response);
		return "redirect:".concat(url);
	}

	@RequestMapping(value = "/**/persistentLogin.html", params = { "url", "pl=false" })
	public String removeCookieAndRedirect(final String url, HttpServletRequest request, HttpServletResponse response){
		removeLoginCookie(request, response);
		return "redirect:".concat(url);
	}

	@RequestMapping(value = "/secure/persistentLogin.html", params = { "!url", "pl=true"})
	public String setCookieAndView(HttpServletRequest request, HttpServletResponse response){
		setCookies(request, response);
		response.setCharacterEncoding("UTF-8");
		return "/persistentlogin.html";
	}

	@RequestMapping(value = "/**/persistentLogin.html", params = { "!url", "pl=false"})
	public String removeCookieAndView(HttpServletRequest request, HttpServletResponse response){
		removeLoginCookie(request, response);
		response.setCharacterEncoding("UTF-8");
		return "/persistentlogin.html";
	}

	private void setCookies(HttpServletRequest request, HttpServletResponse response){
		String sunetid = this.sunetIdSource.getSunetid(request);
		if (null != sunetid) {
			setLoginCookie(request, response, sunetid);
		} else{
			removeLoginCookie(request, response);
		}
	}

	/**
	 * set the lane-user cookie max age to zero.
	 * 
	 * @param response
	 */
	private void removeLoginCookie(HttpServletRequest request,final HttpServletResponse response) {
		 Cookie[] cookies = request.getCookies();
	        if (cookies != null) {
	            for (Cookie webCookie : cookies) {
	                if (PersistentLoginFilter.PERSISTENT_LOGIN_PREFERENCE.equals(webCookie.getName())) {
	                    String cookieValue = webCookie.getValue();
	                	if(!"denied".equals(cookieValue)){//don't want to overwrite denied cookie
	                        webCookie.setPath("/");
	                        webCookie.setMaxAge(0);
	                        response.addCookie(webCookie);
	                	}
	                	break;
	                }
	            }
	        }
		Cookie cookie = new Cookie(SunetIdCookieCodec.LANE_COOKIE_NAME, null);
		cookie.setPath("/");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}

	/**
	 * create and set the lane-user cookie
	 * 
	 * @param sunetid
	 * @param request
	 * @param response
	 */
	private void setLoginCookie(final HttpServletRequest request, final HttpServletResponse response, String sunetid) {
		String userAgent = request.getHeader("User-Agent");
		if (null != userAgent && null != sunetid) {
			int twoWeeks = 3600 * 24 * 7 * 2;
			int gracePeriod = 3600 * 24 * 3; // three days

			PersistentLoginToken token = this.codec.createLoginToken(sunetid, userAgent.hashCode());
			Cookie cookie = new Cookie(SunetIdCookieCodec.LANE_COOKIE_NAME, token.getEncryptedValue());
			cookie.setPath("/");
			cookie.setMaxAge(twoWeeks); // cookie is available for 2 // weeks
			response.addCookie(cookie);

			GregorianCalendar gc = new GregorianCalendar();
			gc.add(Calendar.SECOND, twoWeeks);
			gc.add(Calendar.SECOND, -gracePeriod);
			cookie = new Cookie(PERSISTENT_LOGIN_PREFERENCE, String.valueOf(gc.getTime().getTime()));
			cookie.setPath("/");
			cookie.setMaxAge(twoWeeks); // cookie is available for 2 // weeks
			response.addCookie(cookie);
		}
	}

}
