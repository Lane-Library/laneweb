package edu.stanford.irt.laneweb.servlet.mvc;

/**
 * This class will add three cookies the persistent-expired-date and user. The user cookie will have the userid, name,
 * email , the userAgent and the expired date appended and encrypted. The persistent-expired-date cookie have the
 * expired date. So 3 days will be subtract from it to popup a extension window if the user is active and from stanford.
 */
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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
import edu.stanford.irt.laneweb.servlet.CookieName;
import edu.stanford.irt.laneweb.servlet.binding.UserDataBinder;
import edu.stanford.irt.laneweb.user.LDAPDataAccess;
import edu.stanford.irt.laneweb.user.User;

@Controller
public class PersistentLoginController {

    // login duration is two weeks:
    private static final int PERSISTENT_LOGIN_DURATION = 3600 * 24 * 7 * 2;

    private static final String UTF_8 = StandardCharsets.UTF_8.name();

    private UserCookieCodec codec;

    private LDAPDataAccess ldap;

    private UserDataBinder userBinder;

    @Autowired
    public PersistentLoginController(final UserDataBinder userBinder, final LDAPDataAccess ldap,
            final UserCookieCodec codec) {
        this.userBinder = userBinder;
        this.ldap = ldap;
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
        checkUserAndSetCookies(user, request, response);
        return getRedirectURL(url);
    }

    @RequestMapping(value = { "/secure/persistentLogin.html", "/persistentLogin.html" }, params = { "url", "pl=renew" })
    public String renewPersistentLogin(final RedirectAttributes redirectAttrs,
            @ModelAttribute(Model.USER) final User user,
            final String url,
            final HttpServletRequest request,
            final HttpServletResponse response) {
        if (isRenewable(user)) {
            checkUserAndSetCookies(user, request, response);
        } else {
            resetCookies(request, response);
        }
        return getRedirectURL(url);
    }

    @ModelAttribute
    protected void bind(final HttpServletRequest request, final org.springframework.ui.Model model) {
        this.userBinder.bind(model.asMap(), request);
        if (!model.containsAttribute(Model.USER)) {
            model.addAttribute(Model.USER, null);
        }
    }

    private void checkUserAndSetCookies(final User user, final HttpServletRequest request,
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
                sb.append(URLDecoder.decode(url, UTF_8));
            } catch (UnsupportedEncodingException e) {
                throw new LanewebException("won't happen", e);
            }
        }
        return sb.toString();
    }

    private boolean isRenewable(final User user) {
        boolean renewable = false;
        if (user != null && user.isStanfordUser()) {
            String userid = user.getId();
            String sunetid = userid.substring(0, userid.indexOf('@'));
            renewable = this.ldap.getLdapDataForSunetid(sunetid).isActive();
        }
        return renewable;
    }

    private void resetCookies(final HttpServletRequest request, final HttpServletResponse response) {
        Cookie cookie = new Cookie(CookieName.EXPIRATION.toString(), null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        cookie = new Cookie(CookieName.USER.toString(), null);
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
            Cookie cookie = new Cookie(CookieName.USER.toString(), token.getEncryptedValue());
            cookie.setPath("/");
            cookie.setMaxAge(PERSISTENT_LOGIN_DURATION);
            response.addCookie(cookie);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.SECOND, PERSISTENT_LOGIN_DURATION);
            cookie = new Cookie(CookieName.EXPIRATION.toString(), String.valueOf(calendar.getTime().getTime()));
            cookie.setPath("/");
            cookie.setMaxAge(PERSISTENT_LOGIN_DURATION);
            response.addCookie(cookie);
        }
    }
}
