/*
 * Created on Mar 19, 2004
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package edu.stanford.laneweb;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

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

	static final String PROXY_LINKS = "proxy-links";

	static final String AFFILIATION = "affiliation";

	static final String TEMPLATE = "template";

	static final String TICKET = "ticket";

	static final String SUNETID = "sunetid";

	static final String SHC = "SHC";

	static final String LPCH = "LPCH";

	static final String SU = "SU";

	static final String SOM = "SOM";

	static final String PAVA = "PAVA";

	static final String OTHER = "OTHER";

	static final String ERR = "ERR";

	static final String STAFF = "STAFF";

	static final String LANE = "LANE";

	static final List ATTRS = new ArrayList();
	static {
		ATTRS.add(PROXY_LINKS);
		ATTRS.add(AFFILIATION);
		ATTRS.add(TEMPLATE);
		ATTRS.add(TICKET);
		ATTRS.add(SUNETID);
	}

	private Configuration[] noProxyRegex;

	private Configuration[] proxyRegex;

	private Configuration[] templateConfig;

	private String ezproxyKey;

	public Object getAttribute(String key, Configuration config, Map objectModel)
			throws ConfigurationException {
		Object result = null;
		Request request = ObjectModelHelper.getRequest(objectModel);
		if (key.equals(PROXY_LINKS) || key.equals(AFFILIATION)) {
			String ip = request.getRemoteAddr();
			// mod_proxy puts the real remote address in an x-forwarded-for
			// header
			// Load balancer also does this
			String header = request.getHeader("X-FORWARDED-FOR");
			if (header != null) {
				ip = header;
			}
			if (key.equals(PROXY_LINKS)) {
				result = proxyLinks(ip);
			} else if (key.equals(AFFILIATION)) {
				result = getAffiliation(ip);
			}

		} else if (key.equals(TEMPLATE)) {
			String requestURI = request.getRequestURI();
			int contextPathLength = request.getContextPath().length();
			result = getTemplateName(requestURI.substring(contextPathLength + 1));
		} else if (key.equals(SUNETID)) {
			Session session = request.getSession(false);
			if (session != null) {
				result = session.getAttribute(SUNETID);
			}
		} else if (key.equals(TICKET)) {
			Session session = request.getSession(false);
			if (session != null) {
				String sunetid = (String) session.getAttribute(SUNETID);
				if (sunetid != null) {
					result = getTicket(sunetid);
				}
			}
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

	protected String proxyLinks(final String ip) throws ConfigurationException {
		return Boolean.toString(!isNoProxy(ip) || isProxy(ip));
	}

	protected String getTicket(String user) {
		String result = null;
		Date now = new Date();
		String packet = "$u" + ((int) (now.getTime() / 1000));
		try {
			result = URLEncoder.encode(getKeyedDigest(ezproxyKey + user
					+ packet)
					+ packet, "UTF8");

		} catch (Exception e) {
			getLogger().fatalError(e.getLocalizedMessage());
		}
		return result;
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

	protected String getAffiliation(final String ip) {
		int[] d = new int[4];
		int index = 0;
		try {
			for (StringTokenizer tokenizer = new StringTokenizer(ip, "."); tokenizer
					.hasMoreTokens();) {
				d[index++] = Integer.parseInt(tokenizer.nextToken());
			}
		} catch (Exception e) {
			getLogger().warn("problem parsing ip address: " + ip);
			return ERR;
		}
		if (d[0] == 10) {
			if (d[1] == 252) {
				if (d[2] == 6 || d[2] == 7 || d[2] == 10 || d[2] == 11
						|| d[2] == 14 || d[2] == 15 || d[2] == 18 || d[2] == 19
						|| d[2] == 22 || d[2] == 23 || d[2] == 26 || d[2] == 27
						|| d[2] == 30 || d[2] == 31 || d[2] == 34 || d[2] == 35
						|| d[2] == 38 || d[2] == 39 || d[2] == 43 || d[2] == 46
						|| d[2] == 47 || d[2] == 50 || d[2] == 51 || d[2] == 54
						|| d[2] == 55 || d[2] == 67 || d[2] == 71 || d[2] == 97
						|| d[2] == 108 || d[2] == 109 || d[2] == 122
						|| d[2] == 123) {
					return LPCH;
				}
				if (d[2] >= 138 && d[2] <= 146) {
					return LPCH;
				}
				if (d[2] == 250) {
					if (d[3] >= 96 && d[3] <= 127) {
						return LPCH;
					}
					return SHC;
				}
				return SHC;
			}
			if (d[1] >= 253 && d[1] <= 255) {
				return SHC;
			}
			return OTHER;
		}
		if (d[0] == 128) {
			if (d[1] == 12) {
				return SU;
			}
			return OTHER;
		}
		if (d[0] == 134) {
			if (d[1] == 79) {
				return SU;
			}
			return OTHER;
		}
		if (d[0] == 152) {
			if (d[1] >= 130 && d[1] <= 133) {
				if (d[2] == 10 && d[3] == 128) {
					return PAVA;
				}
			}
			return OTHER;
		}
		if (d[0] == 171) {
			if (d[1] == 64) {
				return SU;
			}
			if (d[1] == 65) {
				if (d[2] >= 1 && d[2] <= 43) {
					return SOM;
				}
				if (d[2] >= 44 && d[2] <= 45) {
					return SHC;
				}
				if (d[2] >= 46 && d[2] <= 55) {
					return SOM;
				}
				if (d[2] >= 56 && d[2] <= 59) {
					return LPCH;
				}
				if (d[2] >= 60 && d[2] <= 81) {
					return SOM;
				}
				if (d[2] == 82) {
					if (d[3] == 12 || d[3] == 14 || d[3] == 15 || d[3] == 25
							|| d[3] == 26 || d[3] == 30 || d[3] == 33
							|| d[3] == 35 || d[3] == 36 || d[3] == 37
							|| d[3] == 41 || d[3] == 42 || d[3] == 51
							|| d[3] == 53 || d[3] == 54 || d[3] == 55
							|| d[3] == 56 || d[3] == 58 || d[3] == 59
							|| d[3] == 61 || d[3] == 62 || d[3] == 66
							|| d[3] == 71 || d[3] == 76 || d[3] == 79
							|| d[3] == 81 || d[3] == 83 || d[3] == 84
							|| d[3] == 85 || d[3] == 86 || d[3] == 94
							|| d[3] == 104 || d[3] == 105 || d[3] == 108
							|| d[3] == 109 || d[3] == 110 || d[3] == 115
							|| d[3] == 119 || d[3] == 124 || d[3] == 125
							|| d[3] == 129 || d[3] == 130 || d[3] == 133
							|| d[3] == 135 || d[3] == 139 || d[3] == 141
							|| d[3] == 143 || d[3] == 144 || d[3] == 145
							|| d[3] == 146 || d[3] == 149 || d[3] == 180
							|| d[3] == 182 || d[3] == 187 || d[3] == 197
							|| d[3] == 204 || d[3] == 219 || d[3] == 231
							|| d[3] == 246 || d[3] == 247 || d[3] == 248
							|| d[3] == 249) {
						return STAFF;
					}
					return SOM;
				}
				if (d[2] == 83) {
					if (d[3] == 3 || d[3] == 8 || d[3] == 82 || d[3] == 137
							|| d[3] == 138 || d[3] == 181 || d[3] == 189) {
						return STAFF;
					}
					return SOM;
				}
				if (d[2] >= 84 && d[2] <= 111) {
					return SOM;
				}
				if (d[2] >= 112 && d[2] <= 126) {
					return SHC;
				}
				if (d[2] == 127) {
					return LPCH;
				}
				if (d[2] >= 128 && d[2] <= 255) {
					return SHC;
				}
				return SU;
			}
			if (d[1] == 66) {
				if (d[2] >= 97 && d[2] <= 99) {
					return SHC;
				}
				return SU;
			}
			if (d[1] == 67) {
				return SU;
			}
			return OTHER;
		}
		return OTHER;
	}

	private String getKeyedDigest(String buffer) {
		StringBuffer sb = new StringBuffer();
		try {
			MessageDigest d = MessageDigest.getInstance("MD5");
			byte[] b = d.digest(buffer.getBytes("UTF8"));
			for (int i = 0; i < b.length; i++) {
				sb.append(Integer.toHexString((b[i] & 0xf0) >> 4)
						+ Integer.toHexString(b[i] & 0x0f));
			}
		} catch (Exception e) {
			getLogger().fatalError(e.getLocalizedMessage());
		}
		return sb.toString();
	}

}