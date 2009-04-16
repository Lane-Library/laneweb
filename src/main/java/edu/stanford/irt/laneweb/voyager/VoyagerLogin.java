package edu.stanford.irt.laneweb.voyager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import edu.stanford.irt.laneweb.JdbcUtils;
import edu.stanford.irt.laneweb.user.User;

public class VoyagerLogin {

    private static final String BASE_URL = "http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?";

    private static final String CLEAR_SESSION_SQL = "DELETE FROM LMLDB.WOPAC_PID_PATRON_KEYS WHERE PATRON_KEY = ? OR PID = ?";

    private static final String CREATE_SESSION_SQL = "INSERT INTO LMLDB.WOPAC_PID_PATRON_KEYS (PATRON_KEY, PID) VALUES (?,?)";

    private static final String ERROR_URL = "/voyagerError.html";

    private DataSource dataSource;

    private Logger logger = Logger.getLogger(VoyagerLogin.class);

    public String getVoyagerURL(final User person, final String pid, final String queryString) {
        String url = ERROR_URL;
        if ((null == pid) || !pid.matches("[\\w0-9-_]+")) {
            this.logger.error("bad pid: " + pid);
            return url;
        }
        if (null == person) {
            this.logger.error("null person");
            return url;
        }
        String univId = person.getUnivId();
        if ((null == univId) || (univId.length() == 0)) {
            this.logger.error("bad univId: " + univId);
            return url;
        }
        univId = "0" + univId; // voyager data prepends 0
        Connection conn = null;
        PreparedStatement clearStmt = null;
        PreparedStatement createStmt = null;
        try {
            conn = this.dataSource.getConnection();
            clearStmt = conn.prepareStatement(CLEAR_SESSION_SQL);
            clearStmt.setString(1, univId);
            clearStmt.setString(2, pid);
            clearStmt.executeUpdate();
            createStmt = conn.prepareStatement(CREATE_SESSION_SQL);
            createStmt.setString(1, univId);
            createStmt.setString(2, pid);
            createStmt.executeUpdate();
            url = BASE_URL.concat(queryString).concat("&authenticate=Y");
        } catch (SQLException e) {
            this.logger.error(e.getMessage(), e);
        } finally {
            JdbcUtils.closeStatement(clearStmt);
            JdbcUtils.closeStatement(createStmt);
            JdbcUtils.closeConnection(conn);
        }
        return url;
    }

    public void setDataSource(final DataSource dataSource) {
        if (null == dataSource) {
            throw new IllegalArgumentException("null dataSource");
        }
        this.dataSource = dataSource;
    }
}
