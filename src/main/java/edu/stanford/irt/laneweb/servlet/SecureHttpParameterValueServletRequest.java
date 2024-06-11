package edu.stanford.irt.laneweb.servlet;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

public class SecureHttpParameterValueServletRequest extends HttpServletRequestWrapper {

  public SecureHttpParameterValueServletRequest(HttpServletRequest request) {
    super(request);
  }

  private static final String[] FORBIDDEN_VALUES = { "\0" };

  @Override
  public String getParameter(String name) {
    String value = super.getParameter(name);
    return removeForbiddenValue(value);
  }

  @Override
  public String[] getParameterValues(String name) {
    String[] values = super.getParameterValues(name);
    return removeForbiddenValueFromArray(values);
  }

  @Override
  public Map<String, String[]> getParameterMap() {
    Map<String, String[]> result = new HashMap<>();
    Map<String, String[]> parameterMap = super.getParameterMap();
    for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
      String[] values = removeForbiddenValueFromArray(entry.getValue());
      result.put(entry.getKey(), values);
    }
    return result;
  }

  private String[] removeForbiddenValueFromArray(String[] values) {
    if (values != null) {
      for (int i = 0; i < values.length; i++) {
        values[i] = removeForbiddenValue(values[i]);
      }
    }
    return values;
  }

  private String removeForbiddenValue(String value) {
    for (String forbiddenValue : FORBIDDEN_VALUES) {
      if (value != null && value.contains(forbiddenValue)) {
        value = value.replace(forbiddenValue, "");
      }
    }
    return value;
  }

}
