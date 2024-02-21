package edu.stanford.irt.laneweb.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class LanewebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    public void onStartup(final ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        servletContext.getFilterRegistration("javamelody").setInitParameter("storage-directory",
                System.getProperty("catalina.base") + "/logs/javamelody");
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[] { LanewebConfiguration.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] {};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
}
