package edu.stanford.irt.laneweb.user;

public class User {

    private String affiliation;

    private String displayName;

    private Boolean proxyLinks;

    private Ticket ticket;

    private IPGroup iPGroup;

    private String sunetId;

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

    public IPGroup getTrackingAffiliation() {
        return this.iPGroup;
    }

    public String getSunetId() {
        return this.sunetId;
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

    public void setTrackingAffiliation(final IPGroup iPGroup) {
        this.iPGroup = iPGroup;
    }

    public void setSunetId(final String uId) {
        this.sunetId = uId;
    }

    public void setUnivId(final String univId) {
        this.univId = univId;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("sunetId=").append(this.sunetId).append(",univid=").append(this.univId).append(",affiliation=").append(this.affiliation)
                .append(",displayname=").append(this.displayName).append(",iPGroup=").append(this.iPGroup).append(",ticket=").append(
                        this.ticket).append(",proxyLinks=").append(this.proxyLinks);
        return sb.toString();
    }
}
