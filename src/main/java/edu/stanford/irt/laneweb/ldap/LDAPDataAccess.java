package edu.stanford.irt.laneweb.ldap;

import java.security.PrivilegedAction;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.security.auth.Subject;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.ldap.AuthenticationException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;

import edu.stanford.irt.laneweb.model.LanewebObjectModel;

public class LDAPDataAccess {

    private static final Logger LOGGER = Logger.getLogger(LDAPDataAccess.class);

    private LdapTemplate ldapTemplate;

    private SubjectSource subjectSource;

    public void getUserData(final LDAPData lDAPData, final HttpServletRequest request) {
        setLdapData(lDAPData, request);
    }

    public void setLdapTemplate(final LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    public void setSubjectSource(final SubjectSource subjectSource) {
        this.subjectSource = subjectSource;
    }

    private void setLdapData(final LDAPData lDAPData, final ServletRequest request) {
        if (null != request.getAttribute(LanewebObjectModel.SUNETID) && null == lDAPData.getName()) {
            Subject subject = this.subjectSource.getSubject();
            if (null != subject) {
                try {
                Subject.doAs(subject, new PrivilegedAction<LDAPData>() {

                    public LDAPData run() {
                        LDAPDataAccess.this.ldapTemplate.search("", "susunetid=" + request.getAttribute(LanewebObjectModel.SUNETID), new AttributesMapper() {

                            public Object mapFromAttributes(final Attributes attributes) throws NamingException {
                                Attribute currentAttribute = attributes.get("displayName");
                                if (null != currentAttribute) {
                                    lDAPData.setName((String) currentAttribute.get());
                                }
                                currentAttribute = attributes.get("suaffiliation");
                                if (null != currentAttribute) {
                                    lDAPData.setAffiliation((String) currentAttribute.get());
                                }
                                currentAttribute = attributes.get("suunivid");
                                if (null != currentAttribute) {
                                    lDAPData.setUnivId((String) currentAttribute.get());
                                }
                                return lDAPData;
                            }
                        });
                        return lDAPData;
                    }
                });
                } catch (AuthenticationException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
    }
}
