package edu.stanford.irt.laneweb.user;

import java.util.Date;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.apache.log4j.Logger;

public class SubjectSource {
    
    private static final Logger LOGGER = Logger.getLogger(SubjectSource.class);

    private Date lastAuthentication;

    private String name;

    private Subject subject;

    public synchronized Subject getSubject() {
        LOGGER.error("getSubject()");
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
            if (null == this.subject) {
                LOGGER.error("null == this.subject");
            } else {
                LOGGER.error(this.lastAuthentication);
            }
            LoginContext loginContext = new LoginContext(this.name);
            loginContext.login();
            this.subject = loginContext.getSubject();
            this.lastAuthentication = new Date();
        }
    }
}
