package edu.stanford.irt.laneweb;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.apache.cocoon.environment.Request;

import edu.stanford.irt.directory.LDAPPerson;

public interface WebDashLogin {

    public static final String ROLE = WebDashLogin.class.getName();

    public String getEncodedUrl(LDAPPerson ldapPerson, Request request)
            throws UnsupportedEncodingException, InvalidKeyException,
            NoSuchAlgorithmException;

}
