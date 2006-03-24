package edu.stanford.laneweb;


import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.AbstractAction;
import java.util.Map;

import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;


public class LoginAction extends AbstractAction {
	
	
  public Map act (Redirector redirector, 
                  SourceResolver resolver, 
                  Map objectModel, 
                  String source, 
                  Parameters params) {
  
    Request request = ObjectModelHelper.getRequest(objectModel);
    String sunetid = (String) request.getAttribute("WEBAUTH_USER");
    if(!"<UNSET>".equals(sunetid))
    	request.getSession().setAttribute(LanewebInputModule.SUNETID, sunetid);
    return null;
  }
}

