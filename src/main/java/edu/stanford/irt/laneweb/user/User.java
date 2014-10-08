package edu.stanford.irt.laneweb.user;

import java.util.Map;

public class User {

    private String email;

    private String fullId;

    private String id;

    private String identityProvider;

    private boolean isActive;

    private String name;

    private String univId;

    public User(final Map<UserAttribute, String> userAttributes) {
        this.id = userAttributes.get(UserAttribute.ID);
        this.identityProvider = userAttributes.get(UserAttribute.PROVIDER);
        this.isActive = Boolean.getBoolean(userAttributes.get(UserAttribute.PROVIDER));
        this.name = userAttributes.get(UserAttribute.NAME);
        this.univId = userAttributes.get(UserAttribute.UNIV_ID);
    }

    public String getEmail() {
        return this.email;
    }

    public String getFullId() {
        return this.fullId;
    }

    public String getId() {
        return this.id;
    }

    public String getIdentityProvider() {
        return this.identityProvider;
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
}
