package edu.stanford.laneweb;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.Session;

public class UserInfoHelperImpl extends AbstractLogEnabled implements UserInfoHelper, ThreadSafe, Initializable{

	LdapClient ldapClient;
	
	public UserInfo getUserInfo(Request request) {
		
		UserInfo userInfo = (UserInfo) request.getAttribute(LanewebConstants.USER_INFO);
		if (userInfo == null) {
			Session session = request.getSession(true);
			userInfo = (UserInfo) session.getAttribute(LanewebConstants.USER_INFO);
			if (userInfo == null) {
				userInfo = new UserInfo();
			session.setAttribute(LanewebConstants.USER_INFO, userInfo);
			}
			request.setAttribute(LanewebConstants.USER_INFO, userInfo);
		}
		if(userInfo.getLdapPerson() == null)
			setUserInfo(userInfo,request);
		
		return userInfo;
	}
		
		
		
  private void setUserInfo(UserInfo userInfo, Request request)
  {
	if (userInfo.getAffiliation() == null) {
		String ip = request.getRemoteAddr();
		// mod_proxy puts the real remote address in an x-forwarded-for
		// header
		// Loa:d balancer also does this
		String header = request.getHeader(LanewebConstants.X_FORWARDED_FOR);
		if (header != null) {
			ip = header;
		}
		try {
			userInfo.setAffiliation(Affiliation.getAffiliationForIP(ip));
		} catch (Exception e) {
			userInfo.setAffiliation(Affiliation.ERR);
		}
	}
	if (userInfo.getSunetId() == null) {
		String requestSunetId = (String) request
				.getAttribute(LanewebConstants.WEBAUTH_USER);
		if (requestSunetId != null && !LanewebConstants.UNSET.equals(requestSunetId)  ) {
			userInfo.setSunetId( requestSunetId);
			userInfo.setLdapPerson( ldapClient.getLdapPerson(requestSunetId));
		}
	}
	if (null != request.getParameter(LanewebConstants.PROXY_LINKS)) {
		userInfo.setProxyLinks( new Boolean(request
				.getParameter(LanewebConstants.PROXY_LINKS)));
	}
	if (userInfo.getSunetId() != null) {
		userInfo.setTicket( new Ticket(userInfo.getSunetId()));
	}
  }

	
	public void initialize() throws Exception {
		ldapClient = (LdapClient) new LdapClientImpl();
	}



	public void setLdapClient(LdapClient ldapClient) {
		this.ldapClient =  ldapClient;
	}

	
	
	
}
