package edu.stanford.irt.laneweb.ldap;

import java.security.PrivilegedAction;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.security.auth.Subject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.CommunicationException;
import org.springframework.ldap.NamingException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;

public class LDAPDataAccess {

    private static class LDAPAttributesMapper implements AttributesMapper {

        public Object mapFromAttributes(final Attributes attributes) throws javax.naming.NamingException {
            String name = null;
            String univId = null;
            boolean isActive = false;
            Attribute currentAttribute = attributes.get("displayName");
            if (currentAttribute != null) {
                name = (String) currentAttribute.get();
            }
            currentAttribute = attributes.get("suunivid");
            if (currentAttribute != null) {
                univId = (String) currentAttribute.get();
            }
            currentAttribute = attributes.get("suAffiliation");
            if (currentAttribute != null) {
            	NamingEnumeration<?> attrs =  currentAttribute.getAll();
            	 while(!isActive && attrs.hasMore()) {
            		 isActive = Affiliation.getAffiliation((String)attrs.next()).isActive();
            	 }
            }
            return new LDAPData(name, univId, isActive);
        }
    }

    private static class LDAPPrivilegedAction implements PrivilegedAction<LDAPData> {

        private AttributesMapper attributesMapper;

        private LdapTemplate ldapTemplate;

        private String sunetid;

        private LDAPPrivilegedAction(final LdapTemplate ldapTemplate, final String sunetid) {
            this.ldapTemplate = ldapTemplate;
            this.sunetid = sunetid;
            this.attributesMapper = new LDAPAttributesMapper();
        }

        @SuppressWarnings("rawtypes")
        public LDAPData run() {
            List list = this.ldapTemplate.search("", "susunetid=" + this.sunetid, this.attributesMapper);
            if (list == null || list.size() == 0) {
                return null;
            }
            return (LDAPData) list.get(0);
        }
    }

    private LdapTemplate ldapTemplate;

    private final Logger log = LoggerFactory.getLogger(LDAPDataAccess.class);

    private SubjectSource subjectSource;

    public LDAPData getLdapData(final String sunetid) {
        LDAPData ldapData = null;
        PrivilegedAction<LDAPData> action = new LDAPPrivilegedAction(this.ldapTemplate, sunetid);
        try {
            Subject subject = this.subjectSource.getSubject();
            ldapData = Subject.doAs(subject, action);
        } catch (SecurityException e) {
            this.log.error("unable to authenticate", e);
        } catch (CommunicationException e) {
            this.log.error("unable to connect to ldap server", e);
        } catch (NamingException e) {
            this.log.error("failed to get ldap data", e);
        }
        if (ldapData == null) {
            this.log.warn("using sunetid for name");
            ldapData = new LDAPData(sunetid, null, false);
        }
        return ldapData;
    }

    public void setLdapTemplate(final LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    public void setSubjectSource(final SubjectSource subjectSource) {
        this.subjectSource = subjectSource;
    }
}
