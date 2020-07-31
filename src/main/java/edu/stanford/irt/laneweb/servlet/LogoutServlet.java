package edu.stanford.irt.laneweb.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // TODO: can we destroy the user session at idp-proxy.med?
    // maybe with https://idp-proxy.med.stanford.edu/auth/realms/test-all/protocol/openid-connect/logout
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
        resp.sendRedirect("https://" + req.getLocalName() + referer);
    }
}
