package edu.stanford.irt.laneweb.servlet.mvc;


import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import edu.stanford.irt.laneweb.servlet.redirect.RedirectProcessor;

public class RedirectHandlerInterceptor implements HandlerInterceptor {

    private RedirectProcessor redirectProcessor;

    public RedirectHandlerInterceptor(final RedirectProcessor redirectProcessor) {
        this.redirectProcessor = redirectProcessor;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
            throws IOException {
        String requestURI = request.getRequestURI();
        String basePath = request.getContextPath();
        String uri = requestURI.substring(basePath.length());
        String redirectURL = this.redirectProcessor.getRedirectURL(uri, basePath, request.getQueryString());
        if (redirectURL != null) {
            response.sendRedirect(redirectURL);
            return false;
        }
        return true;
    }
}
