package edu.stanford.irt.laneweb;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.cocoon.acting.ServiceableAction;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;

import edu.stanford.irt.directory.LDAPPerson;

public class WebdashAction extends ServiceableAction {

    private UserInfoHelper userInfoHelper = null;

    private WebDashLogin webDashLogin = null;

    public Map act(final Redirector redirector, final SourceResolver sourceResolver, final Map objectModel, final String string,
            final Parameters param) throws Exception {

        Request request = ObjectModelHelper.getRequest(objectModel);

        UserInfo userInfo = this.userInfoHelper.getUserInfo(request);
        LDAPPerson ldapPerson = userInfo.getLdapPerson();
        if (null == ldapPerson) {
            throw new RuntimeException("Ladp user not found");
        }
        String url = this.webDashLogin.getEncodedUrl(ldapPerson, request);
        redirector.globalRedirect(true, url);
        return EMPTY_MAP;
    }

    @Override
    public void service(final ServiceManager manager) throws ServiceException {
        super.service(manager);
        this.webDashLogin = (WebDashLogin) manager.lookup(WebDashLogin.ROLE);
        this.userInfoHelper = (UserInfoHelper) manager.lookup(UserInfoHelper.ROLE);

    }

}
