package edu.stanford.irt.laneweb.user;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.springframework.ldap.core.AttributesMapper;

class LDAPAttributesMapper implements AttributesMapper<LDAPData> {

    private List<String> privilegeGroups;

    public LDAPAttributesMapper(final List<String> privilegeGroups) {
        this.privilegeGroups = new ArrayList<>(privilegeGroups);
    }

    @Override
    public LDAPData mapFromAttributes(final Attributes attributes) throws NamingException {
        String sunetid = null;
        boolean isActive = false;
        Attribute privilegeGroups = attributes.get("suPrivilegeGroup");
        if (privilegeGroups != null) {
            NamingEnumeration<?> privilegeGroup = privilegeGroups.getAll();
            while (!isActive && privilegeGroup.hasMore()) {
                isActive = this.privilegeGroups.contains(privilegeGroup.next());
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
