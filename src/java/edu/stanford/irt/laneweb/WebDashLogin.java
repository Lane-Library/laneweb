package edu.stanford.irt.laneweb;

import edu.stanford.irt.directory.LDAPPerson;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface WebDashLogin {
	
	public static final String ROLE = WebDashLogin.class.getName();
	
	public String getEncodedUrl(LDAPPerson ldapPerson, String nonce) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException;

}
