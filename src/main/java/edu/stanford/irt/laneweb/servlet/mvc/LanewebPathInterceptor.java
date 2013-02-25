package edu.stanford.irt.laneweb.servlet.mvc;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import edu.stanford.irt.laneweb.model.Model;

public class LanewebPathInterceptor extends HandlerInterceptorAdapter {

    private URL defaultContentBase;

    private URL stageBase;

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
            throws MalformedURLException {
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        String servletPath = requestURI.substring(contextPath.length());
        String basePath = getBasePath(servletPath, contextPath);
        request.setAttribute(Model.BASE_PATH, basePath);
        int length = basePath.length() - contextPath.length();
        if (length > 0) {
            // FIXME: there must be a better way to reset this, find it and
            // implement it before 1.12 release
            // OK, well, it works so keeping it for the 1.12 release :-)
            String pathWithinMapping = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
            if (pathWithinMapping.length() > length) {
                request.setAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, pathWithinMapping.substring(length));
            }
        }
        request.setAttribute(Model.CONTENT_BASE, getContentBase(servletPath));
        return true;
    }

    public void setDefaultContentBase(final URL defaultContentBase) {
        this.defaultContentBase = defaultContentBase;
    }

    public void setStageBase(final URL stageBase) {
        this.stageBase = stageBase;
    }

    private String getBasePath(final String servletPath, final String contextPath) {
        if (servletPath.indexOf("/stage") == 0) {
            return contextPath + "/stage";
        }
        return contextPath;
    }

    private URL getContentBase(final String servletPath) throws MalformedURLException {
        if (servletPath.indexOf("/stage") == 0) {
            return this.stageBase;
        }
        return this.defaultContentBase;
    }
}
