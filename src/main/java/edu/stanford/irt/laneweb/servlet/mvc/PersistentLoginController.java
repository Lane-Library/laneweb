package edu.stanford.irt.laneweb.servlet.mvc;

/**
 * This class will add three cookies the persistent-expired-date,
 * persistent-preference and user. The user coolie will have the sunetid, the
 * userAgent and the expired date appended and encrypted. The
 * persistent-preference have the expired date minus 3 days only pl=true have to
 * have the secure in the path but not the other But if pl=renew the status of
 * the user is looked up see it is active or not. Before to delete the cookie,
 * we check if the persistent-preference value is not equals to denied because
 * if it is equals denied the persistent window will never appear.
 */
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.PersistentLoginToken;
import edu.stanford.irt.laneweb.servlet.SunetIdCookieCodec;
import edu.stanford.irt.laneweb.servlet.SunetIdSource;

@Controller
public class PersistentLoginController {

    public static final String PERSISTENT_LOGIN_PREFERENCE = "persistent-preference";

    private SunetIdCookieCodec codec;

    private SunetIdSource sunetIdSource;

    @RequestMapping(value =  "/secure/persistentLogin.html" , params = { "pl=true" })
    public String createCookie(final String url, final HttpServletRequest request, final HttpServletResponse response) {
        checkSunetIdAndSetCookies(request, response);
        return setView(url, request, response);
    }

    @RequestMapping(value = {"/secure/persistentLogin.html","/persistentLogin.html"}, params = { "pl=false" })
    public String removeCookieAndView(final String url, final HttpServletRequest request, final HttpServletResponse response) {
        removeCookies(request, response);
        return setView(url, request, response);
    }

    @RequestMapping(value = {"/secure/persistentLogin.html", "/persistentLogin.html"}, params = { "url", "pl=renew" })
    public String renewCookieAndRedirect(final String url, final HttpServletRequest request, final HttpServletResponse response) {
        Boolean isActiveSunetID = (Boolean) request.getSession().getAttribute(Model.IS_ACTIVE_SUNETID);
        if (null != isActiveSunetID && isActiveSunetID) {
            checkSunetIdAndSetCookies(request, response);
        } else {
            resetCookies(request, response);
        }
        return "redirect:".concat(url);
    }





    @Autowired
    public void setSunetIdCookieCodec(final SunetIdCookieCodec codec) {
        this.codec = codec;
    }
    
    @Autowired
    public void setSunetIdSource(final SunetIdSource sunetIdSource) {
        this.sunetIdSource = sunetIdSource;
    }

    private void checkSunetIdAndSetCookies(final HttpServletRequest request, final HttpServletResponse response) {
        String sunetid = this.sunetIdSource.getSunetid(request);
        if (null != sunetid) {
            setCookies(request, response, sunetid);
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
        cookie = new Cookie(SunetIdCookieCodec.LANE_COOKIE_NAME, null);
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
     * @param sunetid
     * @param request
     * @param response
     */
    private void setCookies(final HttpServletRequest request, final HttpServletResponse response, final String sunetid) {
        String userAgent = request.getHeader("User-Agent");
        if (null != userAgent && null != sunetid) {
            int twoWeeks = 3600 * 24 * 7 * 2;
            int gracePeriod = 3600 * 24 * 3; // three days
            PersistentLoginToken token = this.codec.createLoginToken(sunetid, userAgent.hashCode());
            Cookie cookie = new Cookie(SunetIdCookieCodec.LANE_COOKIE_NAME, token.getEncryptedValue());
            cookie.setPath("/");
            cookie.setMaxAge(twoWeeks); // cookie is available for 2 // weeks
            response.addCookie(cookie);
            GregorianCalendar gc = new GregorianCalendar();
            gc.add(Calendar.SECOND, twoWeeks);
            cookie = new Cookie(Model.PERSISTENT_LOGIN_EXPIRATION_DATE, String.valueOf(gc.getTime().getTime()));
            cookie.setPath("/");
            cookie.setMaxAge(twoWeeks); // cookie is available for 2 // weeks
            response.addCookie(cookie);
            gc.add(Calendar.SECOND, -gracePeriod);
            cookie = new Cookie(PERSISTENT_LOGIN_PREFERENCE, String.valueOf(gc.getTime().getTime()));
            cookie.setPath("/");
            cookie.setMaxAge(twoWeeks); // cookie is available for 2 // weeks
            response.addCookie(cookie);
        }
    }


    
    /**
     * set the lane-user cookie max age to zero.
     * 
     * @param response
     */
    private String setView(final String url, final HttpServletRequest request, final HttpServletResponse response) {
        this.sunetIdSource.getSunetid(request);
        if (null == url) {
            response.setCharacterEncoding("UTF-8");
            return "redirect:/myaccounts.html";
        } else {
            return "redirect:".concat(url);
        }
    }
}
