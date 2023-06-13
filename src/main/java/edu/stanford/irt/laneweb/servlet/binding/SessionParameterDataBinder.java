package edu.stanford.irt.laneweb.servlet.binding;

import java.io.Serializable;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public abstract class SessionParameterDataBinder<T extends Serializable> implements DataBinder {

    private String modelKey;

    private String parameterName;

    protected SessionParameterDataBinder(final String modelKey, final String parameterName) {
        this.modelKey = modelKey;
        this.parameterName = parameterName;
    }

    @Override
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        HttpSession session = request.getSession();
        String parameterValue = request.getParameter(this.parameterName);
        T value = parameterValue == null ? null : getParameterAsObject(parameterValue);
        if (value == null) {
            value = (T) session.getAttribute(this.modelKey);
        } else {
            session.setAttribute(this.modelKey, value);
        }
        if (value != null) {
            model.put(this.modelKey, value);
        }
    }

    protected abstract T getParameterAsObject(String parameterValue);
}
