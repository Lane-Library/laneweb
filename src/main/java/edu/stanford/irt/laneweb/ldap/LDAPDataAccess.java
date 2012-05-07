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

        @Override
        public Object mapFromAttributes(final Attributes attributes) throws javax.naming.NamingException {
            String name = null;
            String sunetid = null;
            String univId = null;
            boolean isActive = false;
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
            return new LDAPData(sunetid, name, univId, isActive);
        }
    }

    private static class LDAPPrivilegedAction implements PrivilegedAction<LDAPData> {

        private AttributesMapper attributesMapper;

        private LdapTemplate ldapTemplate;

        private String sunetid;

        private String univid;

        private LDAPPrivilegedAction(final LdapTemplate ldapTemplate) {
            this.ldapTemplate = ldapTemplate;
            this.attributesMapper = new LDAPAttributesMapper();
        }

        @Override
        @SuppressWarnings("rawtypes")
        public LDAPData run() {
            String lookupFilter = null;
            if (this.univid != null) {
                lookupFilter = new StringBuilder("suunivid=").append(this.univid).toString();
            }
            if (this.sunetid != null) {
                lookupFilter = new StringBuilder("susunetid=").append(this.sunetid).toString();
            }
            try {
                List list = this.ldapTemplate.search("", lookupFilter, this.attributesMapper);
                if (list == null || list.size() == 0) {
                    return null;
                }
                return (LDAPData) list.get(0);
            } finally {
                this.sunetid = null;
                this.univid = null;
            }
        }

        public void setSunetid(final String sunetid) {
            this.sunetid = sunetid;
        }

        public void setUnivid(final String univid) {
            this.univid = univid;
        }
    }

    private LdapTemplate ldapTemplate;

    private final Logger log = LoggerFactory.getLogger(LDAPDataAccess.class);

    private SubjectSource subjectSource;

    private String sunetid;

    private String univid;

    private LDAPData doGet() {
        LDAPData ldapData = null;
        LDAPPrivilegedAction action = new LDAPPrivilegedAction(this.ldapTemplate);
        if (this.sunetid != null) {
            action.setSunetid(this.sunetid);
        }
        if (this.univid != null) {
            action.setUnivid(this.univid);
        }
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
        return ldapData;
    }

    @Deprecated
    public LDAPData getLdapData(final String sunetid) {
        return getLdapDataForSunetid(sunetid);
    }

    public LDAPData getLdapDataForSunetid(final String sunetid) {
        this.sunetid = sunetid;
        LDAPData ldapData = doGet();
        if (ldapData == null) {
            this.log.warn("using sunetid for name");
            ldapData = new LDAPData(this.sunetid, this.sunetid, this.univid, false);
        }
        return ldapData;
    }

    public LDAPData getLdapDataForUnivid(final String univid) {
        this.univid = univid;
        LDAPData ldapData = doGet();
        if (ldapData == null) {
            this.log.warn("using univid for name");
            ldapData = new LDAPData(this.sunetid, this.univid, this.univid, false);
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
