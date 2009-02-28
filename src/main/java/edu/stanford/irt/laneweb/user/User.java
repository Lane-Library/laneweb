package edu.stanford.irt.laneweb.user;

public class User {

    private String affiliation;

    private String displayName;

    private Boolean proxyLinks;

    private Ticket ticket;

    private TrackingAffiliation trackingAffiliation;

    private String uId;

    private String univId;

    public String getAffilation() {
        return this.affiliation;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public Boolean getProxyLinks() {
        return this.proxyLinks;
    }

    public Ticket getTicket() {
        return this.ticket;
    }

    public TrackingAffiliation getTrackingAffiliation() {
        return this.trackingAffiliation;
    }

    public String getUId() {
        return this.uId;
    }

    public String getUnivId() {
        return this.univId;
    }

    public void setAffiliation(final String affiliation) {
        this.affiliation = affiliation;
    }

    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

    public void setProxyLinks(final Boolean proxyLinks) {
        this.proxyLinks = proxyLinks;
    }

    public void setTicket(final Ticket ticket) {
        this.ticket = ticket;
    }

    public void setTrackingAffiliation(final TrackingAffiliation trackingAffiliation) {
        this.trackingAffiliation = trackingAffiliation;
    }

    public void setUId(final String uId) {
        this.uId = uId;
    }

    public void setUnivId(final String univId) {
        this.univId = univId;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("uid=").append(this.uId).append(",univid=").append(this.univId).append(",affiliation=").append(this.affiliation)
                .append(",displayname=").append(this.displayName).append(",trackingAffiliation=").append(this.trackingAffiliation).append(",ticket=").append(
                        this.ticket).append(",proxyLinks=").append(this.proxyLinks);
        return sb.toString();
    }
}
