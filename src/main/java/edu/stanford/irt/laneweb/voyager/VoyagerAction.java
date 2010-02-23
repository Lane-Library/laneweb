package edu.stanford.irt.laneweb.voyager;

import java.util.HashMap;
import java.util.Map;

import edu.stanford.irt.laneweb.cocoon.AbstractAction;
import edu.stanford.irt.laneweb.model.LanewebObjectModel;

public class VoyagerAction extends AbstractAction {

    private static final String VOYAGER_KEY = "voyager-url";

    private VoyagerLogin voyagerLogin = null;

    public Map doAct() {
        String pid = this.model.getString(LanewebObjectModel.PID);
        String queryString = this.model.getString(LanewebObjectModel.QUERY_STRING);
        String univId = this.model.getString(LanewebObjectModel.UNIVID);
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
