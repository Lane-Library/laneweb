package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.model.Model;

public class BasePathDataBinder implements DataBinder {

    private String basePath;

    public BasePathDataBinder(final ServletContext servletContext) {
        this.basePath = servletContext.getContextPath();
    }

    @Override
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        model.put(Model.BASE_PATH, this.basePath);
    }
}
