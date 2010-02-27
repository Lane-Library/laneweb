package edu.stanford.irt.laneweb.ldap;

import java.security.PrivilegedAction;
import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.security.auth.Subject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.AuthenticationException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;

public class LDAPDataAccess {

    private static final Logger LOGGER = LoggerFactory.getLogger(LDAPDataAccess.class);

    private LdapTemplate ldapTemplate;

    private SubjectSource subjectSource;

    public void setLdapTemplate(final LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    public void setSubjectSource(final SubjectSource subjectSource) {
        this.subjectSource = subjectSource;
    }
    
    public LDAPData getLdapData(final String sunetid) {
        Subject subject = this.subjectSource.getSubject();
        PrivilegedAction<LDAPData> action = new LDAPPrivilegedAction(this.ldapTemplate, sunetid);
        try {
            return (LDAPData) Subject.doAs(subject, action);
        } catch (AuthenticationException e) {
            LOGGER.error(e.getMessage(), e);
            return action.run();
        }
    }
    
    private static class LDAPPrivilegedAction implements PrivilegedAction<LDAPData> {
        
        private LdapTemplate ldapTemplate;
        
        private String sunetid;
        
        private AttributesMapper attributesMapper;
        
        private LDAPPrivilegedAction(LdapTemplate ldapTemplate, String sunetid) {
            this.ldapTemplate = ldapTemplate;
            this.sunetid = sunetid;
            this.attributesMapper = new LDAPAttributesMapper();
        }
        
        @SuppressWarnings("unchecked")
        public LDAPData run() {
            List list = this.ldapTemplate.search("", "susunetid=" + sunetid, this.attributesMapper);
            if (list == null || list.size() == 0) {
                return null;
            }
            return (LDAPData) list.get(0);
        }
    }
    
    private static class LDAPAttributesMapper implements AttributesMapper {

        public Object mapFromAttributes(Attributes attributes) throws NamingException {
            LDAPData ldapData = new LDAPData();
            Attribute currentAttribute = attributes.get("displayName");
            if (null != currentAttribute) {
                ldapData.setName((String) currentAttribute.get());
            }
            currentAttribute = attributes.get("suaffiliation");
            if (null != currentAttribute) {
                ldapData.setAffiliation((String) currentAttribute.get());
            }
            currentAttribute = attributes.get("suunivid");
            if (null != currentAttribute) {
                ldapData.setUnivId((String) currentAttribute.get());
            }
            return ldapData;
        }
        
    }
}
