package edu.stanford.irt.laneweb.voyager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VoyagerLogin {

    private static final String BASE_URL = "http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?";

    private static final String CHECK_ID_SQL = "SELECT COUNT(*) FROM LMLDB.PATRON WHERE INSTITUTION_ID = ?";

    private static final String CLEAR_SESSION_SQL = "DELETE FROM LMLDB.WOPAC_PID_PATRON_KEYS WHERE PATRON_KEY = ? OR PID = ?";

    private static final String CREATE_SESSION_SQL = "INSERT INTO LMLDB.WOPAC_PID_PATRON_KEYS (PATRON_KEY, PID) VALUES (?,?)";

    private static final String ERROR_URL = "/voyagerError.html";

    private static final Logger LOG = LoggerFactory.getLogger(VoyagerLogin.class);

    private static final int PID = 2;

    private static final Pattern PID_PATTERN = Pattern.compile("[\\w0-9-_]+");

    private static final int UNIV_ID = 1;

    private DataSource dataSource;

    public VoyagerLogin(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String getVoyagerURL(final String univId, final String pid, final String queryString) {
        String voyagerURL = ERROR_URL;
        if (pid == null || !PID_PATTERN.matcher(pid).matches()) {
            LOG.error("bad pid: {}", pid);
        } else if (univId == null || univId.length() == 0) {
            LOG.error("bad univId: {}", univId);
        } else {
            // voyager data prepends 0
            String voyagerUnivId = "0" + univId;
            try {
                if (userInDatabase(voyagerUnivId)) {
                    updateDatabase(voyagerUnivId, pid);
                    voyagerURL = BASE_URL.concat(queryString).concat("&authenticate=Y");
                } else {
                    LOG.error("unable to find univId in voyager: {}", univId);
                }
            } catch (SQLException e) {
                LOG.error(e.getMessage(), e);
                voyagerURL = ERROR_URL;
            }
        }
        return voyagerURL;
    }

    private void updateDatabase(final String voyagerUnivId, final String pid) throws SQLException {
        // univid found so write to voyager tables
        try (Connection conn = this.dataSource.getConnection();
                PreparedStatement clearStmt = conn.prepareStatement(CLEAR_SESSION_SQL);
                PreparedStatement createStmt = conn.prepareStatement(CREATE_SESSION_SQL)) {
            clearStmt.setString(UNIV_ID, voyagerUnivId);
            clearStmt.setString(PID, pid);
            clearStmt.executeUpdate();
            createStmt.setString(UNIV_ID, voyagerUnivId);
            createStmt.setString(PID, pid);
            createStmt.executeUpdate();
        }
    }

    private boolean userInDatabase(final String voyagerUnivId) throws SQLException {
        try (Connection conn = this.dataSource.getConnection();
                PreparedStatement checkStmt = conn.prepareStatement(CHECK_ID_SQL)) {
            checkStmt.setString(UNIV_ID, voyagerUnivId);
            try (ResultSet rs = checkStmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
}
