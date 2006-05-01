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
			Map objectModel, String source, Parameters params) throws ParameterException, ProcessingException, IOException {

		Request request = ObjectModelHelper.getRequest(objectModel);
		
		String sunetid = (String) request.getAttribute("WEBAUTH_USER");
		if (!"<UNSET>".equals(sunetid)) {
			request.getSession().setAttribute(LanewebInputModule.SUNETID,
					sunetid);
			String url = request.getQueryString();
			if (null != url) {
				String ticket = getTicket(sunetid);
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
