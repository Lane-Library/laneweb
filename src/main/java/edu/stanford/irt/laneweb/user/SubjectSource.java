package edu.stanford.irt.laneweb.user;

import java.util.Date;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

public class SubjectSource {

    private Date lastAuthentication;

    private String name;

    private Subject subject;

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
        long now = System.currentTimeMillis();
        if (null == this.subject || this.lastAuthentication.getTime() + (1000 * 60 * 60 * 24) > now) {
            LoginContext loginContext = new LoginContext(this.name);
            loginContext.login();
            this.subject = loginContext.getSubject();
            this.lastAuthentication = new Date();
        }
    }
}
