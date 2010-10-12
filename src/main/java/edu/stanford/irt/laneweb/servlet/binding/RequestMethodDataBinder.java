package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.model.Model;

public class RequestMethodDataBinder implements DataBinder {

    public void bind(Map<String, Object> model, HttpServletRequest request) {
        String value = request.getQueryString();
        if (value != null) {
            model.put(Model.QUERY_STRING, request.getQueryString());
        }
        value = request.getRequestURI();
        if (value != null) {
            model.put(Model.REQUEST_URI, request.getRequestURI());
        }
        value = request.getHeader("referer");
        {
            model.put(Model.REFERRER, request.getHeader("referer"));
        }
    }
}
