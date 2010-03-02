package edu.stanford.irt.laneweb.webdash;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.irt.laneweb.cocoon.AbstractAction;
import edu.stanford.irt.laneweb.model.Model;

public class WebdashAction extends AbstractAction {

    private static final String ERROR_URL = "/webdashError.html";

    private static final Logger LOGGER = LoggerFactory.getLogger(WebdashAction.class);

    private static final String RESULT_KEY = "webdash-url";

    private WebdashLogin webDashLogin;

    @Override
    public Map<String, String> doAct() {
        String nonce = this.model.getString(Model.NONCE);
        String systemUserId = this.model.getString(Model.SYSTEM_USER_ID);
        String sunetId = this.model.getString(Model.SUNETID);
        String name = this.model.getString(Model.NAME);
        String affiliation = this.model.getString(Model.AFFILIATION);
        Map<String, String> result = new HashMap<String, String>(1);
        try {
            result.put(RESULT_KEY, this.webDashLogin.getWebdashURL(sunetId, name, affiliation, nonce, systemUserId));
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
            result.put(RESULT_KEY, ERROR_URL);
        }
        return result;
    }

    public void setWebdashLogin(final WebdashLogin webdashLogin) {
        if (null == webdashLogin) {
            throw new IllegalArgumentException("null webdashLogin");
        }
        this.webDashLogin = webdashLogin;
    }
}
