package edu.stanford.irt.laneweb;

import edu.stanford.irt.directory.LDAPPerson;

public class UserInfo {

    private Boolean proxyLinks;

    private String sunetId;

    private Affiliation affiliation;

    private Ticket ticket;

    private LDAPPerson ldapPerson;

    public Affiliation getAffiliation() {
        return this.affiliation;
    }

    public void setAffiliation(final Affiliation affiliation) {
        this.affiliation = affiliation;
    }

    public LDAPPerson getLdapPerson() {
        return this.ldapPerson;
    }

    public void setLdapPerson(final LDAPPerson ldapPerson) {
        this.ldapPerson = ldapPerson;
    }

    public Boolean getProxyLinks() {
        return this.proxyLinks;
    }

    public void setProxyLinks(final Boolean proxyLinks) {
        this.proxyLinks = proxyLinks;
    }

    public String getSunetId() {
        return this.sunetId;
    }

    public void setSunetId(final String sunetId) {
        this.sunetId = sunetId;
    }

    public Ticket getTicket() {
        return this.ticket;
    }

    public void setTicket(final Ticket ticket) {
        this.ticket = ticket;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("affiliation=").append(this.affiliation).append(' ');
        sb.append("ldapPerson=").append(this.ldapPerson).append(' ');
        sb.append("proxyLinks=").append(this.proxyLinks).append(' ');
        sb.append("sunetId=").append(this.sunetId).append(' ');
        sb.append("ticket=").append(this.ticket);
        return sb.toString();

    }

}
