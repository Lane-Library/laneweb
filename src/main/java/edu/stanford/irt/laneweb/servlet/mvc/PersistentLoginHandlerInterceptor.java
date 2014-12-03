package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class PersistentLoginHandlerInterceptor extends HandlerInterceptorAdapter {

    private final static String IS_PERSISTENT_COOKIE_NAME = "isPersistent";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, java.lang.Object handler)
            throws UnsupportedEncodingException, IOException {
        String req = request.getContextPath() + request.getRequestURI();
        if (req.contains("/secure/login.html")) {
            req = request.getParameter("url");
        } else if (request.getQueryString() != null) {
            req = req + "?" + request.getQueryString();
        }
        if (hasPersistentCookieSet(request)) {
            Cookie cookie = new Cookie(IS_PERSISTENT_COOKIE_NAME, null);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
            response.sendRedirect(request.getContextPath() + "/secure/persistentLogin.html?pl=true&url="+ URLEncoder.encode(req, "UTF-8"));
            return false;
        } else if (request.getRequestURI().contains("/secure/login.html")) {
            response.sendRedirect(req);
            return false;
        }
        return true;
    }

    private boolean hasPersistentCookieSet(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (IS_PERSISTENT_COOKIE_NAME.equals(cookie.getName()) && "yes".equals(cookie.getValue())) {
                    return true;
                }
            }
        }
        return false;
    }
}
