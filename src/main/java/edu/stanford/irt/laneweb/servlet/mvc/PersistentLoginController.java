package edu.stanford.irt.laneweb.servlet.mvc;

import java.time.Clock;
/**
 * This class will add three cookies the persistent-expired-date and user. The user cookie will have the userid, name,
 * email , the userAgent and the expired date appended and encrypted. The persistent-expired-date cookie have the
 * expired date. So 3 days will be subtract from it to popup a extension window if the user is active and from stanford.
 */
import java.time.Duration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.stanford.irt.laneweb.codec.PersistentLoginToken;
import edu.stanford.irt.laneweb.codec.UserCookieCodec;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.CookieName;
import edu.stanford.irt.laneweb.servlet.binding.ActiveSunetidDataBinder;
import edu.stanford.irt.laneweb.servlet.binding.UserDataBinder;
import edu.stanford.irt.laneweb.user.User;

@Controller
public class PersistentLoginController {

    private static final long DURATION_MILLIS = Duration.ofDays(14).toMillis();

    // login duration is two weeks:
    private static final int DURATION_SECONDS = Math.toIntExact(Duration.ofDays(14).getSeconds());
    
    private static final String LANE_PROXY_URL = "https://login.laneproxy.stanford.edu/login";

    private ActiveSunetidDataBinder activeSunetidDataBinder;

    private Clock clock;

    private UserCookieCodec codec;

    private UserDataBinder userBinder;

    @Autowired
    public PersistentLoginController(final UserDataBinder userBinder,
            final ActiveSunetidDataBinder activeSunetidDataBinder, final UserCookieCodec codec) {
        this(userBinder, activeSunetidDataBinder, codec, Clock.systemDefaultZone());
    }

    public PersistentLoginController(final UserDataBinder userBinder,
            final ActiveSunetidDataBinder activeSunetidDataBinder, final UserCookieCodec codec, final Clock clock) {
        this.userBinder = userBinder;
        this.activeSunetidDataBinder = activeSunetidDataBinder;
        this.codec = codec;
        this.clock = clock;
    }

    @GetMapping(value = { "/secure/persistentLogin.html", "/persistentLogin.html" }, params = { "pl=false" })
    public String disablePersistentLogin(final RedirectAttributes redirectAttrs,
            @ModelAttribute(Model.USER) final User user, final String url, final HttpServletResponse response) {
        resetCookies(response);
        return getRedirectURL(url);
    }

    @GetMapping(value = "/secure/persistentLogin.html", params = { "pl=true" })
    public String enablePersistentLogin(final RedirectAttributes redirectAttrs,
            @ModelAttribute(Model.USER) final User user, final String url, final HttpServletRequest request,
            final HttpServletResponse response) {
        checkUserAndSetCookies(user, request, response);
        return getRedirectURL(url);
    }

    @GetMapping(value = { "/secure/persistentLogin.html", "/persistentLogin.html" }, params = { "url", "pl=renew" })
    public String renewPersistentLogin(final RedirectAttributes redirectAttrs,
            @ModelAttribute(Model.IS_ACTIVE_SUNETID) final Boolean isActiveSunetId,
            @ModelAttribute(Model.USER) final User user, final String url, final HttpServletRequest request,
            final HttpServletResponse response) {
        if (isActiveSunetId) {
            checkUserAndSetCookies(user, request, response);
        } else {
            resetCookies(response);
        }
        return getRedirectURL(url);
    }

    @ModelAttribute
    protected void bind(final HttpServletRequest request, final org.springframework.ui.Model model) {
        this.userBinder.bind(model.asMap(), request);
        this.activeSunetidDataBinder.bind(model.asMap(), request);
        if (!model.containsAttribute(Model.USER)) {
            model.addAttribute(Model.USER, null);
        }
    }

    private void checkUserAndSetCookies(final User user, final HttpServletRequest request,
            final HttpServletResponse response) {
        if (null != user) {
            setCookies(request, response, user);
        } else {
            resetCookies(response);
        }
    }
    
    private String getRedirectURL(final String url) {
        StringBuilder sb = new StringBuilder("redirect:");
        if (null == url) {
            sb.append("/index.html");
        }else if(!validateUrl(url)) {
            sb.append("/error.html");
        } else {
            sb.append(url);
        }
        return sb.toString();
    }

    private boolean validateUrl(String url) {
        if(url.startsWith(LANE_PROXY_URL)) {
            return true;
        }
        else {
            return false;
        }
    }
    
    private void resetCookies(final HttpServletResponse response) {
        Cookie cookie = new Cookie(CookieName.EXPIRATION.toString(), null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        cookie = new Cookie(CookieName.USER.toString(), null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    /**
     * create and set the lane-user cookie
     *
     * @param user
     * @param request
     * @param response
     */
    private void setCookies(final HttpServletRequest request, final HttpServletResponse response, final User user) {
        String userAgent = request.getHeader("User-Agent");
        if (null != userAgent && null != user) {
            PersistentLoginToken token = this.codec.createLoginToken(user, userAgent.hashCode());
            Cookie cookie = new Cookie(CookieName.USER.toString(), token.getEncryptedValue());
            cookie.setPath("/");
            cookie.setMaxAge(DURATION_SECONDS);
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
            long expires = this.clock.millis() + DURATION_MILLIS;
            cookie = new Cookie(CookieName.EXPIRATION.toString(), Long.toString(expires));
            cookie.setPath("/");
            cookie.setMaxAge(DURATION_SECONDS);
            response.addCookie(cookie);
        }
    }
}
