package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.codec.PersistentLoginToken;
import edu.stanford.irt.laneweb.codec.UserCookieCodec;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.user.LDAPData;
import edu.stanford.irt.laneweb.user.LDAPDataAccess;
import edu.stanford.irt.laneweb.user.User;
import edu.stanford.irt.laneweb.user.User.Status;

public class UserDataBinder implements DataBinder {

    private UserCookieCodec codec;

    private LDAPDataAccess ldap;

    private Logger log;

    private String userIdHashKey;

    public UserDataBinder(final UserCookieCodec codec, final Logger log, final String userIdHashKey,
            final LDAPDataAccess ldap) {
        this.codec = codec;
        this.log = log;
        this.userIdHashKey = userIdHashKey;
        this.ldap = ldap;
    }

    @Override
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        User user = null;
        HttpSession session = request.getSession();
        synchronized (session) {
            user = (User) session.getAttribute(Model.USER);
            if (user == null) {
                user = getUserFromRequest(request);
                if (user == null) {
                    user = getUserFromCookie(request);
                }
                if (user != null) {
                    session.setAttribute(Model.USER, user);
                }
            }
        }
        if (user != null) {
            model.put(Model.USER, user);
            model.put(Model.USER_ID, user.getId());
            model.put(Model.AUTH, user.getHashedId());
            String email = user.getEmail();
            if (email != null) {
                model.put(Model.EMAIL, email);
            }
            String name = user.getName();
            if (name != null) {
                model.put(Model.NAME, name);
            }
            if (user.isStanfordUser()) {
                Boolean isActive = null;
                Status status = user.getStatus();
                if (status == Status.INACTIVE) {
                    isActive = Boolean.FALSE;
                } else {
                    isActive = Boolean.TRUE;
                }
                model.put(Model.IS_ACTIVE_SUNETID, isActive);
            }
        }
    }

    private User getUserFromCookie(final HttpServletRequest request) {
        User user = null;
        Cookie[] cookies = request.getCookies();
        String userAgent = request.getHeader("User-Agent");
        if (cookies != null && userAgent != null) {
            for (Cookie cookie : cookies) {
                if (UserCookieCodec.LANE_COOKIE_NAME.equals(cookie.getName())) {
                    try {
                        PersistentLoginToken token = this.codec
                                .restoreLoginToken(cookie.getValue(), this.userIdHashKey);
                        if (token.isValidFor(System.currentTimeMillis(), userAgent.hashCode())) {
                            user = token.getUser();
                            user = getUserWithStatus(user);
                        }
                    } catch (LanewebException e) {
                        this.log.error("failed to decode userid from: " + cookie.getValue(), e);
                    }
                    break;
                }
            }
        }
        return user;
    }

    private User getUserFromRequest(final HttpServletRequest request) {
        User user = null;
        String userId = request.getRemoteUser();
        if (userId != null) {
            String name = (String) request.getAttribute("displayName");
            String email = (String) request.getAttribute("mail");
            user = getUserWithStatus(new User(userId, name, email, this.userIdHashKey));
        }
        return user;
    }

    private User getUserWithStatus(final User user) {
        if (user.isStanfordUser()) {
            LDAPData data = this.ldap.getLdapDataForSunetid(user.getId());
            Status status = data.isActive() ? Status.ACTIVE : Status.INACTIVE;
            return new User(user.getId() + "@stanford.edu", user.getName(), user.getEmail(), this.userIdHashKey, status);
        } else {
            return user;
        }
    }
}
