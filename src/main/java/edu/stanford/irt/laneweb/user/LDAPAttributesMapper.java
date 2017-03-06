package edu.stanford.irt.laneweb.user;

import java.util.Set;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.springframework.ldap.core.AttributesMapper;

class LDAPAttributesMapper implements AttributesMapper<LDAPData> {

    private Set<String> activeAffiliations;

    public LDAPAttributesMapper(final Set<String> activeAffiliations) {
        this.activeAffiliations = activeAffiliations;
    }

    @Override
    public LDAPData mapFromAttributes(final Attributes attributes) throws NamingException {
        String sunetid = null;
        boolean isActive = false;
        Attribute suAffiliations = attributes.get("suAffiliation");
        if (suAffiliations != null) {
            NamingEnumeration<?> suAffiliation = suAffiliations.getAll();
            while (!isActive && suAffiliation.hasMore()) {
                isActive = this.activeAffiliations.contains(suAffiliation.next());
            }
        }
        if (isActive) {
            Attribute uid = attributes.get("uid");
            if (uid != null) {
                sunetid = (String) uid.get();
            }
        }
        return new LDAPData(sunetid, isActive);
    }
}