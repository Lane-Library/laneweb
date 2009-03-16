package edu.stanford.irt.laneweb;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.Action;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;

import edu.stanford.irt.laneweb.user.User;
import edu.stanford.irt.laneweb.user.UserDao;

public class PersistentLoginAction implements Action {

    private Cryptor cryptor = null;

    private UserDao userDao = null;

    @SuppressWarnings("unchecked")
    public Map act(final Redirector redirector, final SourceResolver resolver, final Map objectModel, final String source, final Parameters params)
            throws Exception {
        HttpServletRequest request = ObjectModelHelper.getRequest(objectModel);
        HttpServletResponse response = ObjectModelHelper.getResponse(objectModel);
        String persistentLogin = request.getParameter("pl");
        String removePersistentLogin = request.getParameter("remove-pl");
        User user = this.userDao.createOrUpdateUser(request);
        String sunetid = user.getSunetId();
        if ((sunetid == null) && !"logout".equals(persistentLogin)) {
            String secureUrl = request.getContextPath().concat("/secure/persistentlogin.html");
            if (null != request.getQueryString()) {
                secureUrl = secureUrl.concat("?").concat(request.getQueryString());
            }
            redirector.globalRedirect(false, secureUrl);
            return null;
        }
        if ("true".equals(persistentLogin)) {
            createLaneCookie(sunetid, request, response);
        } else if ("true".equals(removePersistentLogin)) {
            deleteSunetIdCookie(response);
        } else if ("logout".equals(persistentLogin)) {
            deleteSunetIdCookie(response);
            deleteWebauthCookie(response);
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            redirector.globalRedirect(false, "https://weblogin.stanford.edu/logout");
        }
        return null;
    }

    public void setCryptor(final Cryptor cryptor) {
        if (null == cryptor) {
            throw new IllegalArgumentException("null cryptor");
        }
        this.cryptor = cryptor;
    }

    public void setUserDao(final UserDao userDao) {
        if (null == userDao) {
            throw new IllegalArgumentException("null userDao");
        }
        this.userDao = userDao;
    }

    private void createLaneCookie(final String sunetid, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        if (sunetid == null) {
            throw new RuntimeException("sunetId is null");
        }
        if (this.cryptor == null) {
            throw new RuntimeException("cryptor is null");
        }
        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null) {
            throw new RuntimeException("userAgent is null");
        }
        StringBuffer cookieValue = new StringBuffer();
        cookieValue.append(sunetid);
        cookieValue.append(LanewebConstants.COOKIE_VALUE_SEPARATOR);
        cookieValue.append(String.valueOf(new Date().getTime()));
        cookieValue.append(LanewebConstants.COOKIE_VALUE_SEPARATOR);
        userAgent = String.valueOf(userAgent.hashCode());
        cookieValue.append(userAgent);
        Cookie laneCookie = new Cookie(LanewebConstants.LANE_COOKIE_NAME, this.cryptor.encrypt(cookieValue.toString()));
        laneCookie.setPath("/");
        laneCookie.setMaxAge(3600 * 24 * 7 * 2); // cookie is available for
        response.addCookie(laneCookie);
    }

    private void deleteSunetIdCookie(final HttpServletResponse response) {
        // FIXME: Remove later after 2 weeks on prod
        Cookie sunetIdCookie = new Cookie(LanewebConstants.USER_COOKIE_NAME, null);
        sunetIdCookie.setPath("/");
        sunetIdCookie.setMaxAge(0);
        Cookie dateCookie = new Cookie(LanewebConstants.DATE_COOKIE_NAME, null);
        dateCookie.setPath("/");
        dateCookie.setMaxAge(0);
        Cookie securityCookie = new Cookie(LanewebConstants.SECURITY_COOKIE_NAME, null);
        securityCookie.setPath("/");
        securityCookie.setMaxAge(0);
        response.addCookie(sunetIdCookie);
        response.addCookie(dateCookie);
        response.addCookie(securityCookie);
        // END of FIXME: Remove later after 2 weeks on prod
        Cookie laneCookie = new Cookie(LanewebConstants.LANE_COOKIE_NAME, null);
        securityCookie.setPath("/");
        securityCookie.setMaxAge(0);
        response.addCookie(laneCookie);
    }

    private void deleteWebauthCookie(final HttpServletResponse response) {
        Cookie webauth = new Cookie("webauth_at", null);
        webauth.setMaxAge(0);
        webauth.setPath("/");
        response.addCookie(webauth);
    }
}
