package edu.stanford.irt.laneweb.user;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.security.sasl.Sasl;

import org.springframework.ldap.core.support.DirContextAuthenticationStrategy;

public class GSSAPIAuthenticationStrategy implements DirContextAuthenticationStrategy {

    public DirContext processContextAfterCreation(final DirContext ctx, final String userDn, final String password) {
        return ctx;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void setupEnvironment(final Hashtable env, final String userDn, final String password) {
        env.put(Context.SECURITY_AUTHENTICATION, "GSSAPI");
        env.put(Sasl.QOP, "auth-conf");
    }
}
