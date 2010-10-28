package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.LanewebContextListener;

public class ServletContextDataBinder implements DataBinder {

    private ServletContext servletContext;

    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        model.put(Model.VERSION, this.servletContext.getAttribute(LanewebContextListener.VERSION));
        model.put(Model.MEDBLOG_BASE, this.servletContext.getAttribute(LanewebContextListener.MEDBLOG_BASE));
        model.put(Model.DEFAULT_CONTENT_BASE, this.servletContext.getAttribute(LanewebContextListener.LIVE_BASE));
    }

    public void setServletContext(final ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
