package edu.stanford.irt.laneweb.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogoutServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String LOGOUT_URL = "/Shibboleth.sso/Logout?return=";

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        Cookie userCookie = new Cookie(CookieName.USER.toString(), null);
        userCookie.setPath("/");
        userCookie.setMaxAge(0);
        resp.addCookie(userCookie);
        Cookie cookie = new Cookie(CookieName.EXPIRATION.toString(), null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        resp.addCookie(cookie);
        Cookie ezproxyCookie = new Cookie(CookieName.EZPROXY.toString(), null);
        ezproxyCookie.setPath("/");
        ezproxyCookie.setMaxAge(0);
        resp.addCookie(ezproxyCookie);
        HttpSession session = req.getSession(false);
        if (null != session) {
            session.invalidate();
        }
        String referer = req.getHeader("referer");
        if(referer == null){
            referer = "/index.html";
        }
       
        resp.sendRedirect("https://"+ req.getLocalName() + LOGOUT_URL+referer);
    }
}
