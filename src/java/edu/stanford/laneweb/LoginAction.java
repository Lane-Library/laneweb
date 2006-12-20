package edu.stanford.laneweb;

import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameterizable;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.acting.AbstractAction;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Date;
import java.util.Map;

import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;

public class LoginAction extends AbstractAction implements Parameterizable {
	
	private String proxyURL;
	private String ezproxyKey;

	public Map act(Redirector redirector, SourceResolver resolver,
			Map objectModel, String source, Parameters params) throws ProcessingException, IOException {

		Request request = ObjectModelHelper.getRequest(objectModel);
		
		String sunetid = (String) request.getAttribute("WEBAUTH_USER");
		
		if (getLogger().isDebugEnabled()) {
			getLogger().debug("user = " + sunetid);
		}
		if (!"<UNSET>".equals(sunetid)) {
			request.getSession().setAttribute(LanewebInputModule.SUNETID,
					sunetid);
			String url = request.getQueryString();
			if (null != url) {
				String ticket = null;
				try {
					ticket = getTicket(sunetid);
				} catch (Exception e) {
					getLogger().error(e.getLocalizedMessage(), e);
				}
				if (getLogger().isDebugEnabled()) {
					getLogger().debug("ticket = " + ticket);
					getLogger().debug("url = " + url);
				}
				String redirectURL = this.proxyURL + "user=" + sunetid + "&ticket=" + ticket + "&" + url;
				redirector.redirect(true, redirectURL);
			}
		}
		return null;
	}

	public void parameterize(Parameters params) throws ParameterException {
		this.proxyURL = params.getParameter("proxy-url","http://laneproxy.stanford.edu/login?");
		this.ezproxyKey = params.getParameter("ezproxy-key");
	}
	


	protected String getTicket(String user) throws Exception {
		String result = null;
		Date now = new Date();
		String packet = "$u" + ((int) (now.getTime() / 1000));
		result = URLEncoder.encode(getKeyedDigest(ezproxyKey + user
				+ packet)
				+ packet, "UTF8");
		return result;
	}
	


	private String getKeyedDigest(String buffer) throws Exception {
		StringBuffer sb = new StringBuffer();
		MessageDigest d = MessageDigest.getInstance("MD5");
		byte[] b = d.digest(buffer.getBytes("UTF8"));
		for (int i = 0; i < b.length; i++) {
			sb.append(Integer.toHexString((b[i] & 0xf0) >> 4)
					+ Integer.toHexString(b[i] & 0x0f));
		}
		return sb.toString();
	}
}
