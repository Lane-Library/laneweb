package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public abstract class SessionParameterDataBinder<T> implements DataBinder {

    private String modelKey;

    private String parameterName;

    public SessionParameterDataBinder(final String modelKey, final String parameterName) {
        this.modelKey = modelKey;
        this.parameterName = parameterName;
    }

    @SuppressWarnings("unchecked")
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        HttpSession session = request.getSession();
        String parameterValue = request.getParameter(this.parameterName);
        T value = parameterValue == null ? null : getParameterAsObject(parameterValue);
        synchronized (session) {
            if (value == null) {
                value = (T) session.getAttribute(this.modelKey);
            } else {
                session.setAttribute(this.modelKey, value);
            }
        }
        if (value != null) {
            model.put(this.modelKey, value);
        }
    }

    protected abstract T getParameterAsObject(String parameterValue);
}
