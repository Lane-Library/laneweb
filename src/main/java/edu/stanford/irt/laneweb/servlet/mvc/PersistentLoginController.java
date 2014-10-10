package edu.stanford.irt.laneweb.servlet.mvc;

/**
 * This class will add three cookies the persistent-expired-date, persistent-preference and user. The user coolie will
 * have the userid, the userAgent and the expired date appended and encrypted. The persistent-preference have the
 * expired date minus 3 days only pl=true have to have the secure in the path but not the other But if pl=renew the
 * status of the user is looked up see it is active or not. Before to delete the cookie, we check if the
 * persistent-preference value is not equals to denied because if it is equals denied the persistent window will never
 * appear.
 */
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import edu.stanford.irt.laneweb.codec.PersistentLoginToken;
import edu.stanford.irt.laneweb.codec.UserCookieCodec;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.binding.UserDataBinder;
import edu.stanford.irt.laneweb.user.User;

@Controller
public class PersistentLoginController {

    public static final String PERSISTENT_LOGIN_PREFERENCE = "persistent-preference";

    private static final String UTF8 = "UTF-8";

    private UserCookieCodec codec;

    private UserDataBinder binder;
    
    @Autowired
    public PersistentLoginController(UserDataBinder binder, UserCookieCodec codec) {
        this.binder = binder;
        this.codec = codec;
    }

    @RequestMapping(value = "/secure/persistentLogin.html", params = { "pl=true" })
    public View createCookie(@ModelAttribute(Model.USER) User user, final String url, final HttpServletRequest request, final HttpServletResponse response)
            throws UnsupportedEncodingException {
        checkUserIdAndSetCookies(user, request, response);
        return setView(url, response);
    }

    @RequestMapping(value = { "/secure/persistentLogin.html", "/persistentLogin.html" }, params = { "pl=false" })
    public View removeCookieAndView(@ModelAttribute(Model.USER) User user, final String url, final HttpServletRequest request,
            final HttpServletResponse response) throws UnsupportedEncodingException {
        removeCookies(request, response);
        return setView(url, response);
    }

    private void checkUserIdAndSetCookies(final User user, final HttpServletRequest request, final HttpServletResponse response) { 
        if (null != user) {
            setCookies(request, response, user);
        } else {
            resetCookies(request, response);
        }
    }

    private void removeCookies(final HttpServletRequest request, final HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie webCookie : cookies) {
                if (PERSISTENT_LOGIN_PREFERENCE.equals(webCookie.getName())) {
                    String cookieValue = webCookie.getValue();
                    if (!"denied".equals(cookieValue)) {
                        webCookie.setPath("/");
                        webCookie.setMaxAge(0);
                        response.addCookie(webCookie);
                    }
                    break;
                }
            }
        }
        Cookie cookie = new Cookie(Model.PERSISTENT_LOGIN_EXPIRATION_DATE, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        cookie = new Cookie(UserCookieCodec.LANE_COOKIE_NAME, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    private void resetCookies(final HttpServletRequest request, final HttpServletResponse response) {
        Cookie cookie = new Cookie(PERSISTENT_LOGIN_PREFERENCE, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        removeCookies(request, response);
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
            int twoWeeks = 3600 * 24 * 7 * 2;
            // gracePeriod is three days
            int gracePeriod = 3600 * 24 * 3;
            PersistentLoginToken token = this.codec.createLoginToken(user, userAgent.hashCode());
            Cookie cookie = new Cookie(UserCookieCodec.LANE_COOKIE_NAME, token.getEncryptedValue());
            cookie.setPath("/");
            // cookie is available for 2 weeks
            cookie.setMaxAge(twoWeeks);
            response.addCookie(cookie);
            GregorianCalendar gc = new GregorianCalendar();
            gc.add(Calendar.SECOND, twoWeeks);
            cookie = new Cookie(Model.PERSISTENT_LOGIN_EXPIRATION_DATE, String.valueOf(gc.getTime().getTime()));
            cookie.setPath("/");
            // cookie is available for 2 weeks
            cookie.setMaxAge(twoWeeks);
            response.addCookie(cookie);
            gc.add(Calendar.SECOND, -gracePeriod);
            cookie = new Cookie(PERSISTENT_LOGIN_PREFERENCE, String.valueOf(gc.getTime().getTime()));
            cookie.setPath("/");
            // cookie is available for 2 weeks
            cookie.setMaxAge(twoWeeks);
            response.addCookie(cookie);
        }
    }

    private View setView(final String url, final HttpServletResponse response) throws UnsupportedEncodingException {
        RedirectView view = null;
        if (null == url) {
            response.setCharacterEncoding(UTF8);
            view = new RedirectView("/myaccounts.html", true, true);
        } else {
            view = new RedirectView(URLDecoder.decode(url, UTF8), true, true);
        }
        view.setExpandUriTemplateVariables(false);
        return view;
    }

    @ModelAttribute
    protected void bind(final HttpServletRequest request, final org.springframework.ui.Model model) {
        this.binder.bind(model.asMap(), request);
        if (!model.containsAttribute(Model.USER)) {
            model.addAttribute(Model.USER, null);
        }
    }
}
