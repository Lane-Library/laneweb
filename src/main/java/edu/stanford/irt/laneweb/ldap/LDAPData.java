package edu.stanford.irt.laneweb.ldap;

public class LDAPData {

    private String name;

    private String univId;

    public String getName() {
        return this.name;
    }

    public String getUnivId() {
        return this.univId;
    }

    public void setName(final String displayName) {
        this.name = displayName;
    }

    public void setUnivId(final String univId) {
        this.univId = univId;
    }

    @Override
    public String toString() {
        return new StringBuilder("univid=").append(this.univId)
                .append(",displayname=").append(this.name).toString();
    }
}
