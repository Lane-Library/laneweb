package edu.stanford.irt.laneweb.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.stanford.irt.laneweb.LanewebConstants;
import edu.stanford.irt.laneweb.model.LanewebObjectModel;
import edu.stanford.irt.laneweb.user.User;

/**
 * A servlet Filter that tries to discover the sunetid associated with a
 * request. It does this by looking in a number of places described below. If it
 * finds it it sets a request attribute 'sunetid' for use later in processing.
 * It also responds to the 'pl' and 'remove-pl' request parameters by setting
 * the lane-user cookie as appropriate.
 * 
 * @author ceyates
 * $Id$
 */
public class SunetIdFilter implements Filter {

    /**
     * this codec codes and decodes the cookie value using sunet id, useragent
     * and time of creation
     */
    private SunetIdCookieCodec codec = new SunetIdCookieCodec();

    public void destroy() {
    }

    /**
     * doFilter looks up the sunet id from the request, session, and lane-user
     * cookie in that order and sets it as a request attribute. If there is a sunetid
     * and a 'pl' request parameter of 'true', a lane user cookie is added to the
     * response with a max age of two weeks. If there is a sunetid and a 'remove-pl'
     * request parameter of 'true' the lane user cookie is set with a max age of zero.
     */
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        String sunetid = getSunetId((HttpServletRequest) request);
        if (null != sunetid) {
            request.setAttribute(LanewebObjectModel.SUNETID, sunetid);
            if (Boolean.parseBoolean(request.getParameter(("pl")))) {
                setLoginCookie(sunetid, (HttpServletRequest) request, (HttpServletResponse) response);
            } else if (Boolean.parseBoolean(request.getParameter("remove-pl"))) {
                removeLoginCookie((HttpServletResponse) response);
            }
        }
        chain.doFilter(request, response);
    }

    public void init(final FilterConfig filterConfig) throws ServletException {
    }

    /**
     * Try to discover the sunet id associated with the request.
     * 
     * @param request
     * @return the associated sunetid or null;
     */
    private String getSunetId(final HttpServletRequest request) {

        String sunetid = getSunetidFromRequest(request);
        
        if (sunetid == null) {
            sunetid = getSunetidFromSession(request.getSession(false));
        }
        
        if (sunetid == null) {
            sunetid = getSunetidFromCookie(request.getCookies(), request.getHeader("User-Agent"));
        }
        return sunetid;
    }
    
    private String getSunetidFromCookie(Cookie[] cookies, String userAgent) {
        String sunetid = null;
        if (cookies != null && userAgent != null) {
            for (Cookie cookie : cookies) {
                if (LanewebConstants.LANE_COOKIE_NAME.equals(cookie.getName())) {
                    PersistentLoginToken token = this.codec.restoreLoginToken(cookie.getValue());
                    if (token.isValidFor(System.currentTimeMillis(), userAgent.hashCode())) {
                        sunetid = token.getSunetId();
                    }
                    break;
                }
            }
        }
        return sunetid;
    }

    private String getSunetidFromSession(HttpSession session) {
        String sunetid = null;
        if (session != null) {
            sunetid = (String) session.getAttribute(LanewebObjectModel.SUNETID);
        }
        return sunetid;
    }

    private String getSunetidFromRequest(HttpServletRequest request) {
        String sunetid = request.getRemoteUser();
        if (sunetid == null) {
            sunetid = request.getHeader("X-WEBAUTH-USER");
            if ("(null)".equals(sunetid)) {
                sunetid = null;
            }
        }
        return sunetid;
    }

    /**
     * set the lane-user cookie max age to zero.
     * 
     * @param response
     */
    private void removeLoginCookie(final HttpServletResponse response) {
        Cookie cookie = new Cookie(LanewebConstants.LANE_COOKIE_NAME, null);
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
    private void setLoginCookie(final String sunetid, final HttpServletRequest request, final HttpServletResponse response) {
        String userAgent = request.getHeader("User-Agent");
        if (null != userAgent) {
            PersistentLoginToken token = this.codec.createLoginToken(sunetid, userAgent.hashCode());
            Cookie cookie = new Cookie(LanewebConstants.LANE_COOKIE_NAME, token.getEncryptedValue());
            cookie.setPath("/");
            cookie.setMaxAge(3600 * 24 * 7 * 2); // cookie is available for 2
                                                 // weeks
            response.addCookie(cookie);
        }
    }
}
