package edu.stanford.irt.laneweb.voyager;

import edu.stanford.irt.laneweb.util.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VoyagerLogin {

    private static final String BASE_URL_PATTERN = "http://{0}.stanford.edu/cgi-bin/Pwebrecon.cgi?";

    private static final String CHECK_ID_SQL = "SELECT COUNT(*) FROM {0}.PATRON WHERE INSTITUTION_ID = ?";

    private static final String CLEAR_SESSION_SQL = "DELETE FROM {0}.WOPAC_PID_PATRON_KEYS WHERE PATRON_KEY = ? OR PID = ?";

    private static final String CREATE_SESSION_SQL = "INSERT INTO {0}.WOPAC_PID_PATRON_KEYS (PATRON_KEY, PID) VALUES (?,?)";

    private static final Pattern PID_PATTERN = Pattern.compile("[\\w0-9-_]+");

    private String baseURL;

    private String checkIdSQL;

    private String clearSessionSQL;

    private String createSessionSQL;

    private DataSource dataSource;

    private String errorURL;

    private final Logger log = LoggerFactory.getLogger(VoyagerLogin.class);

    public String getVoyagerURL(final String univId, final String pid, final String queryString) {
        String voyagerURL = this.errorURL;
        if (pid == null || !PID_PATTERN.matcher(pid).matches()) {
            this.log.error("bad pid: " + pid);
        } else if (univId == null || univId.length() == 0) {
            this.log.error("bad univId: " + univId);
        } else {
            String voyagerUnivId = "0" + univId; // voyager data prepends 0
            Connection conn = null;
            PreparedStatement checkStmt = null;
            PreparedStatement clearStmt = null;
            PreparedStatement createStmt = null;
            ResultSet rs = null;
            try {
                conn = this.dataSource.getConnection();
                checkStmt = conn.prepareStatement(this.checkIdSQL);
                checkStmt.setString(1, voyagerUnivId);
                rs = checkStmt.executeQuery();
                rs.next();
                if (rs.getInt(1) > 0) { // univid found so write to voyager tables
                    clearStmt = conn.prepareStatement(this.clearSessionSQL);
                    clearStmt.setString(1, voyagerUnivId);
                    clearStmt.setString(2, pid);
                    clearStmt.executeUpdate();
                    createStmt = conn.prepareStatement(this.createSessionSQL);
                    createStmt.setString(1, voyagerUnivId);
                    createStmt.setString(2, pid);
                    createStmt.executeUpdate();
                    voyagerURL = this.baseURL.concat(queryString).concat("&authenticate=Y");
                } else {
                    this.log.error("unable to find univId in voyager: " + univId);
                }
            } catch (SQLException e) {
                this.log.error(e.getMessage(), e);
                voyagerURL = this.errorURL;
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

    public void setDataSource(final DataSource dataSource) {
        if (null == dataSource) {
            throw new IllegalArgumentException("null dataSource");
        }
        this.dataSource = dataSource;
    }

    public void setErrorURL(final String errorURL) {
        this.errorURL = errorURL;
    }

    public void setVoyagerDatabase(final String voyagerDatabase) {
        this.baseURL = MessageFormat.format(BASE_URL_PATTERN, voyagerDatabase);
        this.checkIdSQL = MessageFormat.format(CHECK_ID_SQL, voyagerDatabase);
        this.clearSessionSQL = MessageFormat.format(CLEAR_SESSION_SQL, voyagerDatabase);
        this.createSessionSQL = MessageFormat.format(CREATE_SESSION_SQL, voyagerDatabase);
    }
}
