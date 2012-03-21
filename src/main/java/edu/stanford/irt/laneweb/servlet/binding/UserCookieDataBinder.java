package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.SunetIdCookieCodec;

public class UserCookieDataBinder implements DataBinder {

	private final int maxCookie = 2;
		
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
        	int cookieNumber = 0;
            for (Cookie cookie : cookies) {
                if (SunetIdCookieCodec.LANE_COOKIE_NAME.equals(cookie.getName())) {
                    model.put(Model.USER_COOKIE, cookie.getValue());
                    if( ++cookieNumber == maxCookie){
                    	break;
                    }
                }
                if (Model.PERSISTENT_LOGIN_EXPIRATION_DATE.equals(cookie.getName())) {
                	try{
                		int ONE_DAY = 1000 * 60 * 60 * 24;
                		long now = new Date().getTime();
                		long cookieValue = Long.valueOf(cookie.getValue());
                		if(cookieValue - now > 0){
                			model.put(Model.PERSISTENT_LOGIN_EXPIRATION_DATE, String.valueOf( Math.round((cookieValue - now) / ONE_DAY)));
                		}
                	}catch (Exception e) {
                		model.put(Model.PERSISTENT_LOGIN_EXPIRATION_DATE,"ERROR");
					}
                	if( ++cookieNumber == maxCookie){
                    	break;
                    }
                }
            }
        }
    }
}
