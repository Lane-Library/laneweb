package edu.stanford.irt.laneweb.servlet.binding.user;

import jakarta.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.user.User;

@FunctionalInterface
public interface UserFactory {

    User createUser(HttpServletRequest request);
}
