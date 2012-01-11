package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.model.Model;


public class ParameterMapDataBinder implements DataBinder {

    @Override
    public void bind(Map<String, Object> model, HttpServletRequest request) {
        model.put(Model.PARAMETER_MAP, request.getParameterMap());
    }
}
