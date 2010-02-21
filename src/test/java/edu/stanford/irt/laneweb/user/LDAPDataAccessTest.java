package edu.stanford.irt.laneweb.user;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;

import org.apache.cocoon.environment.Session;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;

import edu.stanford.irt.laneweb.model.LanewebObjectModel;

public class LDAPDataAccessTest {

    private LdapTemplate ldapTemplate;

    private HttpServletRequest request;

    private Session session;

    private SubjectSource subjectSource;

    private LDAPData lDAPData;
    
    private Subject subject = new Subject();

    private LDAPDataAccess lDAPDataAccess;

    @Before
    public void setUp() {
        this.request = createMock(HttpServletRequest.class);
        this.session = createMock(Session.class);
        this.subjectSource = createMock(SubjectSource.class);
        this.ldapTemplate = createMock(LdapTemplate.class);
        this.lDAPData = createMock(LDAPData.class);
        this.lDAPDataAccess = new LDAPDataAccess();
        this.lDAPDataAccess.setSubjectSource(this.subjectSource);
        this.lDAPDataAccess.setLdapTemplate(this.ldapTemplate);
    }

    @Test
    public void testGetUserInfo() {
      expect(this.request.getAttribute(LanewebObjectModel.SUNETID)).andReturn("ditenus").atLeastOnce();
      expect(this.subjectSource.getSubject()).andReturn(this.subject);
      expect(this.ldapTemplate.search(eq(""), eq("susunetid=ditenus"), isA(AttributesMapper.class))).andReturn(null);
      replayMocks();
        LDAPData lDAPData = new LDAPData();
        this.lDAPDataAccess.getUserData(lDAPData, this.request);
        verifyMocks();
    }

    private void replayMocks() {
        replay(this.lDAPData);
        replay(this.request);
        replay(this.session);
        replay(this.subjectSource);
        replay(this.ldapTemplate);
        
    }

    private void verifyMocks() {
        verify(this.lDAPData);
        verify(this.request);
        verify(this.session);
        verify(this.subjectSource);
        verify(this.ldapTemplate);
    }
}
