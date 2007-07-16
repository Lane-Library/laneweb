package edu.stanford.irt.laneweb;

import edu.stanford.irt.directory.LDAPPerson;

public class UserInfo {
	
	private Boolean						   proxyLinks;

	private String							sunetId;

	private Affiliation					   affiliation;

	private Ticket							ticket;

	private LDAPPerson						ldapPerson;


	public Affiliation getAffiliation() {
		return affiliation;
	}

	public void setAffiliation(Affiliation affiliation) {
		this.affiliation = affiliation;
	}

	public LDAPPerson getLdapPerson() {
		return ldapPerson;
	}

	public void setLdapPerson(LDAPPerson ldapPerson) {
		this.ldapPerson = ldapPerson;
	}

	public Boolean getProxyLinks() {
		return proxyLinks;
	}

	public void setProxyLinks(Boolean proxyLinks) {
		this.proxyLinks = proxyLinks;
	}

	public String getSunetId() {
		return sunetId;
	}

	public void setSunetId(String sunetId) {
		this.sunetId = sunetId;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}
 

	
	
}
