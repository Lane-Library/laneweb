package edu.stanford.irt.laneweb.servlet.mvc;

import edu.stanford.irt.laneweb.servlet.PersistentLoginToken;
import edu.stanford.irt.laneweb.servlet.SunetIdCookieCodec;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Checks the validity of the Lane user cookie AND if cookie will still be valid GRACE_PERIOD from now. Used to provide
 * an alert to mobile users when their cookie is about to expire.
 * 
 * @author ryanmax
 */
@Controller
public class PersistentLoginCookieValidatorController {

    private static long GRACE_PERIOD = 1000 * 60 * 60 * 24 * 3; // 3 days

    private static final String USER_AGENT_HEADER = "User-Agent";

    private SunetIdCookieCodec codec = new SunetIdCookieCodec();

    private String userAgent;

    private boolean valid;

    private boolean validDuringGracePeriod;

    @RequestMapping(value = "**/apps/loginCookieValidator")
    public void validateCookie(final HttpServletRequest request, final HttpServletResponse response,
            @RequestParam(required = false) final String callback) throws IOException {
        this.valid = false;
        this.validDuringGracePeriod = false;
        this.userAgent = request.getHeader(USER_AGENT_HEADER);
        for (Cookie cookie : request.getCookies()) {
            if (SunetIdCookieCodec.LANE_COOKIE_NAME.equals(cookie.getName())) {
                PersistentLoginToken token = this.codec.restoreLoginToken(cookie.getValue());
                if (token.isValidFor(System.currentTimeMillis(), this.userAgent.hashCode())) {
                    this.valid = true;
                    this.validDuringGracePeriod = token.isValidFor(System.currentTimeMillis() + GRACE_PERIOD,
                            this.userAgent.hashCode());
                }
                break;
            }
        }
        response.setHeader("Content-Type", "application/x-javascript");
        if (callback != null) {
            response.getWriter().write(callback + "(" + this.valid + "," + this.validDuringGracePeriod + ");");
        } else {
            response.getWriter().write(this.valid + "," + this.validDuringGracePeriod);
        }
    }
}
