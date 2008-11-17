package edu.stanford.irt.laneweb;

import org.apache.log4j.Logger;

import edu.stanford.irt.SystemException;
import edu.stanford.irt.directory.LDAPDirectoryFactory;
import edu.stanford.irt.directory.LDAPDirectoryUtil;
import edu.stanford.irt.directory.LDAPPerson;

public class LdapClient {

    private Logger logger = Logger.getLogger(LdapClient.class);

    private LDAPDirectoryFactory directoryFactory;

    private String ldapKeytab;

    public LDAPPerson getLdapPerson(final String sunetId) {
        LDAPPerson person = null;
        try {
            person = this.directoryFactory.getSearcher().searchPersonByUID(sunetId);
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("ldapPerson = " + person);
            }
        } catch (SystemException e) {
            this.logger.error("ldap lookup failed for " + sunetId, e);
        }
        return person;
    }

    public void setLdapKeytab(final String ldapKeytab) {
        this.ldapKeytab = ldapKeytab;
    }

    public void initialize() {
        this.directoryFactory = (LDAPDirectoryFactory) LDAPDirectoryUtil.getLDAPDirectoryFactory(this.ldapKeytab).getDirectoryFactory();
        this.directoryFactory.setConnectionTimeout("5000");
    }
}
