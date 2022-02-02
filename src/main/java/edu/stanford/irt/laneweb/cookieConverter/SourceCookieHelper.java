package edu.stanford.irt.laneweb.cookieConverter;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import edu.stanford.irt.laneweb.LanewebException;

@Service
public class SourceCookieHelper extends CookieHelper {

    private static final String oldUserCookieName = "lane_user";

    @Value("${edu.stanford.irt.laneweb.useridcookiecodec.oldkey}")
    String oldUserCookieKey;

    private static final Logger log = LoggerFactory.getLogger(SourceCookieHelper.class);

    @PostConstruct
    public void setOldUerCookieCodec() {
        super.setCookieHelper(oldUserCookieKey);       
    }

    public void deleteOldUserCookie(final HttpServletResponse response) {
        Cookie cookie = new Cookie(oldUserCookieName, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    public Cookie getOldUserCookies(final HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (int i = 0; cookies != null && i < cookies.length; i++) {
            if (oldUserCookieName.equals(cookies[i].getName())) {
                return cookies[i];
            }
        }
        return null;
    }

    public String getCookieValue(final Cookie cookie, final String userAgent) {
        String value = cookie.getValue();
        if (userAgent != null && !value.isEmpty()) {
            try {
                return super.decrypt(value);
            } catch (LanewebException e) {
                log.error("failed to decode userid from the OLD user cookie: value='{}'", value);
            }
        }
        return null;
    }
}
