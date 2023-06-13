package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.model.Model;

public class RequestMethodDataBinder implements DataBinder {

    @Override
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        String value = request.getQueryString();
        if (value != null) {
            model.put(Model.QUERY_STRING, value);
        }
        value = request.getRequestURI();
        if (value != null) {
            model.put(Model.REQUEST_URI, value);
        }
        value = request.getServletPath();
        if (value != null) {
            model.put(Model.SERVLET_PATH, value);
        }
    }
}
