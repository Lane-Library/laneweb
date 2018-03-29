package edu.stanford.irt.laneweb.user;

import javax.security.auth.Subject;
import javax.security.auth.kerberos.KerberosTicket;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides a Subject with Kerberos authentication.
 */
public class SubjectSource {

    private static final Logger LOG = LoggerFactory.getLogger(SubjectSource.class);

    private Subject subject;

    private KerberosTicket ticket;

    private LoginContextFactory loginContextFactory;
    
    public SubjectSource(final LoginContextFactory loginContextFactory) {
        this.loginContextFactory = loginContextFactory;
    }

    /**
     * returns the kerberos authorized Subject
     *
     * @return the authorized Subject
     */
    public synchronized Subject getSubject() {
        try {
            authenticateIfNecessary();
        } catch (LoginException e) {
            LOG.error(e.getMessage(), e);
            this.subject = null;
        }
        return this.subject;
    }

    private void authenticateIfNecessary() throws LoginException {
        if (null == this.subject || null == this.ticket || !this.ticket.isCurrent()) {
            LoginContext loginContext = this.loginContextFactory.getLoginContext();
            loginContext.login();
            this.subject = loginContext.getSubject();
            for (KerberosTicket subjectTicket : this.subject.getPrivateCredentials(KerberosTicket.class)) {
                this.ticket = subjectTicket;
            }
        }
    }
}
