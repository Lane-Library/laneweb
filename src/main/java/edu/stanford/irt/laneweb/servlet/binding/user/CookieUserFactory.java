package edu.stanford.irt.laneweb.servlet.binding.user;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.codec.PersistentLoginToken;
import edu.stanford.irt.laneweb.codec.UserCookieCodec;
import edu.stanford.irt.laneweb.user.User;

public class CookieUserFactory implements UserFactory {

    private UserCookieCodec codec;

    private Logger log;

    private String userIdHashKey;

    public CookieUserFactory(final UserCookieCodec codec, final Logger log, final String userIdHashKey) {
        this.codec = codec;
        this.log = log;
        this.userIdHashKey = userIdHashKey;
    }

    @Override
    public User createUser(final HttpServletRequest request) {
        User user = null;
        Cookie cookie = getUserCookie(request);
        if (cookie != null) {
            user = getUserFromCookie(cookie, request.getHeader("User-Agent"));
        }
        return user;
    }

    private Cookie getUserCookie(final HttpServletRequest request) {
        Cookie userCookie = null;
        Cookie[] cookies = request.getCookies();
        for (int i = 0; cookies!= null && i <  cookies.length && userCookie == null; i++) {
            if (UserCookieCodec.LANE_COOKIE_NAME.equals(cookies[i].getName())) {
                userCookie = cookies[i];
            }
        }
        return userCookie;
    }

    private User getUserFromCookie(final Cookie cookie, final String userAgent) {
        User user = null;
        if (userAgent != null) {
            try {
                PersistentLoginToken token = this.codec.restoreLoginToken(cookie.getValue(), this.userIdHashKey);
                if (token.isValidFor(System.currentTimeMillis(), userAgent.hashCode())) {
                    user = token.getUser();
                }
            } catch (LanewebException e) {
                this.log.error("failed to decode userid from: " + cookie.getValue(), e);
            }
        }
        return user;
    }
}
