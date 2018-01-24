package edu.stanford.irt.laneweb.servlet.mvc;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import edu.stanford.irt.laneweb.config.LanewebConfiguration;

public class LanewebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
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
        return null;
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
}
