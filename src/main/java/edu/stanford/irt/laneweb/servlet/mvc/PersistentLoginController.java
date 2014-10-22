package edu.stanford.irt.laneweb.servlet.mvc;

/**
 * This class will add three cookies the persistent-expired-date, persistent-preference and user. The user cookie will
 * have the userid, name, email , the userAgent and the expired date appended and encrypted. The persistent-preference
 * have the expired date minus 3 days only pl=true have to have the secure in the path but not the other But if pl=renew
 * the status of the user is looked up see it is active or not. Before to delete the cookie, we check if the
 * persistent-preference value is not equals to denied because if it is equals denied the persistent window will never
 * appear.
 */
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Calendar;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.codec.PersistentLoginToken;
import edu.stanford.irt.laneweb.codec.UserCookieCodec;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.binding.UserDataBinder;
import edu.stanford.irt.laneweb.user.User;
import edu.stanford.irt.laneweb.user.User.Status;

@Controller
public class PersistentLoginController {

    public static final String PERSISTENT_LOGIN_PREFERENCE = "persistent-preference";

    // grace period is three days
    private static final int GRACE_PERIOD = 3600 * 24 * 3;

    // login duration is two weeks:
    private static final int PERSISTENT_LOGIN_DURATION = GRACE_PERIOD + 3600 * 24 * 7 * 2;

    private static final String UTF8 = "UTF-8";

    private UserDataBinder binder;

    private UserCookieCodec codec;

    @Autowired
    public PersistentLoginController(final UserDataBinder binder, final UserCookieCodec codec) {
        this.binder = binder;
        this.codec = codec;
    }

    @RequestMapping(value = { "/secure/persistentLogin.html", "/persistentLogin.html" }, params = { "pl=false" })
    public String disablePersistentLogin(final RedirectAttributes redirectAttrs,
            @ModelAttribute(Model.USER) final User user,
            final String url,
            final HttpServletRequest request,
            final HttpServletResponse response) {
        resetCookies(request, response);
        return getRedirectURL(url);
    }

    @RequestMapping(value = "/secure/persistentLogin.html", params = { "pl=true" })
    public String enablePersistentLogin(final RedirectAttributes redirectAttrs,
            @ModelAttribute(Model.USER) final User user,
            final String url,
            final HttpServletRequest request,
            final HttpServletResponse response) {
        checkUserIdAndSetCookies(user, request, response);
        return getRedirectURL(url);
    }

    @RequestMapping(value = { "/secure/persistentLogin.html", "/persistentLogin.html" }, params = { "url", "pl=renew" })
    public String renewPersistentLogin(final RedirectAttributes redirectAttrs,
            @ModelAttribute(Model.USER) final User user,
            final String url,
            final HttpServletRequest request,
            final HttpServletResponse response) {
        if (user.isStanfordUser() && user.getStatus() == Status.ACTIVE) {
            checkUserIdAndSetCookies(user, request, response);
        } else {
            resetCookies(request, response);
        }
        return getRedirectURL(url);
    }

    @ModelAttribute
    protected void bind(final HttpServletRequest request, final org.springframework.ui.Model model) {
        this.binder.bind(model.asMap(), request);
        if (!model.containsAttribute(Model.USER)) {
            model.addAttribute(Model.USER, null);
        }
    }

    private void checkUserIdAndSetCookies(final User user, final HttpServletRequest request,
            final HttpServletResponse response) {
        if (null != user) {
            setCookies(request, response, user);
        } else {
            resetCookies(request, response);
        }
    }

    private String getRedirectURL(final String url) {
        StringBuilder sb = new StringBuilder("redirect:");
        if (null == url) {
            sb.append("/myaccounts.html");
        } else {
            try {
                sb.append(URLDecoder.decode(url, UTF8));
            } catch (UnsupportedEncodingException e) {
                throw new LanewebException("won't happen", e);
            }
        }
        return sb.toString();
    }

    private void resetCookies(final HttpServletRequest request, final HttpServletResponse response) {
        Cookie cookie = new Cookie(PERSISTENT_LOGIN_PREFERENCE, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        cookie = new Cookie(Model.PERSISTENT_LOGIN_EXPIRATION_DATE, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        cookie = new Cookie(UserCookieCodec.LANE_COOKIE_NAME, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
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
            Cookie cookie = new Cookie(UserCookieCodec.LANE_COOKIE_NAME, token.getEncryptedValue());
            cookie.setPath("/");
            cookie.setMaxAge(PERSISTENT_LOGIN_DURATION);
            response.addCookie(cookie);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.SECOND, PERSISTENT_LOGIN_DURATION);
            cookie = new Cookie(Model.PERSISTENT_LOGIN_EXPIRATION_DATE, String.valueOf(calendar.getTime().getTime()));
            cookie.setPath("/");
            cookie.setMaxAge(PERSISTENT_LOGIN_DURATION);
            response.addCookie(cookie);
            calendar.add(Calendar.SECOND, -GRACE_PERIOD);
            cookie = new Cookie(PERSISTENT_LOGIN_PREFERENCE, String.valueOf(calendar.getTime().getTime()));
            cookie.setPath("/");
            cookie.setMaxAge(PERSISTENT_LOGIN_DURATION);
            response.addCookie(cookie);
        }
    }
}
