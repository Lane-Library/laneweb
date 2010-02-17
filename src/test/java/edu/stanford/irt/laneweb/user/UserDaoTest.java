package edu.stanford.irt.laneweb.user;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;

import org.apache.cocoon.environment.Session;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;

import edu.stanford.irt.laneweb.model.LanewebObjectModel;

public class UserDaoTest {

    private LdapTemplate ldapTemplate;

    private HttpServletRequest request;

    private Session session;

    private SubjectSource subjectSource;

    private User user;
    
    private Subject subject = new Subject();

    private UserDao userDao;

    @Before
    public void setUp() {
        this.request = createMock(HttpServletRequest.class);
        this.session = createMock(Session.class);
        this.subjectSource = createMock(SubjectSource.class);
        this.ldapTemplate = createMock(LdapTemplate.class);
        this.user = createMock(User.class);
        this.userDao = new UserDao();
        this.userDao.setEzproxyKey("ezproxy");
        this.userDao.setSubjectSource(this.subjectSource);
        this.userDao.setLdapTemplate(this.ldapTemplate);
    }

    @Test
    public void testGetUserInfo() {
      expect(this.request.getAttribute(LanewebObjectModel.SUNETID)).andReturn("ditenus");
      expect(this.request.getRemoteAddr()).andReturn("127.0.0.1");
      expect(this.request.getHeader("X-FORWARDED-FOR")).andReturn(null);
      expect(this.request.getParameter(LanewebObjectModel.PROXY_LINKS)).andReturn(null);
      expect(this.request.getParameter(LanewebObjectModel.EMRID)).andReturn(null);
      expect(this.subjectSource.getSubject()).andReturn(this.subject);
      expect(this.ldapTemplate.search(eq(""), eq("susunetid=ditenus"), isA(AttributesMapper.class))).andReturn(null);
      replayMocks();
        User user = new User();
        this.userDao.getUserData(user, this.request);
        assertEquals(IPGroup.OTHER, user.getIPGroup());
        assertEquals("ditenus", user.getSunetId());
        verifyMocks();
    }

    private void replayMocks() {
        replay(this.user);
        replay(this.request);
        replay(this.session);
        replay(this.subjectSource);
        replay(this.ldapTemplate);
        
    }

    private void verifyMocks() {
        verify(this.user);
        verify(this.request);
        verify(this.session);
        verify(this.subjectSource);
        verify(this.ldapTemplate);
    }
}
