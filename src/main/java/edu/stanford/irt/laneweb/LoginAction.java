package edu.stanford.irt.laneweb;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.acting.Action;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Response;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.commons.codec.DecoderException;
import org.apache.log4j.Logger;

import edu.stanford.irt.SystemException;

public class LoginAction implements Action {

    private Logger logger = Logger.getLogger(LoginAction.class);

    private UserInfoHelper userInfoHelper = null;

    private Cryptor encryptor = null;
    
    public Map act(final Redirector redirector, final SourceResolver resolver, final Map objectModel, final String source,
            final Parameters params) throws ProcessingException, IOException, SystemException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, DecoderException {
        Request request = ObjectModelHelper.getRequest(objectModel);
        Response response = ObjectModelHelper.getResponse(objectModel);
        
        UserInfo userInfo = this.userInfoHelper.getUserInfo(request);
        String sunetid = userInfo.getSunetId();
        if (sunetid == null) {
            throw new ProcessingException("null sunetid");
        }
        String ticket = userInfo.getTicket().toString();
        if (ticket == null) {
            throw new ProcessingException("null ticket");
        }
        String removePersistentLogin = request.getParameter("remove-pl");
        String persistentLogin = request.getParameter("pl");
        if(persistentLogin != null || removePersistentLogin != null)
        {
            Cookie cookie = null;
            if("true".equals(persistentLogin)) {
               String value =  this.encryptor.encrypt(sunetid);
               cookie = new Cookie( LanewebConstants.USER_COOKIE_NAME, value);
               cookie.setMaxAge(3600 * 24 * 7 * 2); //cookie is available for 2 weeks.
               cookie.setPath("/");
               response.addCookie(cookie);
            }
            else if("logout".equals(persistentLogin) || "true".equals(removePersistentLogin))
            {
        	cookie = new Cookie( LanewebConstants.USER_COOKIE_NAME, null);
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
            if("logout".equals(persistentLogin)) {
        	HttpSession session = request.getSession(false);
        	if(session != null)
        	    session.removeAttribute(LanewebConstants.USER_INFO);
        	redirector.redirect(false, "https://weblogin.stanford.edu/logout");
            }
            return null;
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

    public void setEncryptor(final Cryptor cryptor)
    {
	if(null == cryptor)
	    throw new IllegalArgumentException("null cryptor");
	this.encryptor = cryptor;
    }
}
