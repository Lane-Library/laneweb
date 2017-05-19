package edu.stanford.irt.laneweb.voyager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import edu.stanford.irt.laneweb.LanewebException;

public class JDBCLoginService implements LoginService {

    private static final String CHECK_ID_SQL = "SELECT COUNT(*) FROM LMLDB.PATRON WHERE INSTITUTION_ID = ?";

    private static final String CLEAR_SESSION_SQL = "DELETE FROM LMLDB.WOPAC_PID_PATRON_KEYS WHERE PATRON_KEY = ? OR PID = ?";

    private static final String CREATE_SESSION_SQL = "INSERT INTO LMLDB.WOPAC_PID_PATRON_KEYS (PATRON_KEY, PID) VALUES (?,?)";

    private static final int PID = 2;

    private static final int UNIV_ID = 1;

    private DataSource dataSource;

    public JDBCLoginService(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean login(final String voyagerUnivId, final String pid) {
        try {
            if (userInDatabase(voyagerUnivId)) {
                updateDatabase(voyagerUnivId, pid);
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw new LanewebException(e);
        }
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
