package edu.stanford.irt.laneweb.voyager;

import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.Action;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;

public class VoyagerAction implements Action {

    private static final String VOYAGER_KEY = "voyager-url";

    private VoyagerLogin voyagerLogin = null;

    @SuppressWarnings("unchecked")
    public Map act(final Redirector redirector, final SourceResolver sourceResolver, final Map objectModel,
            final String string, final Parameters param) throws Exception {
        String pid = param.getParameter("pid", null);
        String queryString = param.getParameter("query-string", null);
        String univId = param.getParameter("univid", null);
        String url = this.voyagerLogin.getVoyagerURL(univId, pid, queryString);
        Map<String, String> result = new HashMap<String, String>(1);
        result.put(VOYAGER_KEY, url);
        return result;
    }

    public void setVoyagerLogin(final VoyagerLogin voyagerLogin) {
        if (null == voyagerLogin) {
            throw new IllegalArgumentException("null voyagerLogin");
        }
        this.voyagerLogin = voyagerLogin;
    }
}
