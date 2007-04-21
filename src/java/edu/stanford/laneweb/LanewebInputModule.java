/*
 * Created on Mar 19, 2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package edu.stanford.laneweb;

import edu.stanford.irt.SystemException;

import java.util.Iterator;
import java.util.Map;

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.cocoon.components.modules.input.InputModule;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;

/**
 * @author ceyates
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class LanewebInputModule extends AbstractLogEnabled implements
		InputModule, ThreadSafe, Configurable {

	private Configuration[] noProxyRegex;

	private Configuration[] proxyRegex;

	private Configuration[] templateConfig;
	
	private String ezproxyKey;

	public Object getAttribute(String key, Configuration config, Map objectModel)
			throws ConfigurationException {
		Object result = null;
		Request request = ObjectModelHelper.getRequest(objectModel);
		UserInfo userInfo = (UserInfo) request.getAttribute(LanewebConstants.USER_INFO);
		if (userInfo == null) {
			Session session = request.getSession(true);
			userInfo = (UserInfo) session.getAttribute(LanewebConstants.USER_INFO);
			if (userInfo == null) {
				userInfo = new UserInfo();
				session.setAttribute(LanewebConstants.USER_INFO, userInfo);
			}
			request.setAttribute(LanewebConstants.USER_INFO, userInfo);
				userInfo.update(objectModel, getLogger());
			}
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

	public Iterator getAttributeNames(Configuration key, Map config) {
		throw new UnsupportedOperationException();
	}

	public Object[] getAttributeValues(String key, Configuration config,
			Map objectModel) throws ConfigurationException {
		Object result = getAttribute(key, config, objectModel);
		if (result != null) {
			return new Object[] { result };
		}
		return null;
	}

	public void configure(Configuration config) throws ConfigurationException {
		this.noProxyRegex = config.getChildren("noproxy-regex");
		this.proxyRegex = config.getChildren("proxy-regex");
		this.templateConfig = config.getChildren("template");
		this.ezproxyKey = config.getChild("ezproxy-key").getValue();
	}

	protected String getTemplateName(final String url)
			throws ConfigurationException {
		for (int i = 0; i < this.templateConfig.length; i++) {
			if (url.matches(this.templateConfig[i].getAttribute("url"))) {
				return this.templateConfig[i].getAttribute("value");
			}
		}
		return null;
	}

	protected boolean proxyLinks(final String ip) throws ConfigurationException {
		return !isNoProxy(ip) || isProxy(ip);
	}

	private boolean isNoProxy(final String ip) throws ConfigurationException {
		for (int i = 0; i < this.noProxyRegex.length; i++) {
			if (ip.matches(this.noProxyRegex[i].getValue())) {
				return true;
			}
		}
		return false;
	}

	private boolean isProxy(String ip) throws ConfigurationException {
		for (int i = 0; i < this.proxyRegex.length; i++) {
			if (ip.matches(this.proxyRegex[i].getValue())) {
				return true;
			}
		}
		return false;
	}

}