package edu.stanford.irt.laneweb;

import edu.stanford.irt.SystemException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
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
import org.apache.cocoon.environment.SourceResolver;
import org.apache.commons.codec.DecoderException;

public class PersistentLoginAction implements Action {

    private UserInfoHelper userInfoHelper = null;

    private Cryptor cryptor = null;

    public Map act(final Redirector redirector, final SourceResolver resolver, final Map objectModel, final String source,
	    final Parameters params) throws ProcessingException, IOException, SystemException, InvalidKeyException,
	    IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException,
	    DecoderException {

	Request request = ObjectModelHelper.getRequest(objectModel);
	Response response = ObjectModelHelper.getResponse(objectModel);

	String persistentLogin = request.getParameter("pl");
	String removePersistentLogin = request.getParameter("remove-pl");
	
	UserInfo userInfo = this.userInfoHelper.getUserInfo(request);
	String sunetid = userInfo.getSunetId();

	if (sunetid == null && !"logout".equals(persistentLogin)) {
	    String secureUrl = request.getContextPath().concat("/secure/persistentlogin.html");
	    if(null != request.getQueryString())
		secureUrl = secureUrl.concat("?").concat(request.getQueryString());
	    redirector.globalRedirect(true, secureUrl);
	}
	if ("true".equals(persistentLogin)) {
	    createSunetIdCookie(sunetid, request, response);
	} 
	else if ("logout".equals(persistentLogin) || "true".equals(removePersistentLogin)) {
	    deleteSunetIdCookie(response);
	}
	if ("logout".equals(persistentLogin)) {
	    deleteWebauthCookie(response);
	    HttpSession session = request.getSession(false);
	    if (session != null)
		session.removeAttribute(LanewebConstants.USER_INFO);
	    redirector.globalRedirect(false, "https://weblogin.stanford.edu/logout");
	}
	return null;
    }


    
    private void createSunetIdCookie(String sunetid, Request request, Response response) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IOException, DecoderException
    {
	if(sunetid == null)
	    throw new RuntimeException("sunetId is null");
	if(cryptor == null)
	    throw new RuntimeException("cryptor is null");
	
	String cryptedUserName = this.cryptor.encrypt(sunetid);
	Cookie sunetIdCookie = new Cookie(LanewebConstants.USER_COOKIE_NAME, cryptedUserName);
	sunetIdCookie.setPath("/");
	sunetIdCookie.setMaxAge(3600 * 24 * 7 * 2); //cookie is available for 2 weeks.
        
	String createdDate = String.valueOf( new Date().getTime());
	String cryptedDate = this.cryptor.encrypt(createdDate);
	
	Cookie dateCookie = new Cookie(LanewebConstants.DATE_COOKIE_NAME, cryptedDate);
	dateCookie.setPath("/");
	dateCookie.setMaxAge(3600 * 24 * 7 * 2); //cookie is available for 2 weeks.
        
	String userAgent = request.getHeader("User-Agent");
	
	String encryptedSecurity =  this.cryptor.encrypt(createdDate.concat(sunetid).concat(userAgent));
	Cookie securityCookie = new Cookie(LanewebConstants.SECURITY_COOKIE_NAME, encryptedSecurity);
	securityCookie.setPath("/");
	securityCookie.setMaxAge(3600 * 24 * 7 * 2); //cookie is available for 2 weeks.
        
	response.addCookie(sunetIdCookie);
	response.addCookie(dateCookie);
	response.addCookie(securityCookie);
    }

    public void deleteSunetIdCookie(Response response)
    {
	Cookie sunetIdCookie = new Cookie(LanewebConstants.USER_COOKIE_NAME, null);
	sunetIdCookie.setPath("/");
	sunetIdCookie.setMaxAge(0);
	Cookie dateCookie = new Cookie(LanewebConstants.DATE_COOKIE_NAME, null);
	dateCookie.setPath("/");
	dateCookie.setMaxAge(0);
	Cookie securityCookie = new Cookie(LanewebConstants.SECURITY_COOKIE_NAME, null);
	securityCookie.setPath("/");
	securityCookie.setMaxAge(0);
	response.addCookie(sunetIdCookie);
	response.addCookie(dateCookie);
	response.addCookie(securityCookie);
    }

    public void deleteWebauthCookie(Response response)
    {
	Cookie webauth = new Cookie( "webauth_at", null);
	webauth.setMaxAge(0);
	webauth.setPath("/");
        response.addCookie(webauth);
    }


    public void setUserInfoHelper(final UserInfoHelper userInfoHelper) {
        if (null == userInfoHelper) {
            throw new IllegalArgumentException("null userInfoHelper");
        }
        this.userInfoHelper = userInfoHelper;
    }


    public void setCryptor(final Cryptor cryptor) {
        if (null == cryptor) {
            throw new IllegalArgumentException("null cryptor");
        }
        this.cryptor = cryptor;
    }
    

}
