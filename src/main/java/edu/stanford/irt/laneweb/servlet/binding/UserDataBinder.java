package edu.stanford.irt.laneweb.servlet.binding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.binding.user.UserFactory;
import edu.stanford.irt.laneweb.user.User;

public class UserDataBinder implements DataBinder {

    private List<UserFactory> userFactories;

    public UserDataBinder(final List<UserFactory> userFactories) {
        this.userFactories = new ArrayList<>(userFactories);
    }

    @Override
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(Model.USER);
        if (user == null) {
            user = this.userFactories
                    .stream()
                    .map((final UserFactory f) -> f.createUser(request))
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
            if (user != null) {
                session.setAttribute(Model.USER, user);
                // case 100633: clear Model.PROXY_LINKS if user is logged in
                session.removeAttribute(Model.PROXY_LINKS);
            }
        }
        putUserInModel(user, model);
    }

    private void putUserInModel(final User user, final Map<String, Object> model) {
        if (user != null) {
            model.put(Model.USER, user);
            model.put(Model.USER_ID, user.getId());
            model.put(Model.AUTH, user.getHashedId());
            String email = user.getEmail();
            if (email != null) {
                model.put(Model.EMAIL, email);
            }
            String name = user.getName();
            if (name != null) {
                model.put(Model.NAME, name);
            }
        }
    }
}
