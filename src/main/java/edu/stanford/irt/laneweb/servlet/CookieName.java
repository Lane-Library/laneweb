package edu.stanford.irt.laneweb.servlet;

public enum CookieName {

    EXPIRATION("lane-login-expiration-date"),
    EZPROXY("ezproxy"),
    IS_PERSISTENT("isPersistent"),
    USER("user");

    private String name;

    CookieName(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
