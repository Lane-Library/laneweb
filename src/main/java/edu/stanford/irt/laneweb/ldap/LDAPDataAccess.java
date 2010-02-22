package edu.stanford.irt.laneweb.ldap;

import java.security.PrivilegedAction;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.security.auth.Subject;

import org.apache.log4j.Logger;
import org.springframework.ldap.AuthenticationException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;

public class LDAPDataAccess {

    private static final Logger LOGGER = Logger.getLogger(LDAPDataAccess.class);

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
        public LDAPData run() {
            return (LDAPData) this.ldapTemplate.search("", "susunetid=" + sunetid, this.attributesMapper);
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
