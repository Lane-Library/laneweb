package edu.stanford.irt.laneweb.servlet.binding.user;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.codec.PersistentLoginToken;
import edu.stanford.irt.laneweb.codec.UserCookieCodec;
import edu.stanford.irt.laneweb.user.LDAPData;
import edu.stanford.irt.laneweb.user.LDAPDataAccess;
import edu.stanford.irt.laneweb.user.User;
import edu.stanford.irt.laneweb.user.User.Status;

public class CookieUserFactory implements UserFactory {

    private UserCookieCodec codec;

    private LDAPDataAccess ldap;

    private Logger log;

    private String userIdHashKey;

    public CookieUserFactory(final UserCookieCodec codec, final LDAPDataAccess ldap, final Logger log,
            final String userIdHashKey) {
        this.codec = codec;
        this.ldap = ldap;
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
        for (int i = 0; i < cookies.length && userCookie == null; i++) {
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
                    user = getUserWithStatus(user);
                }
            } catch (LanewebException e) {
                this.log.error("failed to decode userid from: " + cookie.getValue(), e);
            }
        }
        return user;
    }

    private User getUserWithStatus(final User user) {
        if (user.isStanfordUser()) {
            String userid = user.getId();
            String sunetid = userid.substring(0, userid.indexOf('@'));
            LDAPData data = this.ldap.getLdapDataForSunetid(sunetid);
            Status status = data.isActive() ? Status.ACTIVE : Status.INACTIVE;
            return new User(userid, user.getName(), user.getEmail(), this.userIdHashKey, status);
        }
        return user;
    }
}
