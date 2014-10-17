package edu.stanford.irt.laneweb.servlet;

public enum LanewebCookie {
    EZPROXY("ezproxy"),
    LOGIN_EXPIRATION("persistent-expiration-date"),
    LOGIN_PREFERENCE("persistent-preference"),
    USER("user"),
    WEBAUTH("webauth_at");

    private final String name;

    private LanewebCookie(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
