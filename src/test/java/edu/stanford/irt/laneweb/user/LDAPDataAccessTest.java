package edu.stanford.irt.laneweb.user;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Collections;

import javax.security.auth.Subject;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ldap.CommunicationException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;

public class LDAPDataAccessTest {

    private LDAPData ldapData;

    private LDAPDataAccess lDAPDataAccess;

    private LdapTemplate ldapTemplate;

    private Subject subject = new Subject();

    private SubjectSource subjectSource;

    @Before
    public void setUp() {
        this.ldapTemplate = createMock(LdapTemplate.class);
        this.subjectSource = createMock(SubjectSource.class);
        this.lDAPDataAccess = new LDAPDataAccess(this.ldapTemplate, this.subjectSource, Collections.emptySet());
        this.ldapData = createMock(LDAPData.class);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetActiveSunetId() {
        assertNotNull(this.lDAPDataAccess.getActiveSunetId("univid"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetUserInfo() {
        expect(this.subjectSource.getSubject()).andReturn(this.subject);
        expect(this.ldapTemplate.search(eq(""), eq("susunetid=ditenus"),
                (AttributesMapper<LDAPData>) isA(AttributesMapper.class)))
                        .andReturn(Collections.singletonList(this.ldapData));
        replay(this.subjectSource, this.ldapTemplate);
        assertSame(this.ldapData, this.lDAPDataAccess.getLdapDataForSunetid("ditenus"));
        verify(this.subjectSource, this.ldapTemplate);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetUserInfoEmpty() {
        expect(this.subjectSource.getSubject()).andReturn(this.subject);
        expect(this.ldapTemplate.search(eq(""), eq("susunetid=ditenus"),
                (AttributesMapper<LDAPData>) isA(AttributesMapper.class))).andReturn(Collections.emptyList());
        replay(this.subjectSource, this.ldapTemplate);
        this.lDAPDataAccess.getLdapDataForSunetid("ditenus");
        verify(this.subjectSource, this.ldapTemplate);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetUserInfoNull() {
        expect(this.subjectSource.getSubject()).andReturn(this.subject);
        expect(this.ldapTemplate.search(eq(""), eq("susunetid=ditenus"),
                (AttributesMapper<LDAPData>) isA(AttributesMapper.class))).andReturn(null);
        replay(this.subjectSource, this.ldapTemplate);
        this.lDAPDataAccess.getLdapDataForSunetid("ditenus");
        verify(this.subjectSource, this.ldapTemplate);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIsActive() {
        assertTrue(this.lDAPDataAccess.isActive("sunetid"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testThrowCommunicationException() {
        expect(this.subjectSource.getSubject()).andReturn(this.subject);
        expect(this.ldapTemplate.search(eq(""), eq("susunetid=ditenus"),
                (AttributesMapper<LDAPData>) isA(AttributesMapper.class))).andThrow(new CommunicationException(null));
        replay(this.subjectSource, this.ldapTemplate);
        LDAPData data = this.lDAPDataAccess.getLdapDataForSunetid("ditenus");
        assertEquals("ditenus", data.getName());
        verify(this.subjectSource, this.ldapTemplate);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testThrowCommunicationExceptionNounivid() {
        expect(this.subjectSource.getSubject()).andReturn(this.subject);
        expect(this.ldapTemplate.search(eq(""), eq("suunivid=12345678"),
                (AttributesMapper<LDAPData>) isA(AttributesMapper.class))).andThrow(new CommunicationException(null));
        replay(this.subjectSource, this.ldapTemplate);
        LDAPData data = this.lDAPDataAccess.getLdapDataForUnivid("12345678");
        assertEquals(null, data.getName());
        verify(this.subjectSource, this.ldapTemplate);
    }
}
