package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.model.Model;

public class RequestAttributeDataBinder implements DataBinder {

    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        model.put(Model.BASE_PATH, request.getAttribute(Model.BASE_PATH));
        model.put(Model.CONTENT_BASE, request.getAttribute(Model.CONTENT_BASE));
    }
}
