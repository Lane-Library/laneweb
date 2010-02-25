package edu.stanford.irt.laneweb.webdash;

import java.util.HashMap;
import java.util.Map;

import edu.stanford.irt.laneweb.cocoon.AbstractAction;
import edu.stanford.irt.laneweb.model.Model;

public class WebdashAction extends AbstractAction {

    private static final String RESULT_KEY = "webdash-url";

    private WebdashLogin webDashLogin;

    public Map<String, String> doAct() {
        String nonce = this.model.getString(Model.NONCE);
        String systemUserId = this.model.getString(Model.SYSTEM_USER_ID);
        String sunetId = this.model.getString(Model.SUNETID);
        String name = this.model.getString(Model.NAME);
        String affiliation = this.model.getString(Model.AFFILIATION);
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
