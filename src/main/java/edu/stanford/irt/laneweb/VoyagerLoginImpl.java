package edu.stanford.irt.laneweb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameterizable;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.Request;

import edu.stanford.irt.directory.LDAPPerson;

public class VoyagerLoginImpl extends AbstractLogEnabled implements VoyagerLogin, ThreadSafe, Parameterizable {

    private static final String CLEAR_SESSION_SQL = "DELETE FROM LMLDB.WOPAC_PID_PATRON_KEYS WHERE ";

    private static final String CREATE_SESSION_SQL = "INSERT INTO LMLDB.WOPAC_PID_PATRON_KEYS (PATRON_KEY, PID) VALUES ";

    private String baseUrl;

    private String errorUrl;

    public void parameterize(final Parameters param) throws ParameterException {
        this.baseUrl = param.getParameter("voyagerBaseURL");
        this.errorUrl = param.getParameter("voyagerErrorURL");
    }

    private String validation(final LDAPPerson ldapPerson, final Request request) {
        if (ldapPerson == null) {
            return "ldapPerson";
        }
        if (request.getParameter("PID") == null || request.getParameter("PID").length() == 0
                || !request.getParameter("PID").matches("[0-9a-zA-Z]")) {
            return "PID";
        }
        if (ldapPerson.getUnivId() == null || ldapPerson.getUnivId().length() == 0) {
            return "univId";
        }
        return null;
    }

    public String initPatronSession(final LDAPPerson ldapPerson, final Request request, final Connection conn)
            throws ProcessingException, SQLException {
        String error = validation(ldapPerson, request);
        if (error != null) {
            return this.errorUrl.concat(error);
        }

        String univId = "0" + ldapPerson.getUnivId(); // voyager data prepends 0
        String voyagerPid = request.getParameter("PID");

        boolean clearedSession = clearPatronSession(conn, univId, voyagerPid);
        boolean createdSession = createPatronSession(conn, univId, voyagerPid);

        if (false == clearedSession || false == createdSession) {
            return this.errorUrl.concat("database");
        }

        if (null != conn) {
            conn.close();
        }

        return this.baseUrl.concat(request.getQueryString()).concat("&authenticate=Y");
    }

    private boolean clearPatronSession(final Connection conn, final String univId, final String voyagerPid)
            throws ProcessingException {
        PreparedStatement stmt = null;
        int rs = -1;
        try {
            stmt = conn.prepareStatement(CLEAR_SESSION_SQL + " PATRON_KEY = '" + univId + "' OR PID = '" + voyagerPid + "'");
            rs = stmt.executeUpdate();
        } catch (SQLException e) {
            throw new ProcessingException(e);
        } finally {
            try {
                if (null != stmt) {
                    stmt.close();
                }
            } catch (SQLException e) {
                throw new ProcessingException(e);
            }
        }
        return rs >= 0;
    }

    private boolean createPatronSession(final Connection conn, final String univId, final String voyagerPid)
            throws ProcessingException {
        PreparedStatement stmt = null;
        int rs = -1;
        try {
            stmt = conn.prepareStatement(CREATE_SESSION_SQL + "('" + univId + "','" + voyagerPid + "')");
            rs = stmt.executeUpdate();
        } catch (SQLException e) {
            throw new ProcessingException(e);
        } finally {
            try {
                if (null != stmt) {
                    stmt.close();
                }
            } catch (SQLException e) {
                throw new ProcessingException(e);
            }
        }
        return rs >= 0;
    }

}
