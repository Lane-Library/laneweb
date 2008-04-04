package edu.stanford.irt.laneweb;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.Request;

import edu.stanford.irt.directory.LDAPPerson;

public interface VoyagerLogin {

    public static final String ROLE = VoyagerLogin.class.getName();

    public String initPatronSession(LDAPPerson ldapPerson, Request request, Connection conn) throws ProcessingException,
            SQLException;

}
