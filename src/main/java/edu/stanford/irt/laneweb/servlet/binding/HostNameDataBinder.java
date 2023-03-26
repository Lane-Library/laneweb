package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.model.Model;

public class HostNameDataBinder implements DataBinder {
	
	private static final String X_FORWARDED_HOST = "X-Forwarded-Host";

    @Override
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
    	 String host = request.getHeader(X_FORWARDED_HOST);
    	 if(host == null) {
    		 host = request.getServerName();
    	 }
    	if (host != null) {
        	model.put(Model.HOST, host);
        }
    }
    
    

}
