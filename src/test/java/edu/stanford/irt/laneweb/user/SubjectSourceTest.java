package edu.stanford.irt.laneweb.user;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.replay;
import static org.powermock.api.easymock.PowerMock.verify;

import java.util.Collections;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.kerberos.KerberosTicket;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Subject.class)
public class SubjectSourceTest {

    private static final class TestSubjectSource extends SubjectSource {

        private LoginContext loginContext;

        public TestSubjectSource(final String name, final LoginContext loginContext) {
            super(name);
            this.loginContext = loginContext;
        }

        @Override
        protected LoginContext getLoginContext(final String name) {
            return this.loginContext;
        }
    }

    private LoginContext loginContext;

    private Subject subject;

    private SubjectSource subjectSource;

    private KerberosTicket ticket;

    private Set<KerberosTicket> tickets;

    @Before
    public void setUp() {
        this.loginContext = createMock(LoginContext.class);
        this.subjectSource = new TestSubjectSource("name", this.loginContext);
        this.subject = createMock(Subject.class);
        this.ticket = createMock(KerberosTicket.class);
        this.tickets = Collections.singleton(this.ticket);
    }

    @Test
    public void testGetSubject() throws LoginException {
        this.loginContext.login();
        expect(this.loginContext.getSubject()).andReturn(this.subject);
        expect(this.subject.getPrivateCredentials(KerberosTicket.class)).andReturn(this.tickets);
        replay(this.loginContext, this.subject, this.ticket);
        assertEquals(this.subject, this.subjectSource.getSubject());
        verify(this.loginContext, this.subject, this.ticket);
    }

    @Test
    public void testGetSubjectNoTicket() throws LoginException {
        this.loginContext.login();
        expectLastCall().times(2);
        expect(this.loginContext.getSubject()).andReturn(this.subject).times(2);
        expect(this.subject.getPrivateCredentials(KerberosTicket.class)).andReturn(Collections.emptySet()).times(2);
        replay(this.loginContext, this.subject, this.ticket);
        assertEquals(this.subject, this.subjectSource.getSubject());
        assertEquals(this.subject, this.subjectSource.getSubject());
        verify(this.loginContext, this.subject, this.ticket);
    }

    @Test
    public void testGetSubjectTicketCurrent() throws LoginException {
        this.loginContext.login();
        expect(this.loginContext.getSubject()).andReturn(this.subject);
        expect(this.subject.getPrivateCredentials(KerberosTicket.class)).andReturn(this.tickets);
        expect(this.ticket.isCurrent()).andReturn(true);
        replay(this.loginContext, this.subject, this.ticket);
        assertEquals(this.subject, this.subjectSource.getSubject());
        assertEquals(this.subject, this.subjectSource.getSubject());
        verify(this.loginContext, this.subject, this.ticket);
    }

    @Test
    public void testGetSubjectTicketNotCurrent() throws LoginException {
        this.loginContext.login();
        expectLastCall().times(2);
        expect(this.loginContext.getSubject()).andReturn(this.subject).times(2);
        expect(this.subject.getPrivateCredentials(KerberosTicket.class)).andReturn(this.tickets).times(2);
        expect(this.ticket.isCurrent()).andReturn(false);
        replay(this.loginContext, this.subject, this.ticket);
        assertEquals(this.subject, this.subjectSource.getSubject());
        assertEquals(this.subject, this.subjectSource.getSubject());
        verify(this.loginContext, this.subject, this.ticket);
    }

    @Test
    public void testLoginException() throws LoginException {
        LoginException ex = new LoginException("oopsie");
        this.loginContext.login();
        expectLastCall().andThrow(ex);
        replay(this.loginContext);
        assertNull(this.subjectSource.getSubject());
        verify(this.loginContext);
    }
}
