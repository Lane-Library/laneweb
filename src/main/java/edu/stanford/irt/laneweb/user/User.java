package edu.stanford.irt.laneweb.user;

public interface User {

    String getEmailAddress();

    String getId();

    String getName();

    String getUnivId();

    boolean isActive();
}
