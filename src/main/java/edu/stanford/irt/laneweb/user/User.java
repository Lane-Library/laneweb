package edu.stanford.irt.laneweb.user;

import edu.stanford.irt.laneweb.proxy.Ticket;

public class User {

    public static final String NAME = "name";

    private String affiliation;

    /** the emr user id to pass in for cme authentication requests */
    private String emrId;

    private IPGroup iPGroup;

    private String name;

    private Ticket ticket;

    private String univId;

    public String getAffiliation() {
        return this.affiliation;
    }

    public String getEmrId() {
        return this.emrId;
    }

    public IPGroup getIPGroup() {
        return this.iPGroup;
    }

    public String getName() {
        return this.name;
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

    public void setIPGroup(final IPGroup iPGroup) {
        this.iPGroup = iPGroup;
    }

    public void setName(final String displayName) {
        this.name = displayName;
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
                        ",affiliation=").append(this.affiliation).append(",displayname=").append(this.name).append(
                        ",iPGroup=").append(this.iPGroup).append(",ticket=").append(this.ticket);
        return sb.toString();
    }
}
