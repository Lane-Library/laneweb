package edu.stanford.irt.laneweb.cookieConverter;

import java.time.Clock;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.codec.PersistentLoginToken;
import edu.stanford.irt.laneweb.codec.UserCookieCodec;
import edu.stanford.irt.laneweb.user.User;

@Service
public class SourceCookieHelper {
    
    private static final String oldUserCookieName = "lane_user";
    
    @Value("${edu.stanford.irt.laneweb.useridcookiecodec.oldkey}")
    String oldUserCookieKey;

    @Value("${edu.stanford.irt.laneweb.useridhasholdkey}")
    String oldUserIdHashKey;

    UserCookieCodec codec;

    private Clock clock;
    
    private static final Logger log = LoggerFactory.getLogger(SourceCookieHelper.class);


    @PostConstruct
    public void setOldUerCookieCodec() {
        this.codec = new UserCookieCodec(this.oldUserCookieKey);
        this.clock = Clock.systemDefaultZone();
    }
    
    
    
    public void resetOldUserCookies(final HttpServletResponse response) {
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
    
    public User getUserFromCookie(final Cookie cookie, final String userAgent) {
        User user = null;
        String value = cookie.getValue();
        if (userAgent != null && !value.isEmpty()) {
            try {
                PersistentLoginToken token = this.codec.restoreLoginToken(value, this.oldUserIdHashKey);
                if (token.isValidFor(this.clock.millis(), userAgent.hashCode())) {
                    user = token.getUser();
                }
            } catch (LanewebException e) {
                log.error("failed to decode userid from the OLD user cookie: value='{}'", value);
            }
        }
        return user;
    }
    
}
