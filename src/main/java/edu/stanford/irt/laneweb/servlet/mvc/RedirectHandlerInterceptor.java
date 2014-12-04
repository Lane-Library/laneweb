package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import edu.stanford.irt.laneweb.servlet.redirect.RedirectProcessor;

public class RedirectHandlerInterceptor extends HandlerInterceptorAdapter {

    private RedirectProcessor redirectProcessor;

    public RedirectHandlerInterceptor(final RedirectProcessor redirectProcessor) {
        this.redirectProcessor = redirectProcessor;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
            throws IOException {
        String servletPath = request.getServletPath();
        String basePath = request.getContextPath();
        String redirectURL = this.redirectProcessor.getRedirectURL(servletPath, basePath, request.getQueryString());
        if (redirectURL != null) {
            response.sendRedirect(redirectURL);
            return false;
        }
        return true;
    }
}
