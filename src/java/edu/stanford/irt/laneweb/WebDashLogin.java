package edu.stanford.irt.laneweb;

import edu.stanford.irt.directory.LDAPPerson;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.apache.cocoon.environment.Request;

public interface WebDashLogin {
	
	public static final String ROLE = WebDashLogin.class.getName();
	
	public String getEncodedUrl(LDAPPerson ldapPerson, Request request) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException;

}
