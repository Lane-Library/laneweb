package edu.stanford.laneweb;


import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.AbstractAction;
import java.util.Map;

import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;


public class LoginAction extends AbstractAction {
	
	static String USER_NAME = "USER_NAME";
	
  public Map act (Redirector redirector, 
                  SourceResolver resolver, 
                  Map objectModel, 
                  String source, 
                  Parameters params) {
  
    Request request = ObjectModelHelper.getRequest(objectModel);
    String userWebAuth = (String) request.getAttribute("WEBAUTH_USER");
    
    String userName = request.getRemoteUser();
    if(userName != null)
    	request.getSession().setAttribute(USER_NAME, userName);
    System.out.println("userName "+ userName+ "   webauth "+userWebAuth);
    String url = request.getParameter("u");
    if(url != null)
    {
	    try {
	    	redirector.redirect(true, url);
		} catch (Exception e) {
			getLogger().error(e.getLocalizedMessage());
		}
    }
   
    return null;
  }
}

