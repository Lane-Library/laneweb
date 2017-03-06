package edu.stanford.irt.laneweb.user;

class LDAPData {

    static final LDAPData NULL = new LDAPData(null, false);

    private boolean isActive;

    private String sunetid;

    public LDAPData(final String sunetid, final boolean isActive) {
        this.sunetid = sunetid;
        this.isActive = isActive;
    }

    public String getSunetId() {
        return this.sunetid;
    }

    public boolean isActive() {
        return this.isActive;
    }
}
