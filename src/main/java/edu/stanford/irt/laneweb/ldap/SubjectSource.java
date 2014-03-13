package edu.stanford.irt.laneweb.ldap;

import javax.security.auth.Subject;
import javax.security.auth.kerberos.KerberosTicket;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.slf4j.Logger;

/**
 * Provides a Subject with Kerberos authentication.
 */
public class SubjectSource {

    private Logger log;

    private String name;

    private Subject subject;

    private KerberosTicket ticket;
    
    public SubjectSource(final String name, final Logger log) {
        this.name = name;
        this.log = log;
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
            this.log.error(e.getMessage(), e);
            this.subject = null;
        }
        return this.subject;
    }

    private void authenticateIfNecessary() throws LoginException {
        if (null == this.subject || null == this.ticket || !this.ticket.isCurrent()) {
            LoginContext loginContext = new LoginContext(this.name);
            loginContext.login();
            this.subject = loginContext.getSubject();
            for (KerberosTicket subjectTicket : this.subject.getPrivateCredentials(KerberosTicket.class)) {
                this.ticket = subjectTicket;
            }
        }
    }
}
