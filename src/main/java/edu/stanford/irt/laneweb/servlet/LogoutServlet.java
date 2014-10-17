package edu.stanford.irt.laneweb.servlet;

import static edu.stanford.irt.laneweb.servlet.LanewebCookie.EZPROXY;
import static edu.stanford.irt.laneweb.servlet.LanewebCookie.LOGIN_EXPIRATION;
import static edu.stanford.irt.laneweb.servlet.LanewebCookie.LOGIN_PREFERENCE;
import static edu.stanford.irt.laneweb.servlet.LanewebCookie.USER;
import static edu.stanford.irt.laneweb.servlet.LanewebCookie.WEBAUTH;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogoutServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String WEBAUTH_LOGOUT_URL = "https://weblogin.stanford.edu/logout";

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        Cookie userCookie = new Cookie(USER.getName(), null);
        userCookie.setPath("/");
        userCookie.setMaxAge(0);
        resp.addCookie(userCookie);
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie webCookie : cookies) {
                if (LOGIN_PREFERENCE.getName().equals(webCookie.getName())) {
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
        Cookie cookie = new Cookie(LOGIN_EXPIRATION.getName(), null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        resp.addCookie(cookie);
        Cookie webAuthCookie = new Cookie(WEBAUTH.getName(), null);
        webAuthCookie.setPath("/");
        webAuthCookie.setMaxAge(0);
        resp.addCookie(webAuthCookie);
        Cookie ezproxyCookie = new Cookie(EZPROXY.getName(), null);
        ezproxyCookie.setPath("/");
        ezproxyCookie.setMaxAge(0);
        resp.addCookie(ezproxyCookie);
        HttpSession session = req.getSession(false);
        if (null != session) {
            session.invalidate();
        }
        resp.sendRedirect(WEBAUTH_LOGOUT_URL);
    }
}
