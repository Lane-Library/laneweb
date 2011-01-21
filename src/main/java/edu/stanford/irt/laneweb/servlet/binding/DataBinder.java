package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface DataBinder {

    void bind(Map<String, Object> model, HttpServletRequest request);
}
