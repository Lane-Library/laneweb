package edu.stanford.irt.laneweb.webdash;

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

public class WebdashAction implements Action {

    private static final String RESULT_KEY = "webdash-url";

    private UserDao userDao;

    private WebdashLogin webDashLogin;

    @SuppressWarnings("unchecked")
    public Map act(final Redirector redirector, final SourceResolver sourceResolver, final Map objectModel, final String string, final Parameters param) {
        HttpServletRequest request = ObjectModelHelper.getRequest(objectModel);
        String nonce = request.getParameter("nonce");
        String systemUserId = request.getParameter("system_user_id");
        User user = this.userDao.createOrUpdateUser(request);
        Map<String, String> result = new HashMap<String, String>(1);
        result.put(RESULT_KEY, this.webDashLogin.getWebdashURL(user, nonce, systemUserId));
        return result;
    }

    public void setUserDao(final UserDao userDao) {
        if (null == userDao) {
            throw new IllegalArgumentException("null userDao");
        }
        this.userDao = userDao;
    }

    public void setWebdashLogin(final WebdashLogin webdashLogin) {
        if (null == webdashLogin) {
            throw new IllegalArgumentException("null webdashLogin");
        }
        this.webDashLogin = webdashLogin;
    }
}
