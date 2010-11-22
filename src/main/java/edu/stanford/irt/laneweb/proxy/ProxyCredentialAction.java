package edu.stanford.irt.laneweb.proxy;

import java.util.HashMap;
import java.util.Map;

import edu.stanford.irt.laneweb.Model;
import edu.stanford.irt.laneweb.cocoon.AbstractAction;
import edu.stanford.irt.laneweb.util.ModelUtil;

// $Id$
public class ProxyCredentialAction extends AbstractAction {

    private static final String PROXY_REDIRECT_KEY = "proxy-redirect";

    @Override
    protected Map<String, String> doAct() {
        String queryString = ModelUtil.getString(this.model, Model.QUERY_STRING);
        if (queryString == null) {
            throw new IllegalStateException("null query-string");
        }
        Map<String, String> result = new HashMap<String, String>(1);
        String sunetid = ModelUtil.getString(this.model, Model.SUNETID);
        if (sunetid == null) {
            String basePath = ModelUtil.getString(this.model, Model.BASE_PATH);
            result.put(PROXY_REDIRECT_KEY, basePath + "/secure/apps/proxy/credential?" + queryString);
        } else {
            Ticket ticket = ModelUtil.getObject(this.model, Model.TICKET, Ticket.class);
            if (ticket == null) {
                throw new IllegalStateException("null ticket with sunetid");
            }
            result.put(PROXY_REDIRECT_KEY, "http://laneproxy.stanford.edu/login?user=" + sunetid + "&ticket=" + ticket
                    + "&" + queryString);
        }
        return result;
    }
}
