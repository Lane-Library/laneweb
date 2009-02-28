package edu.stanford.irt.laneweb.user;

import java.util.Date;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

public class SubjectSource {

    private Date lastAuthentication;

    private Object lock = new Object();

    private String name;

    private Subject subject;

    public Subject getSubject() {
        try {
            authenticateIfNecessary();
        } catch (LoginException e) {
            return null;
        }
        return this.subject;
    }

    public void setName(final String name) {
        this.name = name;
    }

    private void authenticate() throws LoginException {
        LoginContext loginContext = new LoginContext(this.name);
        loginContext.login();
        this.subject = loginContext.getSubject();
        this.lastAuthentication = new Date();
    }

    private void authenticateIfNecessary() throws LoginException {
        long now = System.currentTimeMillis();
        synchronized (this.lock) {
            if (null == this.subject || this.lastAuthentication.getTime() + (1000 * 60 * 60 * 24) > now) {
                authenticate();
            }
        }
    }
}
