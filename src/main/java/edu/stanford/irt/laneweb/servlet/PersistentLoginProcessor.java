package edu.stanford.irt.laneweb.servlet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.stanford.irt.laneweb.LanewebConstants;
import edu.stanford.irt.laneweb.model.Model;


public class PersistentLoginProcessor {

    /**
     * this codec codes and decodes the cookie value using sunet id, useragent
     * and time of creation
     */
    private SunetIdCookieCodec codec = new SunetIdCookieCodec();
    
    public void processSunetid(String sunetid, HttpServletRequest request, HttpServletResponse response) {

        if (null != sunetid) {
            request.setAttribute(Model.SUNETID, sunetid);
            if (Boolean.parseBoolean(request.getParameter(("pl")))) {
                setLoginCookie(sunetid, (HttpServletRequest) request, (HttpServletResponse) response);
            } else if (Boolean.parseBoolean(request.getParameter("remove-pl"))) {
                removeLoginCookie((HttpServletResponse) response);
            }
        }
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
}
