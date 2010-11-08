package edu.stanford.irt.laneweb.voyager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.irt.laneweb.JdbcUtils;

public class VoyagerLogin {
    
    private static final Pattern PID_PATTERN = Pattern.compile("[\\w0-9-_]+");

    private static final String BASE_URL = "http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?";

    private static final String CLEAR_SESSION_SQL = "DELETE FROM LMLDB.WOPAC_PID_PATRON_KEYS WHERE PATRON_KEY = ? OR PID = ?";

    private static final String CREATE_SESSION_SQL = "INSERT INTO LMLDB.WOPAC_PID_PATRON_KEYS (PATRON_KEY, PID) VALUES (?,?)";

    private static final String ERROR_URL = "/voyagerError.html";

    private static final Logger LOGGER = LoggerFactory.getLogger(VoyagerLogin.class);

    private DataSource dataSource;

    public String getVoyagerURL(final String univId, final String pid, final String queryString) {
        if (null == pid || !PID_PATTERN.matcher(pid).matches()) {
            LOGGER.error("bad pid: " + pid);
            return ERROR_URL;
        }
        if (null == univId || univId.length() == 0) {
            LOGGER.error("bad univId: " + univId);
            return ERROR_URL;
        }
        String voyagerUnivId = "0" + univId; // voyager data prepends 0
        Connection conn = null;
        PreparedStatement clearStmt = null;
        PreparedStatement createStmt = null;
        try {
            conn = this.dataSource.getConnection();
            clearStmt = conn.prepareStatement(CLEAR_SESSION_SQL);
            clearStmt.setString(1, voyagerUnivId);
            clearStmt.setString(2, pid);
            clearStmt.executeUpdate();
            createStmt = conn.prepareStatement(CREATE_SESSION_SQL);
            createStmt.setString(1, voyagerUnivId);
            createStmt.setString(2, pid);
            createStmt.executeUpdate();
            return BASE_URL.concat(queryString).concat("&authenticate=Y");
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            return ERROR_URL;
        } finally {
            JdbcUtils.closeStatement(clearStmt);
            JdbcUtils.closeStatement(createStmt);
            JdbcUtils.closeConnection(conn);
        }
    }

    public void setDataSource(final DataSource dataSource) {
        if (null == dataSource) {
            throw new IllegalArgumentException("null dataSource");
        }
        this.dataSource = dataSource;
    }
}
