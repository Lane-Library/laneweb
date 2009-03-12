package edu.stanford.irt.laneweb;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.acting.Action;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.log4j.Logger;

import edu.stanford.irt.laneweb.user.User;
import edu.stanford.irt.laneweb.user.UserDao;

public class LoginAction implements Action {

    private Logger logger = Logger.getLogger(LoginAction.class);

    private UserDao userDao;

    @SuppressWarnings("unchecked")
    public Map act(final Redirector redirector, final SourceResolver resolver, final Map objectModel, final String source, final Parameters params)
            throws ProcessingException, IOException {
        HttpServletRequest request = ObjectModelHelper.getRequest(objectModel);
        User user = this.userDao.createOrUpdateUser(request);
        String sunetid = user.getSunetId();
        if (sunetid == null) {
            String redirectUrl = "/secure/login.html";
            if (null != request.getQueryString()) {
                redirectUrl = redirectUrl + "?" + request.getQueryString();
            }
            redirector.redirect(false, redirectUrl);
            return null;
        }
        String ticket = user.getTicket().toString();
        if (ticket == null) {
            throw new ProcessingException("null ticket");
        }
        // note: url is not just the url, it is the whole query string ie
        // url=http://...
        String url = request.getQueryString();
        if (url == null) {
            return null;
        }
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("redirecting to proxy server: " + " sunetid = " + sunetid + " ticket = " + " url = " + url);
        }
        String redirectURL = "http://laneproxy.stanford.edu/login?user=" + sunetid + "&ticket=" + ticket + "&" + url;
        redirector.redirect(false, redirectURL);
        return null;
    }

    public void setUserDao(final UserDao userDao) {
        if (null == userDao) {
            throw new IllegalArgumentException("null userDao");
        }
        this.userDao = userDao;
    }
}
