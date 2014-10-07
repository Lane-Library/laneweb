package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.user.User;
import edu.stanford.irt.laneweb.user.UserAttribute;
import edu.stanford.irt.laneweb.user.UserFactory;

public class UserDataBinder implements DataBinder {

    private UserFactory userFactory;

    public UserDataBinder(final UserFactory userFactory) {
        this.userFactory = userFactory;
    }

    @Override
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        User user = null;
        HttpSession session = request.getSession();
        synchronized (session) {
            user = (User) session.getAttribute(Model.USER);
            if (user == null) {
                Map<UserAttribute, String> userAttributes = getUserAttributes(request);
                user = this.userFactory.createUser(userAttributes);
                if (user != null) {
                    session.setAttribute(Model.USER, user);
                }
            }
        }
        if (user != null) {
            model.put(Model.USER, user);
        }
    }

    private Map<UserAttribute, String> getUserAttributes(final HttpServletRequest request) {
        // TODO not implemented yet
        return Collections.emptyMap();
    }
}
