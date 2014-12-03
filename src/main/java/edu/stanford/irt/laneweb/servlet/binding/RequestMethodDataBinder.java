package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.model.Model;

public class RequestMethodDataBinder implements DataBinder {

    private static final Pattern PROXY_LINKS = Pattern.compile("(&|)proxy-links=(true|false)");

    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        String value = request.getQueryString();
        if (value != null) {
            //remove the proxy-links parameter
            model.put(Model.QUERY_STRING, PROXY_LINKS.matcher(value).replaceAll(""));
        }
        value = request.getRequestURI();
        if (value != null) {
            model.put(Model.REQUEST_URI, value);
        }
    }
}
