package edu.stanford.irt.laneweb.servlet.binding.user;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.codec.PersistentLoginToken;
import edu.stanford.irt.laneweb.codec.UserCookieCodec;
import edu.stanford.irt.laneweb.servlet.CookieName;
import edu.stanford.irt.laneweb.user.User;

public class CookieUserFactory implements UserFactory {

    private static final Logger log = LoggerFactory.getLogger(CookieUserFactory.class);

    private UserCookieCodec codec;

    private String userIdHashKey;

    public CookieUserFactory(final UserCookieCodec codec, final String userIdHashKey) {
        this.codec = codec;
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
        for (int i = 0; cookies != null && i < cookies.length && userCookie == null; i++) {
            if (CookieName.USER.toString().equals(cookies[i].getName())) {
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
                log.error("failed to decode userid from cookie: value='{}'", cookie.getValue());
            }
        }
        return user;
    }
}
