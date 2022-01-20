package edu.stanford.irt.laneweb.cookieConverter;

import java.time.Duration;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.codec.UserCookieCodec;
import edu.stanford.irt.laneweb.servlet.CookieName;


@Service
public class TargetCookieHelper extends CookieHelper{

    @Autowired
    UserCookieCodec codec;
    
    @Value("${edu.stanford.irt.laneweb.useridcookiecodec.key}") 
    String cookieKey;
    
    @PostConstruct
    public void setOldUerCookieCodec() {
        super.setCookieHelper(this.cookieKey);       
    }

    // login duration is two weeks:
    private static final int DURATION_SECONDS = Math.toIntExact(Duration.ofDays(28).getSeconds());

    private static final String SAME_SITE = "; SameSite=Lax";

    private static final String COOKIE_HEADERS = "Set-Cookie";

    // Set the cookie value with the new name
    public void setCookies(final HttpServletRequest request, final HttpServletResponse response, String  decryptedValue) {
        String userAgent = request.getHeader("User-Agent");
        if (null != userAgent && decryptedValue != null) {
            try {
                String encryptedValue = super.encrypt(decryptedValue);
                this.addCookie(CookieName.USER.toString(), encryptedValue, response);
                addSameSiteToCookies(response);
            } catch (NumberFormatException e) {
                throw new LanewebException("invalid encryptedValue", e);
            }
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
    
}
