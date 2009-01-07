/*
 * Created on Mar 19, 2004 To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package edu.stanford.irt.laneweb;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.cocoon.components.modules.input.InputModule;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.log4j.Logger;

/**
 * @author ceyates To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Generation - Code and Comments
 */
public class LanewebInputModule implements InputModule {

    private Logger logger = Logger.getLogger(LanewebInputModule.class);

    private List<String> noProxyRegex;

    private List<String> proxyRegex;

    private Map<String, String> templateConfig;

    private UserInfoHelper userInfoHelper;

    public Object getAttribute(final String key, final Configuration config, final Map objectModel) {
        Object result = null;
        Request request = ObjectModelHelper.getRequest(objectModel);
        UserInfo userInfo = this.userInfoHelper.getUserInfo(request);

        if (LanewebConstants.PROXY_LINKS.equals(key)) {
            String ip = request.getRemoteAddr();
            // mod_proxy puts the real remote address in an x-forwarded-for
            // header
            // Load balancer also does this
            String header = request.getHeader(LanewebConstants.X_FORWARDED_FOR);
            if (header != null) {
                ip = header;
            }
            result = userInfo.getProxyLinks() != null ? userInfo.getProxyLinks() : new Boolean(proxyLinks(ip));
        }
        if (LanewebConstants.AFFILIATION.equals(key)) {
            result = userInfo.getAffiliation();
        }
        if (LanewebConstants.TEMPLATE.equals(key)) {
            String requestURI = request.getRequestURI();
            int contextPathLength = request.getContextPath().length();
            result = getTemplateName(requestURI.substring(contextPathLength + 1));
        }
        if (LanewebConstants.SUNETID.equals(key)) {
            result = userInfo.getSunetId();
        }
        if (LanewebConstants.TICKET.equals(key)) {
            result = userInfo.getTicket();
        }
        if (LanewebConstants.FULL_NAME.equals(key)) {
            if(userInfo.getPerson() != null)
        	result = userInfo.getPerson().getDisplayName();
            if(result == null || result.equals(""))
        	result = userInfo.getSunetId();
        }
        
        if (this.logger.isDebugEnabled()) {
            this.logger.debug(key + " = " + result);
        }
        return result;
    }

    public Iterator<String> getAttributeNames(final Configuration key, final Map config) {
        throw new UnsupportedOperationException();
    }

    public Object[] getAttributeValues(final String key, final Configuration config, final Map objectModel) {
        Object result = getAttribute(key, config, objectModel);
        if (result != null) {
            return new Object[] { result };
        }
        return null;
    }

    public void setNoProxyRegex(final List<String> noProxyRegex) {
        this.noProxyRegex = noProxyRegex;
    }

    public void setProxyRegex(final List<String> proxyRegex) {
        this.proxyRegex = proxyRegex;
    }

    public void setTemplateConfig(final Map<String, String> templateConfig) {
        this.templateConfig = templateConfig;
    }

    protected String getTemplateName(final String url) {
        for (String key : this.templateConfig.keySet()) {
            if (url.matches(key)) {
                return this.templateConfig.get(key);
            }
        }
        return null;
    }

    protected boolean proxyLinks(final String ip) {
        return !isNoProxy(ip) || isProxy(ip);
    }

    private boolean isNoProxy(final String ip) {
        for (String regex : this.noProxyRegex) {
            if (ip.matches(regex)) {
                return true;
            }
        }
        return false;
    }

    private boolean isProxy(final String ip) {
        for (String regex : this.proxyRegex) {
            if (ip.matches(regex)) {
                return true;
            }
        }
        return false;
    }

    public void setUserInfoHelper(final UserInfoHelper userInfoHelper) {
        if (null == userInfoHelper) {
            throw new IllegalArgumentException("null userInfoHelper");
        }
        this.userInfoHelper = userInfoHelper;
    }

}