package edu.stanford.irt.laneweb.ldap;

public class LDAPData {

    private boolean isActive;

    private String name;

    private String univId;

    public LDAPData(final String name, final String univId, final boolean isActive) {
        this.name = name;
        this.univId = univId;
        this.isActive = isActive;
    }

    public String getName() {
        return this.name;
    }

    public String getUnivId() {
        return this.univId;
    }

    public boolean isActive() {
        return this.isActive;
    }

    @Override
    public String toString() {
        return new StringBuilder("univid=").append(this.univId).append(",name=").append(this.name).append(",isActive=")
                .append(this.isActive).toString();
    }
}
