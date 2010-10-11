package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.model.Model;


public class RequestMethodDataBinder implements DataBinder {

    public void bind(Map<String, Object> model, HttpServletRequest request) {
        model.put(Model.QUERY_STRING, request.getQueryString());
        model.put(Model.REQUEST_URI, request.getRequestURI());
        model.put(Model.REFERRER, request.getHeader("referer"));
    }

}
