package edu.stanford.irt.laneweb.servlet.mvc;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.laneweb.model.Model;


@Controller
public class IsUserActiveController {


    @ResponseBody
	@RequestMapping(value = "/user/active")    
    public String  isUserActive(final HttpServletRequest request) {
    	Boolean isActiveSunetID = (Boolean) request.getSession().getAttribute(Model.IS_ACTIVE_SUNETID);
    	if(null == isActiveSunetID){
    		return "false";
    	}
    	return String.valueOf(isActiveSunetID);	
    }
}


