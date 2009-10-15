package edu.stanford.irt.laneweb;

import java.io.IOException;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.acting.Action;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.log4j.Logger;

public class LoginAction implements Action {

    private static final Logger LOGGER = Logger.getLogger(LoginAction.class);

    @SuppressWarnings("unchecked")
    public Map act(final Redirector redirector, final SourceResolver resolver, final Map objectModel, final String source, final Parameters params)
            throws IOException, ProcessingException {
        String sunetid = params.getParameter("sunetid", null);
        String queryString = params.getParameter("query-string", null);
        String ticket = params.getParameter("ticket", null);
        if (null == sunetid || sunetid.length() == 0) {
            String redirectUrl = "/secure/login.html";
            if (null != queryString && queryString.length() > 0) {
                redirectUrl = redirectUrl + "?" + queryString;
            }
            redirector.redirect(false, redirectUrl);
            return null;
        }
        if (null == ticket || ticket.length() == 0) {
            throw new IllegalArgumentException("null ticket");
        }
        // note: url is not just the url, it is the whole query string ie
        // url=http://...
        if (null == queryString || queryString.length() == 0) {
            return null;
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("redirecting to proxy server: " + " sunetid = " + sunetid + " ticket = " + " url = " + queryString);
        }
        String redirectURL = "http://laneproxy.stanford.edu/login?user=" + sunetid + "&ticket=" + ticket + "&" + queryString;
        redirector.redirect(false, redirectURL);
        return null;
    }
}
