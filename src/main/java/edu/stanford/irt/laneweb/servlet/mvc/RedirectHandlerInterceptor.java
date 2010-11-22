package edu.stanford.irt.laneweb.servlet.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import edu.stanford.irt.laneweb.Model;
import edu.stanford.irt.laneweb.servlet.redirect.RedirectProcessor;


public class RedirectHandlerInterceptor extends HandlerInterceptorAdapter {
    
    private RedirectProcessor redirectProcessor;

    
    public void setRedirectProcessor(RedirectProcessor redirectProcessor) {
        this.redirectProcessor = redirectProcessor;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String basePath = (String) request.getAttribute(Model.BASE_PATH);
        String sitemapURI = requestURI.substring(basePath.length());
        //only .html and .xml or ending in / potentially get redirects.
        if (isRedirectable(sitemapURI)) {
            String redirectURL = this.redirectProcessor.getRedirectURL(sitemapURI, basePath, request.getQueryString());
            if (redirectURL != null) {
                response.sendRedirect(redirectURL);
                return false;
            }
        }
        return true;
    }

    private boolean isRedirectable(String sitemapURI) {
        return sitemapURI.indexOf(".html") > 0 
            || sitemapURI.indexOf(".xml") > 0 
            || sitemapURI.endsWith("/")
            || sitemapURI.indexOf("page2rss") > 0
            || sitemapURI.indexOf("/lksc-print") == 0;
    }
}
