package edu.stanford.irt.laneweb.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    private static final String LOGOUT_URL = "/Shibboleth.sso/Logout?return=";

    private static final long serialVersionUID = 1L;

    private static final String UTF_8 = StandardCharsets.UTF_8.name();

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        Cookie userCookie = new Cookie(CookieName.USER.toString(), null);
        userCookie.setPath("/");
        userCookie.setMaxAge(0);
        userCookie.setHttpOnly(true);
        resp.addCookie(userCookie);
        Cookie cookie = new Cookie(CookieName.EXPIRATION.toString(), null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        resp.addCookie(cookie);
        Cookie ezproxyCookie = new Cookie(CookieName.EZPROXY.toString(), null);
        ezproxyCookie.setPath("/");
        ezproxyCookie.setDomain("stanford.edu");
        ezproxyCookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        resp.addCookie(ezproxyCookie);
        HttpSession session = req.getSession(false);
        if (null != session) {
            session.invalidate();
        }
        String referer = req.getHeader("referer");
        if (referer == null || !referer.startsWith("/") || referer.indexOf("/secure/") > -1) {
            referer = "/index.html";
        }
        referer = URLEncoder.encode(referer, UTF_8);
        resp.sendRedirect("https://" + req.getLocalName() + LOGOUT_URL + referer);
    }
}
