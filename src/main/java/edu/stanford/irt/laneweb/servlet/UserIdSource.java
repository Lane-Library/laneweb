package edu.stanford.irt.laneweb.servlet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.codec.PersistentLoginToken;
import edu.stanford.irt.laneweb.codec.UserIdCookieCodec;
import edu.stanford.irt.laneweb.model.Model;

/**
 * This tries to discover the userid associated with a request. It does this by looking in a number of places described
 * below. If it finds it it puts it in the session if it wasn't there already.
 */
public class UserIdSource {

    /**
     * this codec codes and decodes the cookie value using userid, useragent and time of creation
     */
    private final UserIdCookieCodec codec;

    private final Logger log;

    public UserIdSource(final UserIdCookieCodec codec, final Logger log) {
        this.codec = codec;
        this.log = log;
    }

    /**
     * looks up the userid from the session, request, and lane-user cookie in that order. If it is not in the session
     * it is put there.
     * 
     * @param request
     *            the servlet request
     * @return the userid
     */
    public String getUserId(final HttpServletRequest request) {
        String userid = null;
        HttpSession session = request.getSession();
        synchronized (session) {
            userid = (String) session.getAttribute(Model.USER_ID);
            if (userid == null) {
                userid = request.getRemoteUser();
                if (userid == null) {
                    userid = getUserIdFromCookie(request.getCookies(), request.getHeader("User-Agent"));
                }
                if (userid != null) {
                    session.setAttribute(Model.USER_ID, userid);
                }
            }
        }
        return userid;
    }

    private String getUserIdFromCookie(final Cookie[] cookies, final String userAgent) {
        String userid = null;
        if (cookies != null && userAgent != null) {
            for (Cookie cookie : cookies) {
                if (UserIdCookieCodec.LANE_COOKIE_NAME.equals(cookie.getName())) {
                    try {
                        PersistentLoginToken token = this.codec.restoreLoginToken(cookie.getValue());
                        if (token.isValidFor(System.currentTimeMillis(), userAgent.hashCode())) {
                            userid = token.getUserId();
                        }
                    } catch (LanewebException e) {
                        this.log.error("failed to decode userid from: " + cookie.getValue(), e);
                    }
                    break;
                }
            }
        }
        return userid;
    }
}
