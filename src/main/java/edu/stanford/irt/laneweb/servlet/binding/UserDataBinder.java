package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.user.User;

public class UserDataBinder implements DataBinder {

    private static final String IDENTITY_PROVIDER = "Shib-Identity-Provider";

    private Map<Object, UserAttributesRequestParser> requestParsers;

    public UserDataBinder(final Map<Object, UserAttributesRequestParser> requestParsers) {
        this.requestParsers = requestParsers;
    }

    @Override
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        User user = null;
        HttpSession session = request.getSession();
        synchronized (session) {
            user = (User) session.getAttribute(Model.USER);
            if (user == null) {
                user = getUser(request);
                if (user != null) {
                    session.setAttribute(Model.USER, user);
                }
            }
        }
        if (user != null) {
            model.put(Model.USER, user);
        }
    }

    private User getUser(final HttpServletRequest request) {
        User user = null;
        String remoteUser = request.getRemoteUser();
        if (remoteUser != null) {
            String identityProvider = (String) request.getAttribute(IDENTITY_PROVIDER);
            user = new User(this.requestParsers.get(identityProvider).parse(request));
        }
        return user;
    }
}
