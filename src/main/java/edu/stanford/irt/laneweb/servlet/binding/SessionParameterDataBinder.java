package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public abstract class SessionParameterDataBinder<T> implements DataBinder {

    private String parameterName;

    private String modelKey;

    public void bind(Map<String, Object> model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String parameterValue = request.getParameter(this.parameterName);
        T value = null;
        if (parameterValue == null) {
            value = (T) session.getAttribute(this.modelKey);
        } else {
            value = getParameterAsObject(parameterValue);
        }
        if (value != null) {
            session.setAttribute(this.modelKey, value);
            model.put(this.modelKey, value);
        }
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public void setModelKey(String modelKey) {
        this.modelKey = modelKey;
    }

    protected abstract T getParameterAsObject(String parameterValue);
}
