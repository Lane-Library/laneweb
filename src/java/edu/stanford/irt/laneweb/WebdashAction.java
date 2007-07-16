package edu.stanford.irt.laneweb;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.cocoon.acting.ServiceableAction;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;

import edu.stanford.irt.directory.LDAPPerson;

public class WebdashAction extends ServiceableAction{

	UserInfoHelper userInfoHelper = null;
	WebDashLogin webDashLogin = null;
	
	
	public Map act(Redirector redirector, SourceResolver sourceResolver, Map objectModel, String string, Parameters param) throws Exception {
		
		Request request = ObjectModelHelper.getRequest(objectModel);
	
		UserInfo userInfo = userInfoHelper.getUserInfo(request);
		
		LDAPPerson ldapPerson = userInfo.getLdapPerson(); 
		if( userInfo == null ||  userInfo.getLdapPerson() == null)
			throw new RuntimeException("Ladp user not found");

		String url = webDashLogin.getEncodedUrl(ldapPerson);
		redirector.globalRedirect(true, url);
		return null;
	}

	

	@Override
	public void service(ServiceManager manager) throws ServiceException {
		super.service(manager);
		webDashLogin = (WebDashLogin) manager.lookup(WebDashLogin.ROLE);
		userInfoHelper = (UserInfoHelper) manager.lookup(UserInfoHelper.ROLE);

	}


	
	
}
