/*
 * Created on Mar 19, 2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package edu.stanford.irt.laneweb;

import java.util.Iterator;
import java.util.Map;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.cocoon.components.modules.input.InputModule;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;

/**
 * @author ceyates
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class LanewebInputModule extends AbstractLogEnabled implements InputModule, ThreadSafe, Configurable, Serviceable {

    private Configuration[] noProxyRegex;

    private Configuration[] proxyRegex;

    private Configuration[] templateConfig;

    private UserInfoHelper userInfoHelper;

    public Object getAttribute(final String key, final Configuration config, final Map objectModel) throws ConfigurationException {
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
        if (getLogger().isDebugEnabled()) {
            getLogger().debug(key + " = " + result);
        }
        return result;
    }

    public Iterator<String> getAttributeNames(final Configuration key, final Map config) {
        throw new UnsupportedOperationException();
    }

    public Object[] getAttributeValues(final String key, final Configuration config, final Map objectModel)
            throws ConfigurationException {
        Object result = getAttribute(key, config, objectModel);
        if (result != null) {
            return new Object[] { result };
        }
        return null;
    }

    public void configure(final Configuration config) throws ConfigurationException {
        this.noProxyRegex = config.getChildren("noproxy-regex");
        this.proxyRegex = config.getChildren("proxy-regex");
        this.templateConfig = config.getChildren("template");
    }

    protected String getTemplateName(final String url) throws ConfigurationException {
        for (Configuration element : this.templateConfig) {
            if (url.matches(element.getAttribute("url"))) {
                return element.getAttribute("value");
            }
        }
        return null;
    }

    protected boolean proxyLinks(final String ip) throws ConfigurationException {
        return !isNoProxy(ip) || isProxy(ip);
    }

    private boolean isNoProxy(final String ip) throws ConfigurationException {
        for (Configuration element : this.noProxyRegex) {
            if (ip.matches(element.getValue())) {
                return true;
            }
        }
        return false;
    }

    private boolean isProxy(final String ip) throws ConfigurationException {
        for (Configuration element : this.proxyRegex) {
            if (ip.matches(element.getValue())) {
                return true;
            }
        }
        return false;
    }

    public void service(final ServiceManager serviceManager) throws ServiceException {
        this.userInfoHelper = (UserInfoHelper) serviceManager.lookup(UserInfoHelper.ROLE);
    }

}