package edu.stanford.irt.laneweb;

import java.io.IOException;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.acting.Action;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.log4j.Logger;

import edu.stanford.irt.SystemException;

public class LoginAction implements Action {

    private Logger logger = Logger.getLogger(LoginAction.class);

    private UserInfoHelper userInfoHelper = null;

    public Map act(final Redirector redirector, final SourceResolver resolver, final Map objectModel, final String source,
            final Parameters params) throws ProcessingException, IOException, SystemException {
        Request request = ObjectModelHelper.getRequest(objectModel);
        UserInfo userInfo = this.userInfoHelper.getUserInfo(request);
        String sunetid = userInfo.getSunetId();
        if (sunetid == null) {
            throw new ProcessingException("null sunetid");
        }
        String ticket = userInfo.getTicket().toString();
        if (ticket == null) {
            throw new ProcessingException("null ticket");
        }
        // note: url is not just the url, it is the whole query string ie
        // url=http://...
        String url = request.getQueryString();
        if (url == null) {
            throw new ProcessingException("null url");
        }
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("redirecting to proxy server: " + " sunetid = " + sunetid + " ticket = " + " url = " + url);
        }
        String redirectURL = "http://laneproxy.stanford.edu/login?user=" + sunetid + "&ticket=" + ticket + "&" + url;
        redirector.redirect(true, redirectURL);
        return null;
    }

    public void setUserInfoHelper(final UserInfoHelper userInfoHelper) {
        if (null == userInfoHelper) {
            throw new IllegalArgumentException("null userInfoHelper");
        }
        this.userInfoHelper = userInfoHelper;
    }

}
