package edu.stanford.irt.laneweb.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.stanford.irt.laneweb.codec.UserIdCookieCodec;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.mvc.PersistentLoginController;

public class LogoutServlet extends HttpServlet {

    private static final String EZPROXY_COOKIE_NAME = "ezproxy";

    private static final long serialVersionUID = 1L;

    private static final String LOGOUT_URL = "/Shibboleth.sso/Logout?return=/logout.html";

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        Cookie userCookie = new Cookie(UserIdCookieCodec.LANE_COOKIE_NAME, null);
        userCookie.setPath("/");
        userCookie.setMaxAge(0);
        resp.addCookie(userCookie);
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie webCookie : cookies) {
                if (PersistentLoginController.PERSISTENT_LOGIN_PREFERENCE.equals(webCookie.getName())) {
                    String cookieValue = webCookie.getValue();
                    // don't want to overwrite denied cookie
                    if (!"denied".equals(cookieValue)) {
                        webCookie.setPath("/");
                        webCookie.setMaxAge(0);
                        resp.addCookie(webCookie);
                    }
                    break;
                }
            }
        }
        Cookie cookie = new Cookie(Model.PERSISTENT_LOGIN_EXPIRATION_DATE, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        resp.addCookie(cookie);
        Cookie ezproxyCookie = new Cookie(EZPROXY_COOKIE_NAME, null);
        ezproxyCookie.setPath("/");
        ezproxyCookie.setMaxAge(0);
        resp.addCookie(ezproxyCookie);
        HttpSession session = req.getSession(false);
        if (null != session) {
            session.invalidate();
        }
        resp.sendRedirect("https://"+ req.getLocalName() + LOGOUT_URL);
    }
}
