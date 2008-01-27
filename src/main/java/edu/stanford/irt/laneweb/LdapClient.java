package edu.stanford.irt.laneweb;

import edu.stanford.irt.directory.LDAPPerson;

public interface LdapClient {
    
    public static final String ROLE = LdapClient.class.getName();

    LDAPPerson getLdapPerson(String sunetId);
}
