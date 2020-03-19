package edu.stanford.irt.laneweb.user;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
        this.ldapTemplate = mock(LdapTemplate.class);
        this.subjectSource = mock(SubjectSource.class);
        this.lDAPDataAccess = new LDAPDataAccess(this.ldapTemplate, this.subjectSource, Collections.emptyList());
        this.ldapData = mock(LDAPData.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetActiveSunetId() {
        expect(this.subjectSource.getSubject()).andReturn(this.subject);
        expect(this.ldapTemplate.search(eq(""), eq("suunivid=univid"),
                (AttributesMapper<LDAPData>) isA(AttributesMapper.class)))
                        .andReturn(Collections.singletonList(this.ldapData));
        expect(this.ldapData.getSunetId()).andReturn("sunetid");
        replay(this.subjectSource, this.ldapTemplate, this.ldapData);
        assertEquals("sunetid", this.lDAPDataAccess.getActiveSunetId("univid"));
        verify(this.subjectSource, this.ldapTemplate, this.ldapData);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetActiveSunetIdNoResults() {
        expect(this.subjectSource.getSubject()).andReturn(this.subject);
        expect(this.ldapTemplate.search(eq(""), eq("suunivid=univid"),
                (AttributesMapper<LDAPData>) isA(AttributesMapper.class))).andReturn(Collections.emptyList());
        replay(this.subjectSource, this.ldapTemplate, this.ldapData);
        assertNull(this.lDAPDataAccess.getActiveSunetId("univid"));
        verify(this.subjectSource, this.ldapTemplate, this.ldapData);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetActiveSunetIdNotActive() {
        expect(this.subjectSource.getSubject()).andReturn(this.subject);
        expect(this.ldapTemplate.search(eq(""), eq("suunivid=univid"),
                (AttributesMapper<LDAPData>) isA(AttributesMapper.class)))
                        .andReturn(Collections.singletonList(this.ldapData));
        expect(this.ldapData.getSunetId()).andReturn(null);
        replay(this.subjectSource, this.ldapTemplate, this.ldapData);
        assertNull(this.lDAPDataAccess.getActiveSunetId("univid"));
        verify(this.subjectSource, this.ldapTemplate, this.ldapData);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testIsActive() {
        expect(this.subjectSource.getSubject()).andReturn(this.subject);
        expect(this.ldapTemplate.search(eq(""), eq("susunetid=sunetid"),
                (AttributesMapper<LDAPData>) isA(AttributesMapper.class)))
                        .andReturn(Collections.singletonList(this.ldapData));
        expect(this.ldapData.isActive()).andReturn(true);
        replay(this.subjectSource, this.ldapTemplate, this.ldapData);
        assertTrue(this.lDAPDataAccess.isActive("sunetid"));
        verify(this.subjectSource, this.ldapTemplate, this.ldapData);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testThrowCommunicationExceptionNounivid() {
        expect(this.subjectSource.getSubject()).andReturn(this.subject);
        expect(this.ldapTemplate.search(eq(""), eq("suunivid=12345678"),
                (AttributesMapper<LDAPData>) isA(AttributesMapper.class))).andThrow(new CommunicationException(null));
        replay(this.subjectSource, this.ldapTemplate);
        this.lDAPDataAccess.getActiveSunetId("12345678");
        verify(this.subjectSource, this.ldapTemplate);
    }
}
