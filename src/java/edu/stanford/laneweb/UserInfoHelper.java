package edu.stanford.laneweb;

import org.apache.cocoon.environment.Request;

public interface UserInfoHelper {
	
	public static final String ROLE = UserInfoHelper.class.getName();
	
	public UserInfo getUserInfo(Request request) ;
	
	public void setLdapClient(LdapClient ldapClient);
	
}
