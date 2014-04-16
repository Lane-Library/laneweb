package edu.stanford.irt.laneweb.ldap;

import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.security.auth.Subject;

import org.slf4j.Logger;
import org.springframework.ldap.CommunicationException;
import org.springframework.ldap.NamingException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;

public class LDAPDataAccess {

    private static final class LDAPPrivilegedAction implements PrivilegedAction<LDAPData> {

        private AttributesMapper attributesMapper;

        private LdapTemplate ldapTemplate;

        private String lookupFilter;

        private LDAPPrivilegedAction(final LdapTemplate ldapTemplate, final String lookupFilter,
                final AttributesMapper attributesMapper) {
            this.lookupFilter = lookupFilter;
            this.ldapTemplate = ldapTemplate;
            this.attributesMapper = attributesMapper;
        }

        @Override
        @SuppressWarnings("rawtypes")
        public LDAPData run() {
            List list = this.ldapTemplate.search("", this.lookupFilter, this.attributesMapper);
            if (list == null || list.isEmpty()) {
                return null;
            }
            return (LDAPData) list.get(0);
        }
    }

    private AttributesMapper attributesMapper;

    private final LdapTemplate ldapTemplate;

    private final Logger log;

    private final SubjectSource subjectSource;

    public LDAPDataAccess(final LdapTemplate ldapTemplate, final SubjectSource subjectSource,
            final Set<String> activeAffiliations, final Logger log) {
        this.ldapTemplate = ldapTemplate;
        this.subjectSource = subjectSource;
        this.attributesMapper = new LDAPAttributesMapper(Collections.unmodifiableSet(activeAffiliations));
        this.log = log;
    }

    public LDAPData getLdapDataForSunetid(final String sunetid) {
        LDAPData ldapData = doGet("susunetid=" + sunetid);
        if (ldapData == null) {
            this.log.warn("using sunetid for name");
            ldapData = new LDAPData(sunetid, sunetid, null, false, null);
        }
        return ldapData;
    }

    public LDAPData getLdapDataForUnivid(final String univid) {
        LDAPData ldapData = doGet("suunivid=" + univid);
        if (ldapData == null) {
            this.log.warn("can't find sunetid for univid: " + univid);
            ldapData = new LDAPData(null, null, univid, false, null);
        }
        return ldapData;
    }

    private LDAPData doGet(final String lookupFilter) {
        LDAPData ldapData = null;
        try {
            Subject subject = this.subjectSource.getSubject();
            ldapData = Subject.doAs(subject, new LDAPPrivilegedAction(this.ldapTemplate, lookupFilter,
                    this.attributesMapper));
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
