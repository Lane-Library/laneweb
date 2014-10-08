package edu.stanford.irt.laneweb.servlet.binding;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.ldap.LDAPData;
import edu.stanford.irt.laneweb.ldap.LDAPDataAccess;
import edu.stanford.irt.laneweb.user.UserAttribute;

public class LDAPUserAttributesRequestParser implements UserAttributesRequestParser {

    private LDAPDataAccess ldapDataAccess;

    public LDAPUserAttributesRequestParser(final LDAPDataAccess ldapDataAccess) {
        this.ldapDataAccess = ldapDataAccess;
    }

    @Override
    public Map<UserAttribute, String> parse(final HttpServletRequest request) {
        Map<UserAttribute, String> attributes = new HashMap<UserAttribute, String>();
        String id = request.getRemoteUser();
        if (id == null) {
            throw new LanewebException("null remoteUser");
        }
        LDAPData data = this.ldapDataAccess.getLdapDataForSunetid(id);
        attributes.put(UserAttribute.EMAIL, data.getEmailAddress());
        attributes.put(UserAttribute.NAME, data.getName());
        attributes.put(UserAttribute.ID, data.getSunetId());
        attributes.put(UserAttribute.ACTIVE, Boolean.toString(data.isActive()));
        attributes.put(UserAttribute.UNIV_ID, data.getUnivId());
        attributes.put(UserAttribute.PROVIDER, "stanford.edu");
        return attributes;
    }
}
