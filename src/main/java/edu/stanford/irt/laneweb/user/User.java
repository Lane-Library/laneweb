package edu.stanford.irt.laneweb.user;

public class User {

    public static final String CMEID = "cmeid";

    public static final String IPGROUP = "ip-group";

    public static final String NAME = "name";

    public static final String SUNETID = "sunetid";

    public static final String TICKET = "ticket";
    
    public static final String PROXY_LINKS = "proxy-links";

    private String affiliation;

    /** the user id to pass in cme authentication requests */
    private String cmeId;

    private IPGroup iPGroup;

    private String name;

    private Boolean proxyLinks;

    private String sunetId;

    private Ticket ticket;

    private String univId;

    public String getAffilation() {
        return this.affiliation;
    }

    public String getCmeId() {
        return this.cmeId;
    }

    public IPGroup getIPGroup() {
        return this.iPGroup;
    }

    public String getName() {
        return this.name;
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

    public String getUnivId() {
        return this.univId;
    }

    public void setAffiliation(final String affiliation) {
        this.affiliation = affiliation;
    }

    public void setCmeId(final String cmeId) {
        this.cmeId = cmeId;
    }

    public void setIPGroup(final IPGroup iPGroup) {
        this.iPGroup = iPGroup;
    }

    public void setName(final String displayName) {
        this.name = displayName;
    }

    public void setProxyLinks(final Boolean proxyLinks) {
        this.proxyLinks = proxyLinks;
    }

    public void setSunetId(final String uId) {
        this.sunetId = uId;
    }

    public void setTicket(final Ticket ticket) {
        this.ticket = ticket;
    }

    public void setUnivId(final String univId) {
        this.univId = univId;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("sunetId=").append(this.sunetId).append(",univid=").append(this.univId).append(",affiliation=").append(
                this.affiliation).append(",displayname=").append(this.name).append(",iPGroup=").append(this.iPGroup).append(",ticket=").append(this.ticket)
                .append(",proxyLinks=").append(this.proxyLinks);
        return sb.toString();
    }
}
