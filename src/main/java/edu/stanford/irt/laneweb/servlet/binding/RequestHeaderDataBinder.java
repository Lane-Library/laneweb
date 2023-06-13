package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.model.Model;

public class RequestHeaderDataBinder implements DataBinder {

    @Override
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        String value = request.getHeader("referer");
        if (value != null) {
            model.put(Model.REFERRER, value);
        }
        value = request.getHeader("user-agent");
        if (value != null) {
            model.put(Model.USER_AGENT, value);
        }
    }
}
