package edu.stanford.irt.laneweb.servlet.binding.user;

import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.user.User;

public class RequestAttributeUserFactory implements UserFactory {

    private static final String AT = "@";

    private String domain;

    private String emailAttributeName;

    private String nameAttributeName;

    private String userIdHashKey;

    public RequestAttributeUserFactory(final String domain, final String nameAttributeName,
            final String emailAttributeName, final String userIdHashKey) {
        this.domain = domain;
        this.nameAttributeName = nameAttributeName;
        this.emailAttributeName = emailAttributeName;
        this.userIdHashKey = userIdHashKey;
    }

    public User createUser(final HttpServletRequest request) {
        User user = null;
        String remoteUser = request.getRemoteUser();
        if (remoteUser != null) {
            user = new User(getId(remoteUser), getName(request), getEmail(request), this.userIdHashKey);
        }
        return user;
    }

    private String getEmail(final HttpServletRequest request) {
        return getFirstToken(request.getAttribute(this.emailAttributeName));
    }

    private String getFirstToken(final Object value) {
        String firstToken = null;
        if (value != null) {
            firstToken = new StringTokenizer(value.toString(), ";").nextToken();
        }
        return firstToken;
    }

    private String getId(final String remoteUser) {
        StringBuilder sb = new StringBuilder(remoteUser);
        int atIndex = sb.indexOf(AT);
        if (atIndex > -1) {
            sb.setLength(atIndex);
        }
        return sb.append(AT).append(this.domain).toString();
    }

    private String getName(final HttpServletRequest request) {
        return getFirstToken(request.getAttribute(this.nameAttributeName));
    }
}
