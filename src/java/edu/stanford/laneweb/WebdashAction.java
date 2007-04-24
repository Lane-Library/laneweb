package edu.stanford.laneweb;

import edu.stanford.irt.directory.LDAPPerson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameterizable;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;
import org.apache.cocoon.environment.SourceResolver;

public class WebdashAction extends AbstractAction implements Parameterizable, Initializable{

	String url;
	String groupName;
	String dateFormat;
	String webAuthLdapSeparator;
	String groupKey;
	Mac mac ;
	
	public Map act(Redirector redirector, SourceResolver sourceResolver, Map objectModel, String string, Parameters parameter) throws Exception {
		Request request = ObjectModelHelper.getRequest(objectModel);
		UserInfo userInfo = (UserInfo) request.getAttribute(LanewebConstants.USER_INFO);
		if (userInfo == null) {
			Session session = request.getSession(true);
			userInfo = (UserInfo) session.getAttribute(LanewebConstants.USER_INFO);
			if (userInfo == null) {
				userInfo = new UserInfo();
				session.setAttribute(LanewebConstants.USER_INFO, userInfo);
			}
			request.setAttribute(LanewebConstants.USER_INFO, userInfo);
			userInfo.update(objectModel, getLogger());
			if(userInfo.getLdapPerson() == null)
				throw new RuntimeException("Ladp user not found");
		}
			String mail = URLEncoder.encode( userInfo.getLdapPerson().getMail() , "UTF-8"); 
			String fullName = URLEncoder.encode( userInfo.getLdapPerson().getDisplayName(), "UTF-8");
			String affiliation =  getSubGroup(userInfo.getLdapPerson());
			StringBuffer parameters = new StringBuffer();
			parameters.append("email=");
			parameters.append(mail);
			parameters.append("&enddate=".concat(getEndDate()));
			parameters.append("&fullname=".concat(fullName));
			parameters.append("&group=".concat(groupName));
			parameters.append("&subgroup=".concat(affiliation));
			String token = getToken(parameters.toString());
			redirector.redirect(true, url.concat(parameters.toString()).concat("&token=").concat(token));
		return null;
		
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
			result = URLEncoder.encode(affiliation[0] ,"UTF-8");
		}
		else
		{
			throw new RuntimeException("Ldap person : ".concat(ldapPerson.getDisplayName()).concat( "  don't have a affiliation"));	
		}
		return result;
	}
	
	
	private String getEndDate()
	{
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		GregorianCalendar endDate = new GregorianCalendar();
		endDate.add(Calendar.YEAR, 1);
		return sdf.format(endDate.getTime());
	}

	
	public void parameterize(Parameters param) throws ParameterException {
		this.url = param.getParameter("webdashURL");
		this.groupKey = param.getParameter("groupKey");
		this.groupName = param.getParameter("groupName");
		this.dateFormat = param.getParameter("dateFormat");
		this.webAuthLdapSeparator = param.getParameter("webAuthLdapSeparator");
	}

	
	
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

	
}
