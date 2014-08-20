package edu.stanford.irt.laneweb.voyager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.slf4j.Logger;

import edu.stanford.irt.laneweb.util.JdbcUtils;

public class VoyagerLogin {

    private static final String BASE_URL = "http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?";

    private static final String CHECK_ID_SQL = "SELECT COUNT(*) FROM LMLDB.PATRON WHERE INSTITUTION_ID = ?";

    private static final String CLEAR_SESSION_SQL = "DELETE FROM LMLDB.WOPAC_PID_PATRON_KEYS WHERE PATRON_KEY = ? OR PID = ?";

    private static final String CREATE_SESSION_SQL = "INSERT INTO LMLDB.WOPAC_PID_PATRON_KEYS (PATRON_KEY, PID) VALUES (?,?)";

    private static final String ERROR_URL = "/voyagerError.html";

    private static final Pattern PID_PATTERN = Pattern.compile("[\\w0-9-_]+");

    private DataSource dataSource;

    private final Logger log;

    public VoyagerLogin(final DataSource dataSource, final Logger log) {
        this.dataSource = dataSource;
        this.log = log;
    }

    public String getVoyagerURL(final String univId, final String pid, final String queryString) {
        String voyagerURL = ERROR_URL;
        if (pid == null || !PID_PATTERN.matcher(pid).matches()) {
            this.log.error("bad pid: " + pid);
        } else if (univId == null || univId.length() == 0) {
            this.log.error("bad univId: " + univId);
        } else {
            // voyager data prepends 0
            String voyagerUnivId = "0" + univId;
            Connection conn = null;
            PreparedStatement checkStmt = null;
            PreparedStatement clearStmt = null;
            PreparedStatement createStmt = null;
            ResultSet rs = null;
            try {
                conn = this.dataSource.getConnection();
                checkStmt = conn.prepareStatement(CHECK_ID_SQL);
                checkStmt.setString(1, voyagerUnivId);
                rs = checkStmt.executeQuery();
                rs.next();
                if (rs.getInt(1) > 0) {
                    // univid found so write to voyager tables
                    clearStmt = conn.prepareStatement(CLEAR_SESSION_SQL);
                    clearStmt.setString(1, voyagerUnivId);
                    clearStmt.setString(2, pid);
                    clearStmt.executeUpdate();
                    createStmt = conn.prepareStatement(CREATE_SESSION_SQL);
                    createStmt.setString(1, voyagerUnivId);
                    createStmt.setString(2, pid);
                    createStmt.executeUpdate();
                    voyagerURL = BASE_URL.concat(queryString).concat("&authenticate=Y");
                } else {
                    this.log.error("unable to find univId in voyager: " + univId);
                }
            } catch (SQLException e) {
                this.log.error(e.getMessage(), e);
                voyagerURL = ERROR_URL;
            } finally {
                JdbcUtils.closeResultSet(rs);
                JdbcUtils.closeStatement(checkStmt);
                JdbcUtils.closeStatement(clearStmt);
                JdbcUtils.closeStatement(createStmt);
                JdbcUtils.closeConnection(conn);
            }
        }
        return voyagerURL;
    }
}
