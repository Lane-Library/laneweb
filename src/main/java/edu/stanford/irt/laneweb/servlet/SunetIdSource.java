package edu.stanford.irt.laneweb.servlet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.codec.PersistentLoginToken;
import edu.stanford.irt.laneweb.codec.SunetIdCookieCodec;
import edu.stanford.irt.laneweb.model.Model;

/**
 * This tries to discover the sunetid associated with a request. It does this by looking in a number of places described
 * below. If it finds it it puts it in the session if it wasn't there already.
 */
public class SunetIdSource {

    /**
     * this codec codes and decodes the cookie value using sunet id, useragent and time of creation
     */
    private final SunetIdCookieCodec codec;

    private final Logger log;

    public SunetIdSource(final SunetIdCookieCodec codec, final Logger log) {
        this.codec = codec;
        this.log = log;
    }

    /**
     * looks up the sunet id from the session, request, and lane-user cookie in that order. If it is not in the session
     * it is put there.
     * 
     * @param request
     *            the servlet request
     * @return the sunetid
     */
    public String getSunetid(final HttpServletRequest request) {
        String sunetid = null;
        HttpSession session = request.getSession();
        synchronized (session) {
            sunetid = (String) session.getAttribute(Model.SUNETID);
            if (sunetid == null) {
                sunetid = request.getRemoteUser();
                if (sunetid == null) {
                    sunetid = getSunetidFromCookie(request.getCookies(), request.getHeader("User-Agent"));
                }
                if (sunetid != null) {
                    session.setAttribute(Model.SUNETID, sunetid);
                }
            }
        }
        return sunetid;
    }

    private String getSunetidFromCookie(final Cookie[] cookies, final String userAgent) {
        String sunetid = null;
        if (cookies != null && userAgent != null) {
            for (Cookie cookie : cookies) {
                if (SunetIdCookieCodec.LANE_COOKIE_NAME.equals(cookie.getName())) {
                    try {
                        PersistentLoginToken token = this.codec.restoreLoginToken(cookie.getValue());
                        if (token.isValidFor(System.currentTimeMillis(), userAgent.hashCode())) {
                            sunetid = token.getSunetId();
                        }
                    } catch (LanewebException e) {
                        this.log.error("failed to decode sunetid from: " + cookie.getValue(), e);
                    }
                    break;
                }
            }
        }
        return sunetid;
    }
}
