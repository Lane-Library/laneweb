package edu.stanford.irt.laneweb.servlet.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.redirect.RedirectProcessor;

public class RedirectHandlerInterceptor extends HandlerInterceptorAdapter {

    private RedirectProcessor redirectProcessor;

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
            throws Exception {
        String requestURI = request.getRequestURI();
        String basePath = (String) request.getAttribute(Model.BASE_PATH);
        String sitemapURI = requestURI.substring(basePath.length());
        String redirectURL = this.redirectProcessor.getRedirectURL(sitemapURI, basePath, request.getQueryString());
        if (redirectURL != null) {
            response.sendRedirect(redirectURL);
            return false;
        }
        return true;
    }

    public void setRedirectProcessor(final RedirectProcessor redirectProcessor) {
        this.redirectProcessor = redirectProcessor;
    }
}
