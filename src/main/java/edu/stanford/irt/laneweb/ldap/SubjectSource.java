package edu.stanford.irt.laneweb.ldap;

import javax.security.auth.Subject;
import javax.security.auth.kerberos.KerberosTicket;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

/**
 * Provides a Subject with Kerberos authentication.
 */
public class SubjectSource {

    private String name;

    private Subject subject;

    private KerberosTicket ticket;

    /**
     * returns the kerberos authorized Subject
     * 
     * @return the authorized Subject
     */
    public synchronized Subject getSubject() {
        try {
            authenticateIfNecessary();
        } catch (LoginException e) {
            this.subject = null;
        }
        return this.subject;
    }

    public void setName(final String name) {
        this.name = name;
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
