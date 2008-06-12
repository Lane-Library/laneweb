package edu.stanford.irt.laneweb.webdash;

import edu.stanford.irt.directory.LDAPPerson;

public interface WebdashLogin {

    public static final String ROLE = WebdashLogin.class.getName();

    String getQueryString(LDAPPerson person, String nonce);

}
