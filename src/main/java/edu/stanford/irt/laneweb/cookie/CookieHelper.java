package edu.stanford.irt.laneweb.cookie;

import java.time.Duration;
import java.util.Collection;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import edu.stanford.irt.laneweb.codec.PersistentLoginToken;
import edu.stanford.irt.laneweb.codec.UserCookieCodec;
import edu.stanford.irt.laneweb.servlet.CookieName;
import edu.stanford.irt.laneweb.user.User;


@Service
public class CookieHelper {

    @Autowired
    UserCookieCodec codec;
    
    @Value("edu.stanford.irt.laneweb.useridhashkey")
    String  userIdHashKey; 

    private static final String oldUserCookieName = "lane-user";

    

    // login duration is two weeks:
    private static final int DURATION_SECONDS = Math.toIntExact(Duration.ofDays(28).getSeconds());

    private static final String SAME_SITE = "; SameSite=Lax";

    private static final String COOKIE_HEADERS = "Set-Cookie";

    // Set the cookie value with the new name
    public void setCookies(final HttpServletRequest request, final HttpServletResponse response, User user) {
        String userAgent = request.getHeader("User-Agent");
        if (null != userAgent && null != user) {
            user = new User( user.getId(), user.getName()  , user.getEmail(), userIdHashKey);
            PersistentLoginToken token = this.codec.createLoginToken(user, userAgent.hashCode());
            this.addCookie(CookieName.USER.toString(), token.getEncryptedValue(), response);
            addSameSiteToCookies(response);
        }
    }

    private void addCookie(final String name, final String value, final HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(DURATION_SECONDS);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
    }

    // Because there is no method to add SameSite=strict with javax.servlet.http.Cookie
    private void addSameSiteToCookies(HttpServletResponse response) {
        Collection<String> headers = response.getHeaders(COOKIE_HEADERS);
        boolean cookieReseted = false;
        for (String header : headers) {
            if (header.startsWith(CookieName.EXPIRATION.toString()) || header.startsWith(CookieName.USER.toString())) {
                header = header.concat(SAME_SITE);
            }
            if (!cookieReseted) {
                response.setHeader(COOKIE_HEADERS, header);
                cookieReseted = true;
            } else {
                response.addHeader(COOKIE_HEADERS, header);
            }
        }
    }

    public void resetOldUserCookies(final HttpServletResponse response) {
        Cookie cookie = new Cookie(oldUserCookieName, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    public boolean isOldUserCookiesPresent(final HttpServletRequest request) {
        Cookie userCookie = null;
        Cookie[] cookies = request.getCookies();
        for (int i = 0; cookies != null && i < cookies.length && userCookie == null; i++) {
            if (oldUserCookieName.equals(cookies[i].getName())) {
                return true;
            }
        }
        return false;
    }
}
