package edu.stanford.irt.laneweb;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameterizable;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.thread.ThreadSafe;

import edu.stanford.irt.SystemException;
import edu.stanford.irt.directory.LDAPDirectoryFactory;
import edu.stanford.irt.directory.LDAPDirectoryUtil;
import edu.stanford.irt.directory.LDAPPerson;

public class LdapClientImpl extends AbstractLogEnabled implements LdapClient,
		Parameterizable, Initializable, ThreadSafe {

	private LDAPDirectoryFactory directoryFactory = null;

	private String ldapKeytab;

	public LDAPPerson getLdapPerson(final String sunetId) {
		try {
			if (getLogger().isDebugEnabled()) {
				LDAPPerson person = this.directoryFactory.getSearcher().searchPersonByUID(
						sunetId);
				getLogger().debug("ldapPerson = " + person.toString());
				return person;
			} else {
				return this.directoryFactory.getSearcher().searchPersonByUID(
						sunetId);
			}
		} catch (SystemException e) {
			if (getLogger().isErrorEnabled()) {
				getLogger().error("ldap lookup failed for " + sunetId, e);
			}
			return null;
		}
	}

	public void parameterize(Parameters params) throws ParameterException {
		this.ldapKeytab = params.getParameter("ldap-keytab");
	}

	public void initialize() throws Exception {
		this.directoryFactory = (LDAPDirectoryFactory) LDAPDirectoryUtil
				.getLDAPDirectoryFactory(this.ldapKeytab).getDirectoryFactory();
	}
}
