package edu.stanford.laneweb;

import edu.stanford.irt.directory.LDAPPerson;

import java.util.Map;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.ServiceableAction;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.SourceResolver;

public class WebdashAction extends ServiceableAction implements  Initializable{


	WebDashLogin webDashLogin = null;
	
	
	public Map act(Redirector redirector, SourceResolver sourceResolver, Map objectModel, String string, Parameters param) throws Exception {
		
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
		LDAPPerson ldapPerson = userInfo.getLdapPerson(); 
		if(ldapPerson == null)
			throw new RuntimeException("Ladp user not found");

		String url = webDashLogin.getEncodedUrl(ldapPerson);
		redirector.globalRedirect(true, url);
		return null;
	}


	public void initialize() throws Exception {
		webDashLogin = (WebDashLogin) this.manager.lookup(WebDashLogin.ROLE);
	}
	
	
	
}
