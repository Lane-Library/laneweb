package edu.stanford.irt.laneweb.servlet.mvc;

/**
 * This class will add three cookies the persistent-expired-date, persistent-preference and user. The user coolie will
 * have the sunetid, the userAgent and the expired date appended and encrypted. The persistent-preference have the
 * expired date minus 3 days only pl=true have to have the secure in the path but not the other But if pl=renew the
 * status of the user is looked up see it is active or not. Before to delete the cookie, we check if the
 * persistent-preference value is not equals to denied because if it is equals denied the persistent window will never
 * appear.
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

    public static final String PERSISTENT_COOKIE_ACTION = "pca=";

    public static final String PERSISTENT_LOGIN_PREFERENCE = "persistent-preference";

    public static final String PROXY_CREDENTIAL_LINK = "apps/proxy/credential";

    public static final String PROXY_HOST = "laneproxy.stanford.edu";

    private SunetIdCookieCodec codec;

    private SunetIdSource sunetIdSource;

    private void checkSunetIdAndSetCookies(final HttpServletRequest request, final HttpServletResponse response) {
        String sunetid = this.sunetIdSource.getSunetid(request);
        if (null != sunetid) {
            setCookies(request, response, sunetid);
        } else {
            resetCookies(request, response);
        }
    }

    @RequestMapping(value = "/secure/persistentLogin.html", params = { "pl=true" })
    public String createCookie(final String url, final HttpServletRequest request, final HttpServletResponse response) {
        checkSunetIdAndSetCookies(request, response);
        return setView(url, "true", request, response);
    }

    private boolean isProxyUrl(final String url) {
        if (url.contains(PROXY_CREDENTIAL_LINK) || url.contains(PROXY_HOST)) {
            return true;
        }
        return false;
    }

    @RequestMapping(value = { "/secure/persistentLogin.html", "/persistentLogin.html" }, params = { "pl=false" })
    public String removeCookieAndView(final String url, final HttpServletRequest request,
            final HttpServletResponse response) {
        removeCookies(request, response);
        return setView(url, "false", request, response);
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

    @RequestMapping(value = { "/secure/persistentLogin.html", "/persistentLogin.html" }, params = { "url", "pl=renew" })
    public String renewCookieAndRedirect(final String url, final HttpServletRequest request,
            final HttpServletResponse response) {
        Boolean isActiveSunetID = (Boolean) request.getSession().getAttribute(Model.IS_ACTIVE_SUNETID);
        if (null != isActiveSunetID && isActiveSunetID) {
            checkSunetIdAndSetCookies(request, response);
        } else {
            resetCookies(request, response);
        }
        return setView(url, "renew", request, response);
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
     * Append pca=true|false|renew to redirect URL when request is HTTPS. This will allow testing of IE security
     * zone problems. See case 74904 for more information.
     * 
     * @param request
     * @param url
     * @return url with PERSISTENT_COOKIE_ACTION parameter and action if appropriate
     */
    private String setPersistentCookieActionParam(final HttpServletRequest request, final String url,
            final String plAction) {
        if (!"https".equals(request.getScheme()) || isProxyUrl(url)) {
            return url;
        }
        String ampOrQuestion = (url.contains("?")) ? "&" : "?";
        StringBuffer sb = new StringBuffer();
        sb.append(url).append(ampOrQuestion).append(PERSISTENT_COOKIE_ACTION).append(plAction);
        return sb.toString();
    }

    @Autowired
    public void setSunetIdCookieCodec(final SunetIdCookieCodec codec) {
        this.codec = codec;
    }

    @Autowired
    public void setSunetIdSource(final SunetIdSource sunetIdSource) {
        this.sunetIdSource = sunetIdSource;
    }

    private String setView(final String url, final String plAction, final HttpServletRequest request,
            final HttpServletResponse response) {
        String redirectUrl = url;
        if (null == redirectUrl) {
            response.setCharacterEncoding("UTF-8");
            redirectUrl = "/myaccounts.html";
        }
        redirectUrl = setPersistentCookieActionParam(request, redirectUrl, plAction);
        return "redirect:".concat(redirectUrl);
    }
}
