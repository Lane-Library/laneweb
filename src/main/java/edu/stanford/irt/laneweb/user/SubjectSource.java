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

    private static final Logger LOG = LoggerFactory.getLogger("error handler");

    private String name;

    private Subject subject;

    private KerberosTicket ticket;

    public SubjectSource(final String name) {
        this.name = name;
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

    // protected method so that tests can mock the LoginContext
    protected LoginContext getLoginContext(final String name) throws LoginException {
        return new LoginContext(name);
    }

    private void authenticateIfNecessary() throws LoginException {
        if (null == this.subject || null == this.ticket || !this.ticket.isCurrent()) {
            LoginContext loginContext = getLoginContext(this.name);
            loginContext.login();
            this.subject = loginContext.getSubject();
            for (KerberosTicket subjectTicket : this.subject.getPrivateCredentials(KerberosTicket.class)) {
                this.ticket = subjectTicket;
            }
        }
    }
}
