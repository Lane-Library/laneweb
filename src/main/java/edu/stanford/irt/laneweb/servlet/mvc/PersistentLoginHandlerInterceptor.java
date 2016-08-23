package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import edu.stanford.irt.laneweb.servlet.CookieName;

public class PersistentLoginHandlerInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
            throws IOException {
        String encodedURL = getEncodedURL(request);
        if (hasPersistentCookieSet(request)) {
            Cookie cookie = new Cookie(CookieName.IS_PERSISTENT.toString(), null);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
            response.sendRedirect(request.getContextPath() + "/secure/persistentLogin.html?pl=true&url=" + encodedURL);
            return false;
        } else if (request.getRequestURI().contains("/secure/login.html")) {
            response.sendRedirect(request.getContextPath() + "/secure/persistentLogin.html?pl=false&url=" + encodedURL);
            return false;
        }
        return true;
    }

    private String getEncodedURL(final HttpServletRequest request) throws UnsupportedEncodingException {
        String requestURI = request.getRequestURI();
        String url = null;
        if (requestURI.contains("/secure/login.html")) {
            url = request.getParameter("url");
        } else if (request.getQueryString() != null) {
            url = requestURI + "?" + request.getQueryString();
        }
        if (url == null) {
            url = "/index.html";
        }
        return URLEncoder.encode(url, "UTF-8");
    }

    private boolean hasPersistentCookieSet(final HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (CookieName.IS_PERSISTENT.toString().equals(cookie.getName()) && "yes".equals(cookie.getValue())) {
                    return true;
                }
            }
        }
        return false;
    }
}
