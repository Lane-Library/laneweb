package edu.stanford.irt.laneweb.user;

import edu.stanford.irt.laneweb.proxy.Ticket;

public class User {

    public static final String NAME = "name";

    private String affiliation;

    /** the emr user id to pass in for cme authentication requests */
    private String emrId;

    private String name;

    private Boolean proxyLinks;

    private Ticket ticket;

    private String univId;

    public String getAffiliation() {
        return this.affiliation;
    }

    public String getEmrId() {
        return this.emrId;
    }

    public String getName() {
        return this.name;
    }

    public Boolean getProxyLinks() {
        return this.proxyLinks;
    }
    public Ticket getTicket() {
        return this.ticket;
    }

    public String getUnivId() {
        return this.univId;
    }

    public void setAffiliation(final String affiliation) {
        this.affiliation = affiliation;
    }

    public void setEmrId(final String emrId) {
        this.emrId = emrId;
    }

    public void setName(final String displayName) {
        this.name = displayName;
    }

    public void setProxyLinks(final Boolean proxyLinks) {
        this.proxyLinks = proxyLinks;
    }

    public void setTicket(final Ticket ticket) {
        this.ticket = ticket;
    }

    public void setUnivId(final String univId) {
        this.univId = univId;
    }

    @Override
    public String toString() {
        StringBuffer sb =
                new StringBuffer("univid=").append(this.univId).append(
                        ",affiliation=").append(this.affiliation).append(",displayname=").append(this.name).append(",ticket=").append(this.ticket).append(",proxyLinks=")
                        .append(this.proxyLinks);
        return sb.toString();
    }
}
