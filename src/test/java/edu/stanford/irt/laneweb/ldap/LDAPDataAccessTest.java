package edu.stanford.irt.laneweb.ldap;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import javax.security.auth.Subject;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.ldap.CommunicationException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;

public class LDAPDataAccessTest {

    private LDAPDataAccess lDAPDataAccess;

    private LdapTemplate ldapTemplate;

    private Subject subject = new Subject();

    private SubjectSource subjectSource;

    private Logger log;

    @Before
    public void setUp() {
        this.ldapTemplate = createMock(LdapTemplate.class);
        this.log = createMock(Logger.class);
        this.subjectSource = createMock(SubjectSource.class);
        this.lDAPDataAccess = new LDAPDataAccess(this.ldapTemplate, this.log, this.subjectSource);
    }

    @Test
    public void testGetUserInfo() {
        expect(this.subjectSource.getSubject()).andReturn(this.subject);
        expect(this.ldapTemplate.search(eq(""), eq("susunetid=ditenus"), isA(AttributesMapper.class))).andReturn(null);
        replay(this.subjectSource, this.ldapTemplate);
        this.lDAPDataAccess.getLdapDataForSunetid("ditenus");
        verify(this.subjectSource, this.ldapTemplate);
    }

    @Test
    public void testThrowCommunicationException() {
        expect(this.subjectSource.getSubject()).andReturn(this.subject);
        expect(this.ldapTemplate.search(eq(""), eq("susunetid=ditenus"), isA(AttributesMapper.class))).andThrow(
                new CommunicationException(null));
        replay(this.subjectSource, this.ldapTemplate);
        LDAPData data = this.lDAPDataAccess.getLdapDataForSunetid("ditenus");
        assertEquals("ditenus", data.getName());
        verify(this.subjectSource, this.ldapTemplate);
    }

    @Test
    public void testThrowCommunicationExceptionNounivid() {
        expect(this.subjectSource.getSubject()).andReturn(this.subject);
        expect(this.ldapTemplate.search(eq(""), eq("suunivid=12345678"), isA(AttributesMapper.class))).andThrow(
                new CommunicationException(null));
        replay(this.subjectSource, this.ldapTemplate);
        LDAPData data = this.lDAPDataAccess.getLdapDataForUnivid("12345678");
        assertEquals(null, data.getName());
        verify(this.subjectSource, this.ldapTemplate);
    }
}
