package edu.stanford.irt.laneweb.voyager;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.Action;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;

import edu.stanford.irt.laneweb.user.User;
import edu.stanford.irt.laneweb.user.UserDao;

public class VoyagerAction implements Action {

    private static final String VOYAGER_KEY = "voyager-url";

    private UserDao userDao = null;

    private VoyagerLogin voyagerLogin = null;

    @SuppressWarnings("unchecked")
    public Map act(final Redirector redirector, final SourceResolver sourceResolver, final Map objectModel, final String string, final Parameters param)
            throws Exception {
        HttpServletRequest request = ObjectModelHelper.getRequest(objectModel);
        String pid = request.getParameter("PID");
        String queryString = request.getQueryString();
        User user = this.userDao.createOrUpdateUser(request);
        String url = this.voyagerLogin.getVoyagerURL(user, pid, queryString);
        Map<String, String> result = new HashMap<String, String>(1);
        result.put(VOYAGER_KEY, url);
        return result;
    }

    public void setUserDao(final UserDao userDao) {
        if (null == userDao) {
            throw new IllegalArgumentException("null userDao");
        }
        this.userDao = userDao;
    }

    public void setVoyagerLogin(final VoyagerLogin voyagerLogin) {
        if (null == voyagerLogin) {
            throw new IllegalArgumentException("null voyagerLogin");
        }
        this.voyagerLogin = voyagerLogin;
    }
}
