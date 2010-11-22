package edu.stanford.irt.laneweb.voyager;

import java.util.HashMap;
import java.util.Map;

import edu.stanford.irt.laneweb.Model;
import edu.stanford.irt.laneweb.cocoon.AbstractAction;
import edu.stanford.irt.laneweb.util.ModelUtil;

public class VoyagerAction extends AbstractAction {

    private static final String VOYAGER_KEY = "voyager-url";

    private VoyagerLogin voyagerLogin = null;

    @Override
    public Map<String, String> doAct() {
        String pid = ModelUtil.getString(this.model, Model.PID);
        String queryString = ModelUtil.getString(this.model, Model.QUERY_STRING);
        String univId = ModelUtil.getString(this.model, Model.UNIVID);
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
