package edu.stanford.irt.laneweb.servlet;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PersistentLoginFilter extends AbstractLanewebFilter {

    /**
     * this codec codes and decodes the cookie value using sunet id, useragent
     * and time of creation
     */
    private SunetIdCookieCodec codec = new SunetIdCookieCodec();

    private SunetIdSource sunetIdSource = new SunetIdSource();

    @Override
    public void internalDoFilter(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain)
            throws IOException, ServletException {
        if (Boolean.parseBoolean(request.getParameter(("pl")))) {
            String sunetid = this.sunetIdSource.getSunetid(request);
            if (sunetid != null) {
                setLoginCookie(sunetid, request, response);
            }
        } else if (Boolean.parseBoolean(request.getParameter("remove-pl"))) {
            removeLoginCookie(response);
        }
        chain.doFilter(request, response);
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
    private void setLoginCookie(final String sunetid, final HttpServletRequest request, final HttpServletResponse response) {
        String userAgent = request.getHeader("User-Agent");
        if (null != userAgent) {
            PersistentLoginToken token = this.codec.createLoginToken(sunetid, userAgent.hashCode());
            Cookie cookie = new Cookie(SunetIdCookieCodec.LANE_COOKIE_NAME, token.getEncryptedValue());
            cookie.setPath("/");
            cookie.setMaxAge(3600 * 24 * 7 * 2); // cookie is available for 2
                                                 // weeks
            response.addCookie(cookie);
        }
    }
}
