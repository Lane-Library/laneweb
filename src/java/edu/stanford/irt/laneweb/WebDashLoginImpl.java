package edu.stanford.irt.laneweb;

import edu.stanford.irt.directory.LDAPPerson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameterizable;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.http.HttpRequest;

public class WebDashLoginImpl extends AbstractLogEnabled implements WebDashLogin, ThreadSafe, Parameterizable, Initializable {

	String loginUrl;
	String registrationUrl;
	String groupName;
	String groupKey;
	Mac mac ;
	
	
	
	public void initialize() throws Exception {
		try {
			 SecretKey key = new SecretKeySpec(groupKey.getBytes(), "HmacSHA1");
			 mac = Mac.getInstance(key.getAlgorithm());
			 mac.init(key);
		}
		catch (NoSuchAlgorithmException e) {
			getLogger().error(e.getMessage(),e);
		} catch (InvalidKeyException e) {
			getLogger().error(e.getMessage(),e);
		} catch (IllegalStateException e) {
			getLogger().error(e.getMessage(),e);
		}  
	}


	public String getEncodedUrl(LDAPPerson ldapPerson, Request request) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {
		String mail = URLEncoder.encode( ldapPerson.getMail() , "UTF-8"); 
		String fullName = URLEncoder.encode( ldapPerson.getDisplayName(), "UTF-8");
		String userId = URLEncoder.encode(ldapPerson.getUId(),"UTF-8");
		String affiliation =  getSubGroup(ldapPerson);
		String nonce = request.getParameter("nonce");
		StringBuffer parameters = new StringBuffer();
		parameters.append("email=");
		parameters.append(mail);
		parameters.append("&fullname=".concat(fullName.replace("+", "%20")));
		parameters.append("&nonce=");
		if(nonce != null)
			parameters.append(nonce);	
		parameters.append("&subgroup=".concat(affiliation));
		parameters.append("&system_short_name=".concat(groupName));
		parameters.append("&system_user_id=".concat(userId.replace("+", "%20")));
		String token = getToken(parameters.toString());
		if( request.getParameter("system_user_id") != null) // webdash past us system_user_id in the url for a login other wise is a registration 
			return loginUrl.concat(parameters.toString()).concat("&token=").concat(token);
		else
			return registrationUrl.concat(parameters.toString()).concat("&token=").concat(token);
	}	
	
	private String getToken(String string) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException
	{
		 string = string.replace("+", "%20");
	     byte[] utf8 = string.getBytes("UTF8");
	     byte[] b = mac.doFinal(utf8);
	     StringBuffer sb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			sb.append(Integer.toHexString((b[i] & 0xf0) >> 4)
					+ Integer.toHexString(b[i] & 0x0f));
		}
		return sb.toString();
	
	}
	
	
	private String getSubGroup(LDAPPerson ldapPerson) throws UnsupportedEncodingException
	{
		//value coming from LDAP and afflialtion may have multiple value e.i stanford:staff 		
		String result = null;
		String[] affiliations = ldapPerson.getAffilation();
		if(affiliations.length >0)
		{
			String[] affiliation = affiliations[0].split(":");
			if(affiliation.length >0)
				result = URLEncoder.encode(affiliation[1] ,"UTF-8");
			else
				result =  URLEncoder.encode(affiliation[0] ,"UTF-8");
		}
		else
		{
			throw new RuntimeException("Ldap person : ".concat(ldapPerson.getDisplayName()).concat( "  don't have a affiliation"));	
		}
		return result;
	}
	
	


	public void parameterize(Parameters param) throws ParameterException {
		this.registrationUrl = param.getParameter("webdashRegistrationURL");
		this.loginUrl = param.getParameter("webdashLoginURL");
		this.groupKey = param.getParameter("groupKey");
		this.groupName = param.getParameter("groupName");
		
	}

	



}
