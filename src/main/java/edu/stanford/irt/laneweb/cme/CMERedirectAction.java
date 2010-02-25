package edu.stanford.irt.laneweb.cme;

import java.util.HashMap;
import java.util.Map;

import edu.stanford.irt.laneweb.cocoon.AbstractAction;
import edu.stanford.irt.laneweb.model.LanewebObjectModel;

public class CMERedirectAction extends AbstractAction {

    private static final String CME_REDIRECT_KEY = "cme-redirect";

    private static final String ERROR_URL = "/cmeRedirectError.html";

    private static final String PROXY_LINK = "http://laneproxy.stanford.edu/login?url=";

    // TODO: once more vendors, move UTD strings to collection of host objects
    private static final String UTD_CME_STRING =
            "http://www.uptodate.com/online/content/search.do?unid=EMRID&srcsys=epic90710&eiv=2.1.0";
    
    protected Map<String, String> doAct() {
        Map<String, String> result = new HashMap<String, String>(1);
        String host = this.model.getString(LanewebObjectModel.HOST);
        String emrid = this.model.getString(LanewebObjectModel.EMRID);
        if (null != emrid && "uptodate".equalsIgnoreCase(host)) {
            result.put(CME_REDIRECT_KEY, PROXY_LINK + UTD_CME_STRING.replaceFirst("EMRID", emrid));
        } else {
            String queryString = this.model.getString(LanewebObjectModel.QUERY_STRING);
            result.put(CME_REDIRECT_KEY, null == queryString ? ERROR_URL : ERROR_URL + '?' + queryString);
        }
        return result;
    }
}
