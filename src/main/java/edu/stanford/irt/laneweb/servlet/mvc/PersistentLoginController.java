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
import edu.stanford.irt.laneweb.servlet.binding.UserDataBinder;
import edu.stanford.irt.laneweb.user.User;

@Controller
public class PersistentLoginController {

    private static final long DURATION_MILLIS = Duration.ofDays(14).toMillis();

    // login duration is two weeks:
    private static final int DURATION_SECONDS = Math.toIntExact(Duration.ofDays(14).getSeconds());

    private UserCookieCodec codec;

    private UserDataBinder userBinder;

    private Clock clock;

    @Autowired
    public PersistentLoginController(final UserDataBinder userBinder, final UserCookieCodec codec) {
        this(userBinder, codec, Clock.systemDefaultZone());
    }

    public PersistentLoginController(final UserDataBinder userBinder, final UserCookieCodec codec, final Clock clock) {
        this.userBinder = userBinder;
        this.codec = codec;
        this.clock = clock;
    }

    @GetMapping(value = { "/secure/persistentLogin/myaccount.html", "/persistentLogin/myaccount.html" })
    public String myaccount(final RedirectAttributes redirectAttrs, @ModelAttribute(Model.USER) final User user,
            final String pl, final HttpServletRequest request, final HttpServletResponse response) {
        if ("true".equals(pl) && null != user) {
            setCookies(request, response, user);
        } else {
            resetCookies(response);
        }
        return "redirect:/myaccounts.html";
    }

    @GetMapping(value = "/secure/persistentLogin.html", params = { "pl=true" })
    public String enablePersistentLogin(final RedirectAttributes redirectAttrs,
            @ModelAttribute(Model.USER) final User user, final String url, final HttpServletRequest request,
            final HttpServletResponse response) {
        if (null != user) {
            setCookies(request, response, user);
            return getRedirectURL(url);
        } else {
            resetCookies(response);
            return "redirect:/error.html";
        }
    }

    @GetMapping(value = "/secure/login.html")
    public String login(final RedirectAttributes redirectAttrs, String url, User user) {
        if (null != user) {
            return getRedirectURL(url);
        } else {
            return "redirect:/error.html";
        }
    }

    @ModelAttribute
    protected void bind(final HttpServletRequest request, final org.springframework.ui.Model model) {
        this.userBinder.bind(model.asMap(), request);
        if (!model.containsAttribute(Model.USER)) {
            model.addAttribute(Model.USER, null);
        }
    }

    private String getRedirectURL(final String url) {
        StringBuilder sb = new StringBuilder("redirect:");
        if (null == url) {
            sb.append("/index.html");
        } else {
            sb.append(url);
        }
        return sb.toString();
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
