package edu.stanford.irt.laneweb.servlet.binding;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.model.Model;

public class RequestParameterDataBinder implements DataBinder {

    @SuppressWarnings("rawtypes")
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        for (Enumeration params = request.getParameterNames(); params.hasMoreElements();) {
            String name = (String) params.nextElement();
            String value = request.getParameter(name);
            if ("q".equals(name)) {
                model.put(Model.QUERY, value);
                try {
                    model.put("url-encoded-query", URLEncoder.encode(value, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    // won't happen
                }
            } else if ("t".equals(name)) {
                model.put(Model.TYPE, value);
            } else if ("s".equals(name)) {
                model.put(Model.SUBSET, value);
            } else if ("a".equals(name)) {
                model.put(Model.ALPHA, value);
            } else if ("m".equals(name)) {
                model.put(Model.MESH, value);
            } else if ("f".equals(name)) {
                model.put(Model.FACETS, value);
            } else if ("l".equals(name)) {
                model.put(Model.LIMIT, value);
            } else if ("bn".equals(name)) {
                model.put(Model.BASSETT_NUMBER, value);
            } else if ("r".equals(name)) {
                model.put(Model.RESOURCES, Arrays.asList(request.getParameterValues(name)));
                model.put(Model.REGION, value);
            } else if ("e".equals(name)) {
                model.put(Model.ENGINES, Arrays.asList(request.getParameterValues(name)));
            } else if ("source".equals(name)) {
                model.put(Model.SOURCE, value);
            } else if ("sourceid".equals(name)) {
                model.put(Model.SOURCEID, value);
            } else if ("host".equals(name)) {
                model.put(Model.HOST, value);
            } else if ("release".equals(name)) {
                model.put(Model.RELEASE, value);
            } else if ("password".equals(name)) {
                model.put(Model.PASSWORD, value);
            } else if ("PID".equals(name)) {
                model.put(Model.PID, value);
            } else if ("page-number".equals(name)) {
                model.put(Model.PAGE_NUMBER, value);
            } else if (Model.CALLBACK.equals(name)) {
                model.put(Model.CALLBACK, value);
            } else if (Model.URL.equals(name)) {
                model.put(Model.URL, value);
            } else if (Model.BASSETT_NUMBER.equals(name)) {
                model.put(Model.BASSETT_NUMBER, value);
            } else if (Model.SELECTION.equals(name)) {
                model.put(Model.SELECTION, value);
            } else if (Model.TITLE.equals(name)) {
                model.put(Model.TITLE, value);
            } else if ("entryUrl".equals(name)) {
                model.put(Model.ENTRY_URL, value);
            } else if (Model.PAGE.equals(name)) {
                model.put(Model.PAGE, value);
            } else if ("pl".equals(name)) {
                model.put(Model.PERSISTENT_LOGIN, value);
            } else if ("remove-pl".equals(name)) {
                model.put(Model.REMOVE_PERSISTENT_LOGIN, value);
            } else if ("rid".equals(name)) {
                model.put(Model.RESOURCE_ID, value);
            } else if (Model.SYNCHRONOUS.equals(name)) {
                    model.put(Model.SYNCHRONOUS, value);
            } else if (Model.TIMEOUT.equals(name)) {
                model.put(Model.TIMEOUT, value);
            }
                // } else {
                // model.put(name, request.getParameter(name));
            
        }
    }
}
