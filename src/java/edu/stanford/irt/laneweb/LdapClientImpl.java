package edu.stanford.irt.laneweb;

import edu.stanford.irt.directory.LDAPDirectoryFactory;
import edu.stanford.irt.directory.LDAPDirectoryUtil;
import edu.stanford.irt.directory.LDAPPerson;

import org.apache.log4j.Logger;

public class LdapClientImpl implements LdapClient {

	Logger log = Logger.getLogger(this.getClass());
	
	private static LDAPDirectoryFactory directoryFactory = null;
	
	
	public LdapClientImpl()
	{
		try {
			directoryFactory =(LDAPDirectoryFactory) LDAPDirectoryUtil
			.getLDAPDirectoryFactory("IRT_K5")
			.getDirectoryFactory();

		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}
	
	public LDAPPerson getLdapPerson(String sunetId)
	{
		LDAPPerson ldapPerson = null;
		if(sunetId != null)
		{
			try {
				ldapPerson = directoryFactory.getSearcher().searchPersonByUID(sunetId);
			} catch (Exception e) {
				log.error(e.getMessage(),e);
			}
		}
		return ldapPerson;
	}
}
