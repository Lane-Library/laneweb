package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.CookieName;

public class UserCookieDataBinder implements DataBinder {

    private static final int MAX_USER_COOKIES = 2;

    private static final int ONE_DAY = 1000 * 60 * 60 * 24;

    private Logger log;

    public UserCookieDataBinder(final Logger log) {
        this.log = log;
    }

    //TODO: determine if the user cookie needs to be in the model, I think not.
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            int userCookieCount = 0;
            String name = null;
            for (int i = 0; userCookieCount < MAX_USER_COOKIES && i < cookies.length; i++) {
                name = cookies[i].getName();
                if (CookieName.USER.toString().equals(name)) {
                    model.put(Model.USER_COOKIE, cookies[i].getValue());
                    userCookieCount++;
                } else if (CookieName.EXPIRATION.toString().equals(name)) {
                    String expiration = getExpirationValue(cookies[i].getValue());
                    if (expiration != null) {
                        model.put(Model.PERSISTENT_LOGIN_EXPIRATION_DATE, expiration);
                    }
                    userCookieCount++;
                }
            }
        }
    }

    private String getExpirationValue(final String cookieValue) {
        String result = null;
        try {
            long expiry = Long.valueOf(cookieValue) - System.currentTimeMillis();
            if (expiry > 0) {
                result = Long.toString(expiry / ONE_DAY);
            }
        } catch (NumberFormatException e) {
            this.log.error(e.getMessage(), e);
            result = "ERROR";
        }
        return result;
    }
}
