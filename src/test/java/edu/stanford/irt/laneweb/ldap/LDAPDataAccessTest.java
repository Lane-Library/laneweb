package edu.stanford.irt.laneweb.ldap;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import javax.security.auth.Subject;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ldap.CommunicationException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;

public class LDAPDataAccessTest {

    private LDAPDataAccess lDAPDataAccess;

    private LdapTemplate ldapTemplate;

    private Subject subject = new Subject();

    private SubjectSource subjectSource;

    @Before
    public void setUp() {
        this.subjectSource = createMock(SubjectSource.class);
        this.ldapTemplate = createMock(LdapTemplate.class);
        this.lDAPDataAccess = new LDAPDataAccess();
        this.lDAPDataAccess.setSubjectSource(this.subjectSource);
        this.lDAPDataAccess.setLdapTemplate(this.ldapTemplate);
    }

    @Test
    public void testGetUserInfo() {
        expect(this.subjectSource.getSubject()).andReturn(this.subject);
        expect(this.ldapTemplate.search(eq(""), eq("susunetid=ditenus"), isA(AttributesMapper.class))).andReturn(null);
        replay(this.subjectSource, this.ldapTemplate);
        this.lDAPDataAccess.getLdapData("ditenus");
        verify(this.subjectSource, this.ldapTemplate);
    }

    @Test
    public void testThrowCommunicationException() {
        expect(this.subjectSource.getSubject()).andReturn(this.subject);
        expect(this.ldapTemplate.search(eq(""), eq("susunetid=ditenus"), isA(AttributesMapper.class))).andThrow(
                new CommunicationException(null));
        replay(this.subjectSource, this.ldapTemplate);
        LDAPData data = this.lDAPDataAccess.getLdapData("ditenus");
        assertEquals("ditenus", data.getName());
        verify(this.subjectSource, this.ldapTemplate);
    }
}
