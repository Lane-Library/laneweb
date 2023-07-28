package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

/**
 * A DataBinder that binds the base proxy url to the Model. It requires that proxyLink is already in the model.
 */
public class BaseProxyURLDataBinder implements DataBinder {

    private static final String PROXY_BASE = "https://login.laneproxy.stanford.edu/login?url=";

    @Override
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        Boolean proxyLinks = ModelUtil.getObject(model, Model.PROXY_LINKS, Boolean.class, Boolean.FALSE);
        if (proxyLinks.equals(Boolean.TRUE)) {
            model.put(Model.BASE_PROXY_URL, PROXY_BASE);
        }
    }
}
