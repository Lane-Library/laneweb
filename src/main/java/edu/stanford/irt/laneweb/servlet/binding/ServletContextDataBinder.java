package edu.stanford.irt.laneweb.servlet.binding;

import java.net.MalformedURLException;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.model.Model;

public class ServletContextDataBinder implements DataBinder {

    private ServletContext servletContext;

    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        model.put(Model.VERSION, this.servletContext.getAttribute("laneweb.context.version"));
        model.put(Model.MEDBLOG_BASE, this.servletContext.getAttribute("laneweb.context.medblog-base"));
        model.put(Model.DEFAULT_CONTENT_BASE, this.servletContext.getAttribute("laneweb.context.live-base"));
            try {
                model.put(Model.DEFAULT_RESOURCES_BASE, this.servletContext.getResource("/resources"));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

    }

    public void setServletContext(final ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
