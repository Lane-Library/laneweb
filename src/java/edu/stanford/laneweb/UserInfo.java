package edu.stanford.laneweb;

import java.util.Map;

import org.apache.avalon.framework.logger.Logger;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;

import edu.stanford.irt.directory.LDAPDirectoryFactory;
import edu.stanford.irt.directory.LDAPDirectoryUtil;
import edu.stanford.irt.directory.LDAPPerson;

public class UserInfo {
	
	private final static LDAPDirectoryFactory directoryFactory =(LDAPDirectoryFactory) LDAPDirectoryUtil
	   																			.getLDAPDirectoryFactory("IRT_K5")
	   																			.getDirectoryFactory();
	
	private Boolean						   proxyLinks;

	private String							sunetId;

	private Affiliation					   affiliation;

	private Ticket							ticket;

	private LDAPPerson						ldapPerson;
 
  
	
	
	public void update(final Map objectModel, Logger logger) {
		if (objectModel == null) {
			throw new IllegalArgumentException("null objectModel");
		}
		if (logger == null) {
			throw new IllegalArgumentException("null logger");
		}
		
		Request request = ObjectModelHelper.getRequest(objectModel);
		if (this.affiliation == null) {
			String ip = request.getRemoteAddr();
			// mod_proxy puts the real remote address in an x-forwarded-for
			// header
			// Load balancer also does this
			String header = request.getHeader(LanewebConstants.X_FORWARDED_FOR);
			if (header != null) {
				ip = header;
			}
			try {
				this.affiliation = Affiliation.getAffiliationForIP(ip);
			} catch (Exception e) {
				this.affiliation = Affiliation.ERR;
			}
		}
		if (this.sunetId == null) {
			String requestSunetId = (String) request
					.getAttribute(LanewebConstants.WEBAUTH_USER);
			
			if (!LanewebConstants.UNSET.equals(requestSunetId)) {
				this.sunetId = requestSunetId;
				if(sunetId != null)
				{
					try {
						ldapPerson = directoryFactory.getSearcher().searchPersonByUID(sunetId);
					} catch (Exception e) {
						logger.error(e.getMessage(),e);
					}
				}
			}
		}


		if (null != request.getParameter(LanewebConstants.PROXY_LINKS)) {
			this.proxyLinks = new Boolean(request
					.getParameter(LanewebConstants.PROXY_LINKS));
		}
		if (this.sunetId != null) {
			this.ticket = new Ticket(this.sunetId);
		}
	}

	public Affiliation getAffiliation() {
		return this.affiliation;
	}

	public Boolean getProxyLinks() {
		return this.proxyLinks;
	}

	public String getSunetId() {
		return this.sunetId;
	}

	public Ticket getTicket() {
		return this.ticket;
	}

	public LDAPPerson getLdapPerson() {
		return ldapPerson;
	}

	
	
}
