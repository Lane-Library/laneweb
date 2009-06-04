package edu.stanford.irt.laneweb.cme;

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

public class CMERedirectAction implements Action {

  private Logger logger = Logger.getLogger(CMERedirectAction.class);

  private static final String ERROR_URL = "/cmeRedirectError.html";

  private static final String HOST_PARAM = "host";

  // TODO: once more vendors, move UTD strings to collection of host objects
  private static final String UTD_CME_STRING = "http://www.uptodate.com/online/content/search.do?unid=EMRID&srcsys=epicXXX&eiv=2.1.0";

  @SuppressWarnings("unchecked")
  public Map act(final Redirector redirector, final SourceResolver resolver,
      final Map objectModel, final String source, final Parameters params)
      throws ProcessingException, IOException {
    String redirectUrl = ERROR_URL;
    HttpServletRequest request = ObjectModelHelper.getRequest(objectModel);
    String hostid = request.getParameter(HOST_PARAM);
    String emrid = params.getParameter(User.EMRID, "");
    if (null == emrid) {
      this.logger.error("null emrid");
    }
    if (null == hostid) {
      this.logger.error("null hostid");
    }
    if ("uptodate".equalsIgnoreCase(hostid)) {
      redirectUrl = UTD_CME_STRING.replaceFirst("EMRID", emrid);
    } else {
      this.logger.error("unknown cme host: " + hostid);
    }
    if (ERROR_URL.equals(redirectUrl)) {
      if (null != request.getQueryString()) {
        redirectUrl = redirectUrl + "?" + request.getQueryString();
      }
    }
    if (this.logger.isDebugEnabled()) {
      this.logger.debug("redirecting to cme host: hostid = " + hostid
          + " emrid = " + emrid + " redirectUrl = " + redirectUrl);
    }
    redirector.redirect(false, redirectUrl);
    return null;
  }
}
