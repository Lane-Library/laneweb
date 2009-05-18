package edu.stanford.irt.laneweb.inputmodule;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.avalon.framework.configuration.Configuration;

import edu.stanford.irt.laneweb.user.User;

public class Template extends AbstractInputModule {

    private Map<String, String> templateConfig;

    @Override
    @SuppressWarnings("unchecked")
    public Object[] getAttributeValues(final String name, final Configuration modeConf, final Map objectModel) {
        Object obj = getAttribute(name, modeConf, objectModel);
        return null == obj ? null : new Object[] { obj };
    }

    public void setTemplateConfig(final Map<String, String> templateConfig) {
        this.templateConfig = templateConfig;
    }

    @Override
    protected Object doGetAttribute(final String key, final User user, final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        int contextPathLength = request.getContextPath().length();
        return getTemplateName(requestURI.substring(contextPathLength + 1));
    }

    protected String getTemplateName(final String url) {
        for (String key : this.templateConfig.keySet()) {
            if (url.matches(key)) {
                return this.templateConfig.get(key);
            }
        }
        return null;
    }
}
