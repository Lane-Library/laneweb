package edu.stanford.irt.laneweb.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.stanford.irt.laneweb.model.Model;

public class PersistentLoginFilter extends AbstractLanewebFilter {

	public static final String PERSISTENT_LOGIN_PREFERENCE = "persistent-preference";

	/**
	 * this codec codes and decodes the cookie value using sunet id, useragent
	 * and time of creation
	 */
	private SunetIdCookieCodec codec = new SunetIdCookieCodec();

	private SunetIdSource sunetIdSource = new SunetIdSource();

	@Override
	public void internalDoFilter(final HttpServletRequest request, final HttpServletResponse response,
			final FilterChain chain)
			throws IOException, ServletException {
		String sunetid = this.sunetIdSource.getSunetid(request);
		if (null != sunetid) {
			Boolean isActiveSunetID = (Boolean) request.getSession().getAttribute(Model.IS_ACTIVE_SUNETID);
			if ("renew".equals(request.getParameter(("pl"))) && null != isActiveSunetID && isActiveSunetID) {
				setLoginCookie(request, response, sunetid);
			}
			else if (Boolean.parseBoolean(request.getParameter(("pl")))) {
				setLoginCookie(request, response, sunetid);
			}
			else if (!Boolean.parseBoolean(request.getParameter(("pl")))) {
				removeLoginCookie(response);
			}
		} else
		{
			removeLoginCookie(response);
		}

		String url = request.getParameter("url");
		if (null != url && !"".equals(url)) {
			url = URLDecoder.decode(url, "UTF-8");
			response.sendRedirect(url);
		}
		else {
			chain.doFilter(request, response);
		}
	}

	/**
	 * set the lane-user cookie max age to zero.
	 * 
	 * @param response
	 */
	private void removeLoginCookie(final HttpServletResponse response) {
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
