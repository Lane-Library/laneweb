package edu.stanford.irt.laneweb;

import org.apache.log4j.Logger;

import edu.stanford.irt.directory.LDAPDirectoryFactory;
import edu.stanford.irt.directory.LDAPDirectoryUtil;
import edu.stanford.irt.directory.LDAPPerson;

public class LdapClientImpl implements LdapClient {

    Logger log = Logger.getLogger(this.getClass());

    private static LDAPDirectoryFactory directoryFactory = null;

    public LdapClientImpl() {
        try {
            directoryFactory = (LDAPDirectoryFactory) LDAPDirectoryUtil.getLDAPDirectoryFactory("IRT_K5").getDirectoryFactory();

        } catch (Exception e) {
            this.log.error(e.getMessage(), e);
        }
    }

    public LDAPPerson getLdapPerson(final String sunetId) {
        LDAPPerson ldapPerson = null;
        if (sunetId != null) {
            try {
                ldapPerson = directoryFactory.getSearcher().searchPersonByUID(sunetId);
            } catch (Exception e) {
                this.log.error(e.getMessage(), e);
            }
        }
        return ldapPerson;
    }
}
