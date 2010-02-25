package edu.stanford.irt.laneweb.proxy;

import java.util.HashMap;
import java.util.Map;

import edu.stanford.irt.laneweb.cocoon.AbstractAction;
import edu.stanford.irt.laneweb.model.LanewebObjectModel;

//$Id$
public class ProxyCredentialAction extends AbstractAction {

    private static final String PROXY_REDIRECT_KEY = "proxy-redirect";

    @Override
    protected Map<String, String> doAct() {
        String queryString = this.model.getString(LanewebObjectModel.QUERY_STRING);
        if (queryString == null) {
            throw new IllegalStateException("null query-string");
        }
        Map<String, String> result = new HashMap<String, String>(1);
        String sunetid = this.model.getString(LanewebObjectModel.SUNETID);
        if (sunetid == null) {
            String basePath = this.model.getString(LanewebObjectModel.BASE_PATH);
            result.put(PROXY_REDIRECT_KEY, basePath + "/secure/apps/proxy/credential?" + queryString);
        } else {
            String ticket = this.model.getString(LanewebObjectModel.TICKET);
            result.put(PROXY_REDIRECT_KEY, "http://laneproxy.stanford.edu/login?user=" + sunetid + "&ticket=" + ticket
                    + "&" + queryString);
        }
        return result;
    }
}
