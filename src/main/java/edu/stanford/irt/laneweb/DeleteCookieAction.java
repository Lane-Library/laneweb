package edu.stanford.irt.laneweb;

import java.util.Collections;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.Action;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;

public class DeleteCookieAction implements Action {

    @SuppressWarnings("unchecked")
    public Map act(final Redirector redirector, final SourceResolver resolver, final Map objectModel,
            final String source, final Parameters parameters) {
        String cookieName = parameters.getParameter("cookie-name", null);
        if (null == cookieName || cookieName.length() == 0) {
            throw new IllegalArgumentException("no cookie-name");
        }
        HttpServletResponse response = ObjectModelHelper.getResponse(objectModel);
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return Collections.emptyMap();
    }
}
