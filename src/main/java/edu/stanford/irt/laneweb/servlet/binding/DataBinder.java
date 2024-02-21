package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

@FunctionalInterface
public interface DataBinder {

    void bind(Map<String, Object> model, HttpServletRequest request);
}
