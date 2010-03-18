package edu.stanford.irt.laneweb.cocoon;

import javax.servlet.ServletContext;

import org.apache.avalon.framework.service.ServiceManager;


public class ContextSourceFactory extends org.apache.cocoon.components.source.impl.ContextSourceFactory {

    public ContextSourceFactory(ServletContext servletContext, ServiceManager serviceManager) {
        this.manager = serviceManager;
        this.servletContext = servletContext;
    }

}
