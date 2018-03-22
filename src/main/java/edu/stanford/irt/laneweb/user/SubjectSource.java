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

    private LoginContext loginContext;

    private String name;

    private Subject subject;

    private KerberosTicket ticket;

    public SubjectSource(final LoginContext loginContext) {
        this.loginContext = loginContext;
    }

    /**
     * Although preferable to inject the LoginContext, for the sake of simplifying integraton testing by not requiring a
     * jaas configuration will lazy initialize it using this constructor.
     * 
     * @param name
     */
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

    private void authenticateIfNecessary() throws LoginException {
        if (this.loginContext == null) {
            this.loginContext = new LoginContext(this.name);
        }
        if (null == this.subject || null == this.ticket || !this.ticket.isCurrent()) {
            this.loginContext.login();
            this.subject = this.loginContext.getSubject();
            for (KerberosTicket subjectTicket : this.subject.getPrivateCredentials(KerberosTicket.class)) {
                this.ticket = subjectTicket;
            }
        }
    }
}
