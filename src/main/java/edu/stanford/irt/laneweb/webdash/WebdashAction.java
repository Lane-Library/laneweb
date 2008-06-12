package edu.stanford.irt.laneweb.webdash;

import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.cocoon.acting.Action;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;

import edu.stanford.irt.directory.LDAPPerson;
import edu.stanford.irt.laneweb.UserInfo;
import edu.stanford.irt.laneweb.UserInfoHelper;

public class WebdashAction extends AbstractLogEnabled implements Action,
        Serviceable {

    private UserInfoHelper userInfoHelper;

    private WebdashLogin webDashLogin;

    public void setWebdashLogin(final WebdashLogin webdashLogin) {
        if (null == webdashLogin) {
            throw new IllegalArgumentException("null webdashLogin");
        }
        this.webDashLogin = webdashLogin;
    }

    public void setUserInfoHelper(final UserInfoHelper userInfoHelper) {
        if (null == userInfoHelper) {
            throw new IllegalArgumentException("null userInfoHelper");
        }
        this.userInfoHelper = userInfoHelper;
    }

    public Map act(final Redirector redirector,
            final SourceResolver sourceResolver, final Map objectModel,
            final String string, final Parameters param) {

        Request request = ObjectModelHelper.getRequest(objectModel);

        String nonce = param.getParameter("nonce", null);
        String systemUserId = param.getParameter("system-user-id", null);
        UserInfo userInfo = this.userInfoHelper.getUserInfo(request);
        LDAPPerson person = userInfo.getLdapPerson();
        Map<String, String> result = new HashMap<String, String>(1);
        String url;
        try {
            if (null == systemUserId) {
                url = this.webDashLogin.getRegistrationURL(person, nonce);
            } else {
                url = this.webDashLogin.getLoginURL(person, nonce);
            }
        } catch (IllegalArgumentException e) {
            getLogger().error(e.getMessage(), e);
            url = "/error_webdash.html?error=".concat(e.getMessage());
        }
        result.put("webdash-url", url);
        return result;
    }

    public void service(final ServiceManager manager) throws ServiceException {
        this.webDashLogin = (WebdashLogin) manager.lookup(WebdashLogin.ROLE);
        this.userInfoHelper = (UserInfoHelper) manager
                .lookup(UserInfoHelper.ROLE);

    }

}
