package edu.stanford.irt.laneweb.cme;

import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.Action;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import edu.stanford.irt.laneweb.user.User;

public class CMERedirectAction implements Action {

    private static final String CME_REDIRECT_KEY = "cme-redirect";

    private static final String ERROR_URL = "/cmeRedirectError.html";

    private static final String HOST_PARAM = "host";

    private static final Logger LOGGER = Logger.getLogger(CMERedirectAction.class);

    private static final String PROXY_LINK = "http://laneproxy.stanford.edu/login?url=";

    private static final String QUERY_STRING = "query-string";

    // TODO: once more vendors, move UTD strings to collection of host objects
    private static final String UTD_CME_STRING = "http://www.uptodate.com/online/content/search.do?unid=EMRID&srcsys=epic90710&eiv=2.1.0";

    @SuppressWarnings("unchecked")
    public Map act(final Redirector redirector, final SourceResolver resolver, final Map objectModel, final String source, final Parameters params) {
        Map result = new HashMap();
        String host = params.getParameter(HOST_PARAM, null);
        String emrid = params.getParameter(User.EMRID, null);
        if (null != emrid && emrid.length() > 0 && "uptodate".equalsIgnoreCase(host)) {
            result.put(CME_REDIRECT_KEY, PROXY_LINK + UTD_CME_STRING.replaceFirst("EMRID", emrid));
        } else {
            String queryString = params.getParameter(QUERY_STRING, null);
            result.put(CME_REDIRECT_KEY, null == queryString ? ERROR_URL : ERROR_URL + '?' + queryString);
            if (LOGGER.isEnabledFor(Level.ERROR)) {
                StringBuilder message = new StringBuilder();
                if (null == emrid || emrid.length() == 0) {
                    message.append("null emrid");
                }
                if (null == host || host.length() == 0) {
                    if (message.length() > 0) {
                        message.append(", ");
                    }
                    message.append("null hostid");
                } else if (!"uptodate".equals(host)) {
                    if (message.length() > 0) {
                        message.append(", ");
                    }
                    message.append("unknown host: " + host);
                }
                LOGGER.error(message.toString());
            }
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("redirecting to cme host: host = " + host + " emrid = " + emrid + " redirectUrl = " + result.get(CME_REDIRECT_KEY));
        }
        return result;
    }
}
