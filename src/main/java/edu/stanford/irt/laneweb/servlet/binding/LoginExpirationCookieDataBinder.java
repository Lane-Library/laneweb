package edu.stanford.irt.laneweb.servlet.binding;

import java.time.Clock;
import java.time.Duration;
import java.util.Map;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.irt.laneweb.servlet.CookieName;

public class LoginExpirationCookieDataBinder implements DataBinder {

    private static final Logger log = LoggerFactory.getLogger(LoginExpirationCookieDataBinder.class);

    private static final long ONE_DAY = Duration.ofDays(1).toMillis();

    private Clock clock;

    public LoginExpirationCookieDataBinder() {
        this(Clock.systemDefaultZone());
    }

    public LoginExpirationCookieDataBinder(final Clock clock) {
        this.clock = clock;
    }

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
                model.put(CookieName.EXPIRATION.toString(), expiration);
            }
        }
    }

    private String getExpirationValue(final String cookieValue) {
        String result = null;
        if (cookieValue.isEmpty()) {
            result = "ERROR";
        } else {
            try {
                long expiry = Long.parseLong(cookieValue) - this.clock.millis();
                if (expiry > 0) {
                    result = Long.toString(expiry / ONE_DAY);
                }
            } catch (NumberFormatException e) {
                log.error("failed to decode expiration date from cookie: value='{}'", cookieValue);
                result = "ERROR";
            }
        }
        return result;
    }
}
