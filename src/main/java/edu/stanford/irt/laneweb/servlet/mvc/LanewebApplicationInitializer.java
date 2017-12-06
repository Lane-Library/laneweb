package edu.stanford.irt.laneweb.servlet.mvc;

import java.util.Collections;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import javax.servlet.SessionTrackingMode;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class LanewebApplicationInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(final ServletContext servletContext) {
        servletContext.getFilterRegistration("javamelody").setInitParameter("storage-directory",
                System.getProperty("catalina.base") + "/logs/javamelody");
        servletContext.setSessionTrackingModes(Collections.singleton(SessionTrackingMode.COOKIE));
        servletContext.setInitParameter("webAppRootKey", "laneweb");
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation("edu.stanford.irt.laneweb.config.LanewebConfiguration");
        servletContext.addListener(new ContextLoaderListener(context));
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("DispatcherServlet",
                new DispatcherServlet(context));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }
}
