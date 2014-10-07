package edu.stanford.irt.laneweb.user;

import java.util.Map;

public interface UserFactory {

    User createUser(Map<UserAttribute, String> attributes);
}
