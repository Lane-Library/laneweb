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

    private static final class LDAPPrivilegedAction implements PrivilegedAction<LDAPData> {

        private AttributesMapper attributesMapper;

        private LdapTemplate ldapTemplate;

        private String lookupFilter;

        private LDAPPrivilegedAction(final LdapTemplate ldapTemplate, final String lookupFilter) {
            this.lookupFilter = lookupFilter;
            this.ldapTemplate = ldapTemplate;
            this.attributesMapper = new LDAPAttributesMapper();
        }

        @Override
        @SuppressWarnings("rawtypes")
        public LDAPData run() {
            List list = this.ldapTemplate.search("", this.lookupFilter, this.attributesMapper);
            if (list == null || list.size() == 0) {
                return null;
            }
            return (LDAPData) list.get(0);
        }
    }

    private LdapTemplate ldapTemplate;

    private final Logger log = LoggerFactory.getLogger(LDAPDataAccess.class);

    private SubjectSource subjectSource;

    public LDAPData getLdapDataForSunetid(final String sunetid) {
        LDAPData ldapData = doGet("susunetid=" + sunetid);
        if (ldapData == null) {
            this.log.warn("using sunetid for name");
            ldapData = new LDAPData(sunetid, sunetid, null, false);
        }
        return ldapData;
    }

    public LDAPData getLdapDataForUnivid(final String univid) {
        LDAPData ldapData = doGet("suunivid=" + univid);
        if (ldapData == null) {
            this.log.warn("can't find sunetid for univid: " + univid);
            ldapData = new LDAPData(null, null, univid, false);
        }
        return ldapData;
    }

    public void setLdapTemplate(final LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    public void setSubjectSource(final SubjectSource subjectSource) {
        this.subjectSource = subjectSource;
    }

    private LDAPData doGet(final String lookupFilter) {
        LDAPData ldapData = null;
        try {
            Subject subject = this.subjectSource.getSubject();
            ldapData = Subject.doAs(subject, new LDAPPrivilegedAction(this.ldapTemplate, lookupFilter));
        } catch (SecurityException e) {
            this.log.error("unable to authenticate", e);
        } catch (CommunicationException e) {
            this.log.error("unable to connect to ldap server", e);
        } catch (NamingException e) {
            this.log.error("failed to get ldap data", e);
        }
        return ldapData;
    }
}
