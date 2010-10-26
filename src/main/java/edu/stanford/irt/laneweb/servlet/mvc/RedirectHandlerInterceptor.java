package edu.stanford.irt.laneweb.servlet.mvc;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import edu.stanford.irt.laneweb.servlet.redirect.RedirectProcessor;


public class RedirectHandlerInterceptor extends HandlerInterceptorAdapter {

    private static final String[][] BASE_MAPPINGS = new String[][] {
            { "/alainb", "file:/afs/ir.stanford.edu/users/a/l/alainb/laneweb" },
            { "/ceyates", "file:/afs/ir.stanford.edu/users/c/e/ceyates/laneweb" },
            { "/olgary", "file:/afs/ir.stanford.edu/users/o/l/olgary/laneweb" },
            { "/ryanmax", "file:/afs/ir.stanford.edu/users/r/y/ryanmax/laneweb" },
            { "/ajchrist", "file:/afs/ir.stanford.edu/users/a/j/ajchrist/laneweb" },
            { "/rzwies", "file:/afs/ir.stanford.edu/users/r/z/rzwies/laneweb" } };
    
    private RedirectProcessor redirectProcessor;
    
    private Map<String, String> baseMappings;
    
    public RedirectHandlerInterceptor() {
        this.baseMappings = new HashMap<String, String>(BASE_MAPPINGS.length);
        for (String[] element : BASE_MAPPINGS) {
            this.baseMappings.put(element[0], element[1]);
        }
    }

    
    public void setRedirectProcessor(RedirectProcessor redirectProcessor) {
        this.redirectProcessor = redirectProcessor;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String basePath = getBasePath(requestURI, request.getContextPath());
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
    
    private String getBasePath(final String requestURI, final String contextPath) {
        String servletPath = requestURI.substring(contextPath.length());
        if (servletPath.indexOf("/stage") ==0) {
            return contextPath + "/stage";
        }
        for (String key : this.baseMappings.keySet()) {
            if (servletPath.indexOf(key) == 0) {
                return contextPath + key;
            }
        }
        return contextPath;
    }
}
