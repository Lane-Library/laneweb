package edu.stanford.irt.laneweb.ldap;

public class LDAPData {

    private String affiliation;

    private String name;

    private String univId;

    public String getAffiliation() {
        return this.affiliation;
    }

    public String getName() {
        return this.name;
    }

    public String getUnivId() {
        return this.univId;
    }

    public void setAffiliation(final String affiliation) {
        this.affiliation = affiliation;
    }

    public void setName(final String displayName) {
        this.name = displayName;
    }

    public void setUnivId(final String univId) {
        this.univId = univId;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("univid=").append(this.univId).append(",affiliation=")
                .append(this.affiliation).append(",displayname=").append(this.name);
        return sb.toString();
    }
}
