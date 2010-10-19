package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.model.Model;

public class ServletContextDataBinder extends BasePathDataBinder {

    private ServletContext servletContext;

    @Override
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        model.put(Model.VERSION, this.servletContext.getAttribute("laneweb.context.version"));
        model.put(Model.MEDBLOG_BASE, this.servletContext.getAttribute("laneweb.context.medblog-base"));
        model.put(Model.DEFAULT_CONTENT_BASE, this.servletContext.getAttribute("laneweb.context.live-base"));
        model.put(Model.CONTENT_BASE, model.get("default-content-base"));
        model.put(Model.DEFAULT_RESOURCES_BASE, "file:" + this.servletContext.getRealPath("/").toString() + "resources");
        model.put(Model.RESOURCES_BASE, model.get("default-resources-base"));
        String uri = (String) model.get(Model.BASE_PATH);
        if (uri.indexOf("/stage") == 0) {
            model.put(Model.CONTENT_BASE, this.servletContext.getAttribute("laneweb.context.stage-base"));
        } else {
            for (Entry<String, String> entry : this.baseMappings.entrySet()) {
                if (uri.indexOf(entry.getKey()) == 0) {
                    model.put(Model.CONTENT_BASE, entry.getValue() + "/content");
                    model.put(Model.RESOURCES_BASE, entry.getValue() + "/resources");
                }
            }
        }
    }

    public void setServletContext(final ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
