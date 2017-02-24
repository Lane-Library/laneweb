package edu.stanford.irt.laneweb.user;

import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.security.auth.Subject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.NamingException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;

public class LDAPDataAccess {

    private static final Logger LOG = LoggerFactory.getLogger(LDAPDataAccess.class);

    private AttributesMapper<LDAPData> attributesMapper;

    private final LdapTemplate ldapTemplate;

    private final SubjectSource subjectSource;

    public LDAPDataAccess(final LdapTemplate ldapTemplate, final SubjectSource subjectSource,
            final Set<String> activeAffiliations) {
        this.ldapTemplate = ldapTemplate;
        this.subjectSource = subjectSource;
        this.attributesMapper = new LDAPAttributesMapper(Collections.unmodifiableSet(activeAffiliations));
    }

    public String getActiveSunetId(final String univid) {
        return doGet("suunivid=" + univid).getSunetId();
    }

    public boolean isActive(final String sunetid) {
        return doGet("susunetid=" + sunetid).isActive();
    }

    private LDAPData doGet(final String lookupFilter) {
        LDAPData ldapData = null;
        try {
            Subject subject = this.subjectSource.getSubject();
            List<LDAPData> data = Subject.doAs(subject, new PrivilegedAction<List<LDAPData>>() {

                @Override
                public List<LDAPData> run() {
                    return LDAPDataAccess.this.ldapTemplate.search("", lookupFilter,
                            LDAPDataAccess.this.attributesMapper);
                }
            });
            if (data.isEmpty()) {
                ldapData = LDAPData.NULL;
            } else {
                ldapData = data.get(0);
            }
        } catch (SecurityException | NamingException e) {
            LDAPDataAccess.LOG.error("failed to get ldap data", e);
            ldapData = LDAPData.NULL;
        }
        return ldapData;
    }
}
