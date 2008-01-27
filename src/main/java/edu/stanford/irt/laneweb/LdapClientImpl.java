package edu.stanford.irt.laneweb;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.logger.AbstractLogEnabled;

import edu.stanford.irt.SystemException;
import edu.stanford.irt.directory.LDAPDirectoryFactory;
import edu.stanford.irt.directory.LDAPDirectoryUtil;
import edu.stanford.irt.directory.LDAPPerson;

public class LdapClientImpl extends AbstractLogEnabled implements LdapClient, Initializable {

    private  LDAPDirectoryFactory directoryFactory = null;

    public void initialize() {
        this.directoryFactory = (LDAPDirectoryFactory) LDAPDirectoryUtil.getLDAPDirectoryFactory("IRT_K5").getDirectoryFactory();
    }

    public LDAPPerson getLdapPerson(final String sunetId) {
        if (getLogger().isDebugEnabled()) {
            getLogger().debug("LdapClientImpl.getLdapPerson("+sunetId+")");
        }
        if (null == sunetId) {
            throw new IllegalArgumentException("null sunetId");
        }
        try {
            return this.directoryFactory.getSearcher().searchPersonByUID(sunetId);
        } catch (SystemException e) {
            if (getLogger().isErrorEnabled()) {
                getLogger().error(e.getMessage(), e);
            }
            return null;
        }
    }
}
