package edu.stanford.irt.laneweb.voyager;

import edu.stanford.irt.laneweb.util.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VoyagerLogin {

    private static final String BASE_URL = "http://?.stanford.edu/cgi-bin/Pwebrecon.cgi?";

    private static final String CLEAR_SESSION_SQL_1 = "DELETE FROM ";

    private static final String CLEAR_SESSION_SQL_2 = ".WOPAC_PID_PATRON_KEYS WHERE PATRON_KEY = ? OR PID = ?";

    private static final String CREATE_SESSION_SQL_1 = "INSERT INTO ";

    private static final String CREATE_SESSION_SQL_2 = ".WOPAC_PID_PATRON_KEYS (PATRON_KEY, PID) VALUES (?,?)";

    private static final String JACKSON_ERROR_URL = "http://jbldb.stanford.edu/login-error.html";

    private static final String LANE_ERROR_URL = "/voyagerError.html";

    private static final Pattern PID_PATTERN = Pattern.compile("[\\w0-9-_]+");

    private static final Pattern VOYDB_PATTERN = Pattern.compile("^(lm|jb)ldb$");

    private DataSource dataSource;

    private String errorUrl;

    private final Logger log = LoggerFactory.getLogger(VoyagerLogin.class);

    public String getVoyagerURL(final String voyagerDatabase, final String univId, final String pid,
            final String queryString) {
        this.errorUrl = LANE_ERROR_URL;
        if (null == voyagerDatabase || !VOYDB_PATTERN.matcher(voyagerDatabase).matches()) {
            this.log.error("bad voyagerDatabase: " + voyagerDatabase);
            return this.errorUrl;
        }
        if ("jbldb".equals(voyagerDatabase)) {
            this.errorUrl = JACKSON_ERROR_URL;
        }
        if (null == pid || !PID_PATTERN.matcher(pid).matches()) {
            this.log.error("bad pid: " + pid);
            return this.errorUrl;
        }
        if (null == univId || univId.length() == 0) {
            this.log.error("bad univId: " + univId);
            return this.errorUrl;
        }
        String voyagerUnivId = "0" + univId; // voyager data prepends 0
        Connection conn = null;
        PreparedStatement clearStmt = null;
        PreparedStatement createStmt = null;
        try {
            conn = this.dataSource.getConnection();
            clearStmt = conn.prepareStatement(CLEAR_SESSION_SQL_1 + voyagerDatabase + CLEAR_SESSION_SQL_2);
            clearStmt.setString(1, voyagerUnivId);
            clearStmt.setString(2, pid);
            clearStmt.executeUpdate();
            createStmt = conn.prepareStatement(CREATE_SESSION_SQL_1 + voyagerDatabase + CREATE_SESSION_SQL_2);
            createStmt.setString(1, voyagerUnivId);
            createStmt.setString(2, pid);
            createStmt.executeUpdate();
            return BASE_URL.replaceFirst("\\?", voyagerDatabase).concat(queryString).concat("&authenticate=Y");
        } catch (SQLException e) {
            this.log.error(e.getMessage(), e);
            return this.errorUrl;
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
