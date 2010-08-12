package edu.stanford.irt.laneweb.cocoon;

import java.net.MalformedURLException;

import javax.servlet.ServletContext;

import org.apache.avalon.framework.service.ServiceManager;
import org.apache.cocoon.components.source.CocoonSourceResolver;

public class SourceResolver extends CocoonSourceResolver {

    public SourceResolver(final ServletContext servletContext, final ServiceManager serviceManager)
            throws MalformedURLException {
        this.baseURL = servletContext.getResource("/");
        this.manager = serviceManager;
    }
}
