package edu.stanford.irt.laneweb.voyager;

import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.cocoon.acting.Action;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;

import edu.stanford.irt.directory.LDAPPerson;
import edu.stanford.irt.laneweb.UserInfo;
import edu.stanford.irt.laneweb.UserInfoHelper;

public class VoyagerAction implements Action, Serviceable, ThreadSafe {

    private static final String VOYAGER_KEY = "voyager-url";

    private UserInfoHelper userInfoHelper = null;

    private VoyagerLogin voyagerLogin = null;

    public Map act(final Redirector redirector,
            final SourceResolver sourceResolver, final Map objectModel,
            final String string, final Parameters param) throws Exception {

        Request request = ObjectModelHelper.getRequest(objectModel);
        String pid = request.getParameter("PID");
        String queryString = request.getQueryString();
        UserInfo userInfo = this.userInfoHelper.getUserInfo(request);
        LDAPPerson person = userInfo.getPerson();

        String url = this.voyagerLogin.getVoyagerURL(person, pid, queryString);
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

    public void setUserInfoHelper(final UserInfoHelper userInfoHelper) {
        if (null == userInfoHelper) {
            throw new IllegalArgumentException("null userInfoHelper");
        }
        this.userInfoHelper = userInfoHelper;
    }

    public void service(final ServiceManager manager) throws ServiceException {
        setVoyagerLogin((VoyagerLogin) manager.lookup(VoyagerLogin.ROLE));
        setUserInfoHelper((UserInfoHelper) manager.lookup(UserInfoHelper.ROLE));
    }

}
