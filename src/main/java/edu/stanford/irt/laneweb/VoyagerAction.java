package edu.stanford.irt.laneweb;

import java.sql.Connection;
import java.util.Map;

import org.apache.avalon.excalibur.datasource.DataSourceComponent;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.ServiceSelector;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.cocoon.acting.ServiceableAction;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;

import edu.stanford.irt.directory.LDAPPerson;

public class VoyagerAction extends ServiceableAction implements ThreadSafe {

    private UserInfoHelper userInfoHelper = null;

    private VoyagerLogin voyagerLogin = null;

    private DataSourceComponent dataSource;

    public Map act(final Redirector redirector, final SourceResolver sourceResolver, final Map objectModel, final String string,
            final Parameters param) throws Exception {
        Request request = ObjectModelHelper.getRequest(objectModel);
        UserInfo userInfo = this.userInfoHelper.getUserInfo(request);
        LDAPPerson ldapPerson = userInfo.getLdapPerson();
        if (null == ldapPerson) {
            throw new RuntimeException("Ldap user not found");
        }
        Connection conn = this.dataSource.getConnection();
        if (null == conn) {
            throw new RuntimeException("voyager database connection not found");
        }
        String url = this.voyagerLogin.initPatronSession(ldapPerson, request, conn);
        if (null != conn) {
          conn.close();
        }
        redirector.globalRedirect(true, url);
        return EMPTY_MAP;
    }

    @Override
    public void service(final ServiceManager manager) throws ServiceException {
        super.service(manager);
        this.voyagerLogin = (VoyagerLogin) manager.lookup(VoyagerLogin.ROLE);
        this.userInfoHelper = (UserInfoHelper) manager.lookup(UserInfoHelper.ROLE);

        ServiceSelector selector = (ServiceSelector) manager.lookup(DataSourceComponent.ROLE + "Selector");
        this.dataSource = (DataSourceComponent) selector.select("voyager");

        this.manager.release(selector);
    }

}

