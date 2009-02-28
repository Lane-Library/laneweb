package edu.stanford.irt.laneweb.voyager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import edu.stanford.irt.laneweb.user.User;

public class VoyagerLogin {

    private static final String BASE_URL = "http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?";

    private static final String CLEAR_SESSION_SQL = "DELETE FROM LMLDB.WOPAC_PID_PATRON_KEYS WHERE PATRON_KEY = ? OR PID = ?";

    private static final String CREATE_SESSION_SQL = "INSERT INTO LMLDB.WOPAC_PID_PATRON_KEYS (PATRON_KEY, PID) VALUES (?,?)";

    private static final String ERROR_URL = "/voyagerError.html";

    private DataSource dataSource;

    private Logger logger = Logger.getLogger(VoyagerLogin.class);

    public String getVoyagerURL(final User person, final String pid, final String queryString) {
        if ((null == pid) || !pid.matches("[\\w0-9-_]+")) {
            this.logger.error("bad pid: " + pid);
            return ERROR_URL;
        }
        if (null == person) {
            this.logger.error("null person");
            return ERROR_URL;
        }
        String univId = person.getUnivId();
        if ((null == univId) || (univId.length() == 0)) {
            this.logger.error("bad univId: " + univId);
            return ERROR_URL;
        }
        univId = "0" + univId; // voyager data prepends 0
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = this.dataSource.getConnection();
            stmt = conn.prepareStatement(CLEAR_SESSION_SQL);
            stmt.setString(1, univId);
            stmt.setString(2, pid);
            stmt.executeUpdate();
            stmt = conn.prepareStatement(CREATE_SESSION_SQL);
            stmt.setString(1, univId);
            stmt.setString(2, pid);
            stmt.executeUpdate();
            return BASE_URL.concat(queryString).concat("&authenticate=Y");
        } catch (SQLException e) {
            this.logger.error(e.getMessage(), e);
            return ERROR_URL;
        } finally {
            if (null != stmt) {
                try {
                    stmt.close();
                } catch (SQLException e1) {
                    throw new RuntimeException(e1);
                }
            }
            if (null != conn) {
                try {
                    conn.close();
                } catch (SQLException e1) {
                    throw new RuntimeException(e1);
                }
            }
        }
    }

    public void setDataSource(final DataSource dataSource) {
        if (null == dataSource) {
            throw new IllegalArgumentException("null dataSource");
        }
        this.dataSource = dataSource;
    }
}
