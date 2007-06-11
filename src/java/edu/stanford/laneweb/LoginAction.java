package edu.stanford.laneweb;

import java.io.IOException;
import java.util.Map;

import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameterizable;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.acting.ServiceableAction;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;

import edu.stanford.irt.SystemException;

public class LoginAction extends ServiceableAction implements Parameterizable  {
	
	private String proxyURL;
	private UserInfoHelper userInfoHelper = null;
	
	public Map act(Redirector redirector, SourceResolver resolver,
			Map objectModel, String source, Parameters params) throws ProcessingException, IOException, SystemException {
		Request request = ObjectModelHelper.getRequest(objectModel);
		UserInfo userInfo = userInfoHelper.getUserInfo(request); 
		String sunetid = userInfo.getSunetId();
		if (sunetid == null) {
			throw new ProcessingException("null sunetid");
		}
		String ticket = userInfo.getTicket().toString();
		if (ticket == null) {
			throw new ProcessingException("null ticket");
		}
		//note:  url is not just the url, it is the whole query string ie url=http://...
		String url = request.getQueryString();
		if (url == null) {
			throw new ProcessingException("null url");
		}
		if (getLogger().isDebugEnabled()) {
			getLogger().debug("redirecting to proxy server: " +
					" sunetid = " + sunetid + 
					" ticket = " + 
					" url = " + url);
		}
		String redirectURL = this.proxyURL + "user=" + sunetid + "&ticket=" + ticket + "&" + url;
		redirector.redirect(true, redirectURL);
		return null;
	}

	public void parameterize(Parameters params) throws ParameterException {
		this.proxyURL = params.getParameter("proxy-url","http://laneproxy.stanford.edu/login?");
	}
	

	@Override
	public void service(ServiceManager manager) throws ServiceException {
		super.service(manager);
		userInfoHelper = (UserInfoHelper) manager.lookup(UserInfoHelper.ROLE);
	}


}
