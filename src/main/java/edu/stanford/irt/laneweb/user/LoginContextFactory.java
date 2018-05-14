package edu.stanford.irt.laneweb.user;

import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

public class LoginContextFactory {
    
    private String loginContextName;

    public LoginContextFactory(String loginContextName) {
        this.loginContextName = loginContextName;
    }
    
    public LoginContext getLoginContext() throws LoginException {
        return new LoginContext(this.loginContextName);
    }
}
