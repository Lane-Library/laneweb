package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.model.Model;

public class ParameterMapDataBinder implements DataBinder {

    @Override
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        Map parameterMap = request.getParameterMap();
        if (!parameterMap.isEmpty()) {
            model.put(Model.PARAMETER_MAP, parameterMap);
        }
    }
}
