package edu.stanford.irt.laneweb.user;

import java.util.Set;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.springframework.ldap.core.AttributesMapper;

public class LDAPAttributesMapper implements AttributesMapper {

    private Set<String> activeAffiliations;

    public LDAPAttributesMapper(final Set<String> activeAffiliations) {
        this.activeAffiliations = activeAffiliations;
    }

    @Override
    public Object mapFromAttributes(final Attributes attributes) throws NamingException {
        String name = null;
        String sunetid = null;
        String univId = null;
        boolean isActive = false;
        String emailAddress = null;
        Attribute currentAttribute = attributes.get("displayName");
        if (currentAttribute != null) {
            name = (String) currentAttribute.get();
        }
        currentAttribute = attributes.get("uid");
        if (currentAttribute != null) {
            sunetid = (String) currentAttribute.get();
        }
        currentAttribute = attributes.get("suunivid");
        if (currentAttribute != null) {
            univId = (String) currentAttribute.get();
        }
        currentAttribute = attributes.get("suAffiliation");
        if (currentAttribute != null) {
            NamingEnumeration<?> attrs = currentAttribute.getAll();
            while (!isActive && attrs.hasMore()) {
                isActive = this.activeAffiliations.contains(attrs.next());
            }
        }
        currentAttribute = attributes.get("mail");
        if (currentAttribute != null) {
            emailAddress = (String) currentAttribute.get();
        }
        return new LDAPData(sunetid, name, univId, isActive, emailAddress);
    }
}