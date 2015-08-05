package edu.stanford.irt.laneweb.servlet.binding.user;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.irt.laneweb.user.User;

public class RequestAttributeUserFactory implements UserFactory {

    private static final String AT = "@";

    private static final String EMAIL_ATTRIBUTE_NAME = "mail";

    private static final Logger LOG = LoggerFactory.getLogger(RequestAttributeUserFactory.class);

    private static final String NAME_ATTRIBUTE_NAME = "displayName";

    private static final String SHIBBOLETH_PROVIDER = "Shib-Identity-Provider";

    private final Map<String, String> domainMap;

    private final String userIdHashKey;

    public RequestAttributeUserFactory(final String userIdHashKey) {
        this.userIdHashKey = userIdHashKey;
        this.domainMap = new HashMap<String, String>();
    }

    @Override
    public User createUser(final HttpServletRequest request) {
        User user = null;
        String remoteUser = request.getRemoteUser();
        if (remoteUser != null) {
            user = new User(getId(remoteUser, request), getName(request), getEmail(request), this.userIdHashKey);
        }
        return user;
    }

    private String getDomain(final HttpServletRequest request) {
        String domain = null;
        String provider = (String) request.getAttribute(SHIBBOLETH_PROVIDER);
        if (provider == null) {
            domain = "unknown";
        } else {
            synchronized (this.domainMap) {
                domain = this.domainMap.get(provider);
                if (domain == null) {
                    domain = getDomainFromProvider(provider);
                    this.domainMap.put(provider, domain);
                }
            }
        }
        return domain;
    }

    private String getDomainFromProvider(final String provider) {
        String domain = null;
        try {
            domain = new URI(provider).getHost();
            int first = domain.indexOf('.');
            int last = domain.lastIndexOf('.');
            if (first != last) {
                domain = domain.substring(first + 1, domain.length());
            }
        } catch (URISyntaxException e) {
            LOG.error(e.getMessage(), e);
            domain = "unknown";
        }
        return domain;
    }

    private String getEmail(final HttpServletRequest request) {
        return getFirstToken(request.getAttribute(EMAIL_ATTRIBUTE_NAME));
    }

    private String getFirstToken(final Object value) {
        String firstToken = null;
        if (value != null) {
            firstToken = new StringTokenizer(value.toString(), ";").nextToken();
        }
        return firstToken;
    }

    private String getId(final String remoteUser, final HttpServletRequest request) {
        String id = null;
        if (remoteUser.contains(AT)) {
            id = remoteUser;
        } else {
            id = new StringBuilder(remoteUser).append(AT).append(getDomain(request)).toString();
        }
        return id;
    }

    private String getName(final HttpServletRequest request) {
        return getFirstToken(request.getAttribute(NAME_ATTRIBUTE_NAME));
    }
}
