package edu.stanford.irt.laneweb.webdash;

import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.Action;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;

public class WebdashAction implements Action {

    private static final String RESULT_KEY = "webdash-url";

    private WebdashLogin webDashLogin;

    @SuppressWarnings("unchecked")
    public Map act(final Redirector redirector, final SourceResolver sourceResolver, final Map objectModel,
            final String string, final Parameters param) {
        String nonce = param.getParameter("nonce", null);
        String systemUserId = param.getParameter("system-user-id", null);
        String sunetId = param.getParameter("sunet-id", null);
        String name = param.getParameter("name", null);
        String affiliation = param.getParameter("affiliation", null);
        Map<String, String> result = new HashMap<String, String>(1);
        result.put(RESULT_KEY, this.webDashLogin.getWebdashURL(sunetId, name, affiliation, nonce, systemUserId));
        return result;
    }

    public void setWebdashLogin(final WebdashLogin webdashLogin) {
        if (null == webdashLogin) {
            throw new IllegalArgumentException("null webdashLogin");
        }
        this.webDashLogin = webdashLogin;
    }
}
