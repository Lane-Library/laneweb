package edu.stanford.irt.laneweb.servlet.binding;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.PersistentLoginToken;
import edu.stanford.irt.laneweb.servlet.SunetIdCookieCodec;

/**
 * A servlet Filter that tries to discover the sunetid associated with a request. It does this by looking in a number of
 * places described below. If it finds it it sets a request attribute 'sunetid' for use later in processing. It also
 * responds to the 'pl' and 'remove-pl' request parameters by setting the lane-user cookie as appropriate.
 * 
 * @author ceyates $Id$
 */
public class SunetIdSource {

    /**
     * this codec codes and decodes the cookie value using sunet id, useragent and time of creation
     */
    private SunetIdCookieCodec codec = new SunetIdCookieCodec();

    private Logger log = LoggerFactory.getLogger(SunetIdSource.class);

    /**
     * doFilter looks up the sunet id from the session, request, and lane-user cookie in that order. If it is not in the
     * session it is put there.
     */
    public String getSunetid(final HttpServletRequest request, final HttpSession session) {
        String sessionSunetid = (String) session.getAttribute(Model.SUNETID);
        String sunetid = sessionSunetid == null ? getSunetidFromRequest(request) : sessionSunetid;
        if (sunetid == null) {
            sunetid = getSunetidFromCookie(request.getCookies(), request.getHeader("User-Agent"));
        }
        if (sunetid != null && sessionSunetid == null) {
            session.setAttribute(Model.SUNETID, sunetid);
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
                    } catch (IllegalStateException e) {
                        this.log.error("failed to decode sunetid from: " + cookie.getValue(), e);
                    }
                    break;
                }
            }
        }
        return sunetid;
    }

    private String getSunetidFromRequest(final HttpServletRequest request) {
        String sunetid = request.getRemoteUser();
        if (sunetid == null) {
            sunetid = request.getHeader("X-WEBAUTH-USER");
            if ("(null)".equals(sunetid)) {
                sunetid = null;
            }
        }
        return sunetid;
    }
}
