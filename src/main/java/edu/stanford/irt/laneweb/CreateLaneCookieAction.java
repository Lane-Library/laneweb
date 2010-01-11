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

import edu.stanford.irt.laneweb.user.PersistentLoginToken;

public class CreateLaneCookieAction implements Action {

    private Cryptor cryptor;

    @SuppressWarnings("unchecked")
    public Map act(final Redirector redirector, final SourceResolver resolver, final Map objectModel,
            final String source, final Parameters parameters) {
        if (this.cryptor == null) {
            throw new RuntimeException("cryptor is null");
        }
        String sunetId = parameters.getParameter("sunet-id", null);
        String userAgent = parameters.getParameter("user-agent", null);
        HttpServletResponse response = ObjectModelHelper.getResponse(objectModel);
        if (sunetId == null) {
            throw new IllegalArgumentException("sunet-id is null");
        }
        if (userAgent == null) {
            throw new IllegalArgumentException("user-agent is null");
        }
        PersistentLoginToken token = this.cryptor.createLoginToken(sunetId, userAgent.hashCode());
        Cookie laneCookie = new Cookie(LanewebConstants.LANE_COOKIE_NAME, token.getEncryptedValue());
        laneCookie.setPath("/");
        laneCookie.setMaxAge(3600 * 24 * 7 * 2); // cookie is available for 2
        // weeks
        response.addCookie(laneCookie);
        return Collections.emptyMap();
    }

    public void setCryptor(final Cryptor cryptor) {
        if (null == cryptor) {
            throw new IllegalArgumentException("null cryptor");
        }
        this.cryptor = cryptor;
    }
}
