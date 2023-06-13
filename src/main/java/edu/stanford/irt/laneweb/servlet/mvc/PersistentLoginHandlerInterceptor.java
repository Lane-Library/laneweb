package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import edu.stanford.irt.laneweb.servlet.CookieName;


public class PersistentLoginHandlerInterceptor implements HandlerInterceptor {

    private static final String UTF_8 = StandardCharsets.UTF_8.name();

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
            throws IOException {
        if (hasPersistentCookieSet(request)) {
            String encodedURL = getEncodedURL(request);
            Cookie cookie = new Cookie(CookieName.IS_PERSISTENT.toString(), null);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
            response.sendRedirect(request.getContextPath() + "/secure/persistentLogin.html?pl=true&url=" + encodedURL);
            return false;
        }
        return true;
    }

    private String getEncodedURL(final HttpServletRequest request) throws UnsupportedEncodingException {
        String url = request.getRequestURI();
        if (url.contains("/secure/login.html")) {
            url = request.getParameter("url"); 
            if(url == null) {
                url = "/index.html";
            }
        } else if (request.getQueryString() != null) {
            url = url + "?" + request.getQueryString();
        }
        return URLEncoder.encode(url, UTF_8);
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
