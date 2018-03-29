package edu.stanford.irt.laneweb.user;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Collections;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.kerberos.KerberosTicket;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.junit.Before;
import org.junit.Test;

public class SubjectSourceTest {

    private LoginContext loginContext;

    private LoginContextFactory loginContextFactory;

    private Subject subject;

    private SubjectSource subjectSource;

    private KerberosTicket ticket;

    private Set<KerberosTicket> tickets;

    @Before
    public void setUp() {
        this.loginContextFactory = mock(LoginContextFactory.class);
        this.subjectSource = new SubjectSource(this.loginContextFactory);
        this.loginContext = mock(LoginContext.class);
        this.ticket = mock(KerberosTicket.class);
        this.tickets = Collections.singleton(this.ticket);
        this.subject = new Subject(true, Collections.emptySet(), Collections.emptySet(), this.tickets);
    }

    @Test
    public void testGetSubject() throws LoginException {
        expect(this.loginContextFactory.getLoginContext()).andReturn(this.loginContext);
        this.loginContext.login();
        expect(this.loginContext.getSubject()).andReturn(this.subject);
        replay(this.loginContextFactory, this.loginContext, this.ticket);
        assertEquals(this.subject, this.subjectSource.getSubject());
        verify(this.loginContextFactory, this.loginContext, this.ticket);
    }

    @Test
    public void testGetSubjectNoTicket() throws LoginException {
        expect(this.loginContextFactory.getLoginContext()).andReturn(this.loginContext).times(2);
        this.loginContext.login();
        expectLastCall().times(2);
        Subject noticket = new Subject(true, Collections.emptySet(), Collections.emptySet(), Collections.emptySet());
        expect(this.loginContext.getSubject()).andReturn(noticket);
        expect(this.loginContext.getSubject()).andReturn(this.subject);
        replay(this.loginContextFactory, this.loginContext, this.ticket);
        assertEquals(noticket, this.subjectSource.getSubject());
        assertEquals(this.subject, this.subjectSource.getSubject());
        verify(this.loginContextFactory, this.loginContext, this.ticket);
    }

    @Test
    public void testGetSubjectTicketCurrent() throws LoginException {
        expect(this.loginContextFactory.getLoginContext()).andReturn(this.loginContext);
        this.loginContext.login();
        expect(this.loginContext.getSubject()).andReturn(this.subject);
        expect(this.ticket.isCurrent()).andReturn(true);
        replay(this.loginContextFactory, this.loginContext, this.ticket);
        assertEquals(this.subject, this.subjectSource.getSubject());
        assertEquals(this.subject, this.subjectSource.getSubject());
        verify(this.loginContextFactory, this.loginContext, this.ticket);
    }

    @Test
    public void testGetSubjectTicketNotCurrent() throws LoginException {
        expect(this.loginContextFactory.getLoginContext()).andReturn(this.loginContext).times(2);
        this.loginContext.login();
        expectLastCall().times(2);
        expect(this.loginContext.getSubject()).andReturn(this.subject).times(2);
        expect(this.ticket.isCurrent()).andReturn(false);
        replay(this.loginContextFactory, this.loginContext, this.ticket);
        assertEquals(this.subject, this.subjectSource.getSubject());
        assertEquals(this.subject, this.subjectSource.getSubject());
        verify(this.loginContextFactory, this.loginContext, this.ticket);
    }

    @Test
    public void testLoginException() throws LoginException {
        expect(this.loginContextFactory.getLoginContext()).andReturn(this.loginContext);
        LoginException ex = new LoginException("oopsie");
        this.loginContext.login();
        expectLastCall().andThrow(ex);
        replay(this.loginContextFactory, this.loginContext);
        assertNull(this.subjectSource.getSubject());
        verify(this.loginContextFactory, this.loginContext);
    }

    @Test
    public void testLoginFactoryException() throws LoginException {
        LoginException ex = new LoginException("oopsie");
        this.loginContextFactory.getLoginContext();
        expectLastCall().andThrow(ex);
        replay(this.loginContextFactory);
        assertNull(this.subjectSource.getSubject());
        verify(this.loginContextFactory);
    }
}
