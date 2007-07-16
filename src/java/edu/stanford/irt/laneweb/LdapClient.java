package edu.stanford.irt.laneweb;

import edu.stanford.irt.directory.LDAPPerson;

public interface LdapClient {

	public LDAPPerson getLdapPerson(String sunetId);
}
