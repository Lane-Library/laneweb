package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public abstract class SessionParameterDataBinder<T> implements DataBinder {

    private String modelKey;

    private String parameterName;

    @SuppressWarnings("unchecked")
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
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

    public void setModelKey(final String modelKey) {
        this.modelKey = modelKey;
    }

    public void setParameterName(final String parameterName) {
        this.parameterName = parameterName;
    }

    protected abstract T getParameterAsObject(String parameterValue);
}
