package edu.stanford.irt.laneweb.servlet.binding;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.model.Model;

//TODO: have a set of parameters where the model name is the same as the parameter names
public class RequestParameterDataBinder implements DataBinder {

    private static final String[][] PARAMETER_ARRAY_MODEL = { { "r", Model.RESOURCES }, { "e", Model.ENGINES } };

    private static final String[][] PARAMETER_MODEL = { { "q", Model.QUERY }, { "t", Model.TYPE }, { "s", Model.SUBSET },
            { "a", Model.ALPHA }, { "m", Model.MESH }, { "f", Model.FACETS }, { "l", Model.LIMIT }, { "bn", Model.BASSETT_NUMBER },
            { "r", Model.REGION }, { "source", Model.SOURCE }, { "sourceid", Model.SOURCEID }, { "host", Model.HOST },
            { "release", Model.RELEASE }, { "password", Model.PASSWORD }, { "PID", Model.PID },
            { "page-number", Model.PAGE_NUMBER }, { Model.CALLBACK, Model.CALLBACK }, { Model.URL, Model.URL },
            { Model.BASSETT_NUMBER, Model.BASSETT_NUMBER }, { Model.SELECTION, Model.SELECTION }, { Model.TITLE, Model.TITLE },
            { "entryUrl", Model.ENTRY_URL }, { Model.PAGE, Model.PAGE }, { "pl", Model.PERSISTENT_LOGIN },
            { "remove-pl", Model.REMOVE_PERSISTENT_LOGIN }, { "rid", Model.RESOURCE_ID }, { Model.SYNCHRONOUS, Model.SYNCHRONOUS },
            { Model.TIMEOUT, Model.TIMEOUT }, { Model.CLASS_ID, Model.CLASS_ID }, { Model.BANNER, Model.BANNER } };

    /**
     * parameterArrayModelMap contains the mapping of parameter names to model name of model attributes
     * that are Lists
     */
    private Map<String, String> parameterArrayModelMap;

    /**
     * parameterModelMap contains the mapping of parameter names to model names.
     */
    private Map<String, String> parameterModelMap;

    public RequestParameterDataBinder() {
        this.parameterModelMap = new HashMap<String, String>();
        for (int i = 0; i < PARAMETER_MODEL.length; i++) {
            this.parameterModelMap.put(PARAMETER_MODEL[i][0], PARAMETER_ARRAY_MODEL[i][1]);
        }
        this.parameterArrayModelMap = new HashMap<String, String>();
        for (String[] element : PARAMETER_ARRAY_MODEL) {
            this.parameterArrayModelMap.put(element[0], element[1]);
        }
    }

    @SuppressWarnings("rawtypes")
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        for (Enumeration params = request.getParameterNames(); params.hasMoreElements();) {
            String name = (String) params.nextElement();
            String value = request.getParameter(name);
            if ("q".equals(name)) {
                try {
                    model.put("url-encoded-query", URLEncoder.encode(value, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
            if (this.parameterModelMap.containsKey(name)) {
                model.put(this.parameterModelMap.get(name), value);
            }
            if (this.parameterArrayModelMap.containsKey(name)) {
                model.put(this.parameterArrayModelMap.get(name), Arrays.asList(request.getParameterValues(name)));
            }
        }
    }
}
