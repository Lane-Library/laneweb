package edu.stanford.irt.laneweb.cookie;

import java.io.IOException;
import java.time.Clock;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.codec.PersistentLoginToken;
import edu.stanford.irt.laneweb.codec.UserCookieCodec;
import edu.stanford.irt.laneweb.servlet.AbstractLanewebFilter;
import edu.stanford.irt.laneweb.user.User;

@WebFilter("/*")
public class UserCookieUpdatorFilter extends AbstractLanewebFilter {

    private static final Logger log = LoggerFactory.getLogger(UserCookieUpdatorFilter.class);

    @Autowired
    CookieHelper cookieHelper;

    @Value("${edu.stanford.irt.laneweb.useridcookiecodec.oldkey}")
    String oldUserCookieKey;

    @Value("${edu.stanford.irt.laneweb.useridhasholdkey}")
    String oldUserIdHashKey;

    UserCookieCodec codec;

    private Clock clock;

    @PostConstruct
    public void setOldUerCookieCodec() {
        this.codec = new UserCookieCodec(this.oldUserCookieKey);
        this.clock = Clock.systemDefaultZone();
    }

    @Override
    protected void internalDoFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (this.cookieHelper != null) {
            Cookie oldCookie = this.cookieHelper.getOldUserCookies(request);
            if (oldCookie != null) {
                User user = this.getUserFromCookie(oldCookie, request.getHeader("User-Agent"));
                this.cookieHelper.setCookies(request, response, user);
                this.cookieHelper.resetOldUserCookies(response);
                String url = getUrl( request);
                response.sendRedirect(url);
            }
        }
        chain.doFilter(request, response);
    }

    private User getUserFromCookie(final Cookie cookie, final String userAgent) {
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

    private String getUrl(HttpServletRequest req) {
        String queryString = req.getQueryString();
        return queryString == null ? req.getRequestURL().toString()
                : req.getRequestURL().append('?').append(queryString).toString();
    }
}
