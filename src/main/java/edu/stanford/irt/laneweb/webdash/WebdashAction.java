package edu.stanford.irt.laneweb.webdash;

import java.util.HashMap;
import java.util.Map;

import edu.stanford.irt.laneweb.cocoon.AbstractAction;
import edu.stanford.irt.laneweb.model.LanewebObjectModel;

public class WebdashAction extends AbstractAction {

    private static final String RESULT_KEY = "webdash-url";

    private WebdashLogin webDashLogin;

    public Map<String, String> doAct() {
        String nonce = this.model.getString(LanewebObjectModel.NONCE);
        String systemUserId = this.model.getString(LanewebObjectModel.SYSTEM_USER_ID);
        String sunetId = this.model.getString(LanewebObjectModel.SUNETID);
        String name = this.model.getString(LanewebObjectModel.NAME);
        String affiliation = this.model.getString(LanewebObjectModel.AFFILIATION);
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
