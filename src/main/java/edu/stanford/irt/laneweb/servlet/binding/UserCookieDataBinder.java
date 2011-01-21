package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.SunetIdCookieCodec;

public class UserCookieDataBinder implements DataBinder {

    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (SunetIdCookieCodec.LANE_COOKIE_NAME.equals(cookie.getName())) {
                    model.put(Model.USER_COOKIE, cookie.getValue());
                    break;
                }
            }
        }
    }
}
