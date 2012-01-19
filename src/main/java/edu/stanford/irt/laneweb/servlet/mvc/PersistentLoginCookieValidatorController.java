package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.stanford.irt.laneweb.servlet.PersistentLoginToken;
import edu.stanford.irt.laneweb.servlet.SunetIdCookieCodec;

/**
 * Checks the validity of the Lane user cookie AND if cookie will still be valid GRACE_PERIOD from now. Used to provide
 * an alert to mobile users when their cookie is about to expire.
 * 
 * @author ryanmax
 */
@Controller
public class PersistentLoginCookieValidatorController {

    private static final long GRACE_PERIOD = 1000 * 60 * 60 * 24 * 3; // 3 days
    
    private static final Cookie[] NO_COOKIES = new Cookie[0];

    private static final String USER_AGENT_HEADER = "User-Agent";

    private SunetIdCookieCodec codec = new SunetIdCookieCodec();

    @RequestMapping(value = "**/apps/loginCookieValidator")
    public void validateCookie(final HttpServletRequest request, final HttpServletResponse response,
            @RequestParam(required = false) final String callback) throws IOException {
        boolean valid = false;
        boolean validDuringGracePeriod = false;
        String userAgent = request.getHeader(USER_AGENT_HEADER);
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            cookies = NO_COOKIES;
        }
        for (Cookie cookie : cookies) {
            if (SunetIdCookieCodec.LANE_COOKIE_NAME.equals(cookie.getName())) {
                PersistentLoginToken token = this.codec.restoreLoginToken(cookie.getValue());
                if (token.isValidFor(System.currentTimeMillis(), userAgent.hashCode())) {
                    valid = true;
                    validDuringGracePeriod = token.isValidFor(System.currentTimeMillis() + GRACE_PERIOD,
                            userAgent.hashCode());
                }
                break;
            }
        }
        response.setHeader("Content-Type", "application/x-javascript");
        if (callback != null) {
            response.getWriter().write(callback + "(" + valid + "," + validDuringGracePeriod + ");");
        } else {
            response.getWriter().write(valid + "," + validDuringGracePeriod);
        }
    }
}
