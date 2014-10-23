package edu.stanford.irt.laneweb.servlet.binding.user;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.user.User;

public class ShibbolethAttributeUserFactory implements UserFactory {

    private Map<String, UserFactory> userFactories;

    public ShibbolethAttributeUserFactory(final Map<String, UserFactory> userFactories) {
        this.userFactories = userFactories;
    }

    @Override
    public User createUser(final HttpServletRequest request) {
        User user = null;
        String provider = (String) request.getAttribute("Shib-Identity-Provider");
        if (provider != null && this.userFactories.containsKey(provider)) {
            user = this.userFactories.get(provider).createUser(request);
        }
        return user;
    }
}
