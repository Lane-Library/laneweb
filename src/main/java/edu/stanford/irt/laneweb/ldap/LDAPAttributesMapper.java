package edu.stanford.irt.laneweb.ldap;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.springframework.ldap.core.AttributesMapper;

public class LDAPAttributesMapper implements AttributesMapper {

    @Override
    public Object mapFromAttributes(final Attributes attributes) throws javax.naming.NamingException {
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
                isActive = Affiliation.getAffiliation((String) attrs.next()).isActive();
            }
        }
        currentAttribute = attributes.get("mail");
        if (currentAttribute != null) {
            emailAddress = (String) currentAttribute.get();
        }
        return new LDAPData(sunetid, name, univId, isActive, emailAddress);
    }
}