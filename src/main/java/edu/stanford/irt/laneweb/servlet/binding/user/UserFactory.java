package edu.stanford.irt.laneweb.servlet.binding.user;

import javax.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.user.User;

@FunctionalInterface
public interface UserFactory {

    User createUser(HttpServletRequest request);
}
