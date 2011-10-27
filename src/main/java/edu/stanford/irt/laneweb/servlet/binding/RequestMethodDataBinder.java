package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.model.Model;

public class RequestMethodDataBinder implements DataBinder {

    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        String value = request.getQueryString();
        if (value != null) {
            model.put(Model.QUERY_STRING, value);
        }
        value = request.getRequestURI();
        if (value != null) {
            model.put(Model.REQUEST_URI, value);
        }
        value = request.getHeader("referer");
        if (value != null) {
            model.put(Model.REFERRER, value);
        }
        value = request.getRequestURL().toString();
        if (value != null) {
            model.put(Model.REQUEST_URL, value);
        }
    }
}
