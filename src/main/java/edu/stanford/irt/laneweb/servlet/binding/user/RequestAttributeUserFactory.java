package edu.stanford.irt.laneweb.servlet.binding.user;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.irt.laneweb.user.User;

public class RequestAttributeUserFactory implements UserFactory {

    private static final String AT = "@";

    private static final String EMAIL_ATTRIBUTE_NAME = "LANE_OIDC_mail";

    private static final Logger log = LoggerFactory.getLogger(RequestAttributeUserFactory.class);

    private static final String NAME_ATTRIBUTE_NAME = "LANE_OIDC_displayName";

    private final String userIdHashKey;

    // TODO: using headers instead of attributes. best practice? rename class?
    public RequestAttributeUserFactory(final String userIdHashKey) {
        this.userIdHashKey = userIdHashKey;
    }

    @Override
    public User createUser(final HttpServletRequest request) {
        User user = null;
        String remoteUser = request.getRemoteUser();
        if (remoteUser != null) {
            user = new User(checkId(remoteUser), getName(request), getEmail(request), this.userIdHashKey);
        }
        return user;
    }

    // TODO: can we assume OIDC will always populate user domain?
    // if not, may need another attribute from OIDC/RedHatSSO to determine domain
    private String checkId(final String remoteUser) {
        String id = remoteUser;
        if (!id.contains(AT)) {
            id = id + AT + "unknown";
            log.error("user mising domain: {}", remoteUser);
        }
        return id;
    }

    private String getEmail(final HttpServletRequest request) {
        return request.getHeader(EMAIL_ATTRIBUTE_NAME);
    }

    private String getName(final HttpServletRequest request) {
        return request.getHeader(NAME_ATTRIBUTE_NAME);
    }
}
