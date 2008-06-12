package edu.stanford.irt.laneweb.voyager;

import edu.stanford.irt.directory.LDAPPerson;

public interface VoyagerLogin {

    public static final String ROLE = VoyagerLogin.class.getName();

    String getVoyagerURL(LDAPPerson ldapPerson, String pid, String queryString);

}
