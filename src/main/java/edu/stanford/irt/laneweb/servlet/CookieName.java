package edu.stanford.irt.laneweb.servlet;

public enum CookieName {
    EXPIRATION("lane-login-expiration-date"), EZPROXY("ezproxy"), IS_PERSISTENT("isPersistent"), USER("lane_user");

    String name;

    CookieName(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
