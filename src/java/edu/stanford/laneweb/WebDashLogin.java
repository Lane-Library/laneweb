package edu.stanford.laneweb;

import edu.stanford.irt.directory.LDAPPerson;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface WebDashLogin {
	
	public static final String ROLE = WebDashLogin.class.getName();
	
	public String getEncodedUrl(LDAPPerson ldapPerson) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException;

}
