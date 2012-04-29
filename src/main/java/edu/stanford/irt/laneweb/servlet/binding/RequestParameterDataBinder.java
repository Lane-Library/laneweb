package edu.stanford.irt.laneweb.servlet.binding;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.model.Model;

public class RequestParameterDataBinder implements DataBinder {

    private static final String[][] PARAMETER_ARRAY_MODEL = { { "r", Model.RESOURCES }, { "e", Model.ENGINES },
            { "i", Model.ITEMS } };

    private static final String[][] PARAMETER_MODEL = { { "q", Model.QUERY }, { "t", Model.TYPE }, { "s", Model.SUBSET },
            { "a", Model.ALPHA }, { "m", Model.MESH }, { "f", Model.FACETS }, { "l", Model.LIMIT }, { "bn", Model.BASSETT_NUMBER },
            { "r", Model.REGION }, { "PID", Model.PID }, { "page-number", Model.PAGE_NUMBER }, { "entryUrl", Model.ENTRY_URL },
            { "pl", Model.PERSISTENT_LOGIN }, { "remove-pl", Model.REMOVE_PERSISTENT_LOGIN } };

    private static final String[] PARAMETER_SAME_AS_MODEL = { Model.ACTION, Model.BANNER, Model.CATEGORY, Model.CLASS_ID,
            Model.TIMEOUT, Model.SYNCHRONOUS, Model.RESOURCE_ID, Model.PAGE, Model.TITLE, Model.SELECTION, Model.BASSETT_NUMBER,
            Model.URL, Model.CALLBACK, Model.PASSWORD, Model.RELEASE, Model.HOST, Model.SOURCEID, Model.SOURCE, Model.ID,
            Model.TEXT };

    /**
     * parameterArrayModelMap contains the mapping of parameter names to model
     * name of model attributes that are Lists
     */
    private Map<String, String> parameterArrayModelMap;

    /**
     * parameterModelMap contains the mapping of parameter names to model names.
     */
    private Map<String, String> parameterModelMap;

    private Set<String> parameterSameAsModel;

    public RequestParameterDataBinder() {
        this.parameterModelMap = new HashMap<String, String>();
        for (String[] element : PARAMETER_MODEL) {
            this.parameterModelMap.put(element[0], element[1]);
        }
        this.parameterArrayModelMap = new HashMap<String, String>();
        for (String[] element : PARAMETER_ARRAY_MODEL) {
            this.parameterArrayModelMap.put(element[0], element[1]);
        }
        this.parameterSameAsModel = new HashSet<String>();
        for (String name : PARAMETER_SAME_AS_MODEL) {
            this.parameterSameAsModel.add(name);
        }
    }

    @SuppressWarnings("rawtypes")
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        for (Enumeration params = request.getParameterNames(); params.hasMoreElements();) {
            String name = (String) params.nextElement();
            String value = request.getParameter(name);
            if (this.parameterSameAsModel.contains(name)) {
                model.put(name, value);
            } else if (this.parameterModelMap.containsKey(name)) {
                model.put(this.parameterModelMap.get(name), value);
                if ("q".equals(name)) {
                    try {
                        model.put(Model.URL_ENCODED_QUERY, URLEncoder.encode(value, "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        throw new LanewebException(e);
                    }
                }
            }
            if (this.parameterArrayModelMap.containsKey(name)) {
                model.put(this.parameterArrayModelMap.get(name), Arrays.asList(request.getParameterValues(name)));
            }
        }
    }
}
