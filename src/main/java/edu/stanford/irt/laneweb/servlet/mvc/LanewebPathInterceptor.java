package edu.stanford.irt.laneweb.servlet.mvc;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import edu.stanford.irt.laneweb.Model;


public class LanewebPathInterceptor extends HandlerInterceptorAdapter {

    private static final String[][] BASE_MAPPINGS = new String[][] {
            { "/alainb", "file:/afs/ir.stanford.edu/users/a/l/alainb/laneweb" },
            { "/ceyates", "file:/afs/ir.stanford.edu/users/c/e/ceyates/laneweb" },
            { "/olgary", "file:/afs/ir.stanford.edu/users/o/l/olgary/laneweb" },
            { "/ryanmax", "file:/afs/ir.stanford.edu/users/r/y/ryanmax/laneweb" },
            { "/ajchrist", "file:/afs/ir.stanford.edu/users/a/j/ajchrist/laneweb" },
            { "/rzwies", "file:/afs/ir.stanford.edu/users/r/z/rzwies/laneweb" } };
    
    private Map<String, URL> baseMappings;
    
    private URL defaultContentBase;
    
    private URL resourcesBase;
    
    private URL stageBase;
    
    public LanewebPathInterceptor() throws MalformedURLException {
        this.baseMappings = new HashMap<String, URL>(BASE_MAPPINGS.length);
        for (String[] element : BASE_MAPPINGS) {
            this.baseMappings.put(element[0], new URL(element[1]));
        }
    }
    
    private String getBasePath(final String servletPath, final String contextPath) {
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

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        String servletPath = requestURI.substring(contextPath.length());
        request.setAttribute(Model.BASE_PATH, getBasePath(servletPath, contextPath));
        request.setAttribute(Model.CONTENT_BASE, getContentBase(servletPath, contextPath));
        request.setAttribute(Model.RESOURCES_BASE, this.resourcesBase);
        return true;
    }

    
    public void setDefaultContentBase(URL defaultContentBase) {
        this.defaultContentBase = defaultContentBase;
    }

    
    public void setResourcesBase(URL resourcesBase) {
        this.resourcesBase = resourcesBase;
    }

    private URL getContentBase(String servletPath, String contextPath) throws MalformedURLException {
        if (servletPath.indexOf("/stage") == 0) {
            return this.stageBase;
        }
        for (String key : this.baseMappings.keySet()) {
            if (servletPath.indexOf(key) == 0) {
                return new URL(this.baseMappings.get(key) + "/content");
            }
        }
        return this.defaultContentBase;
    }

    public void setStageBase(URL stageBase) {
        this.stageBase = stageBase;
    }
    
}
