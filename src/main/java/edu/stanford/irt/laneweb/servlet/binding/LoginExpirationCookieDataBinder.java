package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.CookieName;

public class LoginExpirationCookieDataBinder implements DataBinder {

    private static final int ONE_DAY = 1000 * 60 * 60 * 24;

    private static final Logger log = LoggerFactory.getLogger("error handler");

    @Override
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            String expiration = null;
            for (int i = 0; expiration == null && i < cookies.length; i++) {
                if (CookieName.EXPIRATION.toString().equals(cookies[i].getName())) {
                    expiration = getExpirationValue(cookies[i].getValue());
                }
            }
            if (expiration != null) {
                model.put(Model.PERSISTENT_LOGIN_EXPIRATION_DATE, expiration);
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
