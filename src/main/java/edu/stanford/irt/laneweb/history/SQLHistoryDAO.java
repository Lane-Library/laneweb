package edu.stanford.irt.laneweb.history;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.irt.laneweb.util.JdbcUtils;

public class SQLHistoryDAO implements HistoryDAO {
    
    private static final String DELETE_HISTORY_SQL = "DELETE FROM HISTORY WHERE EMRID = ?";

    private static final String READ_HISTORY_SQL = "SELECT HISTORY FROM HISTORY WHERE EMRID = ?";

    private static final String WRITE_HISTORY_SQL =
        "BEGIN "
        +"  INSERT INTO history(emrid, history) "
        + "  VALUES (?, empty_blob()) "
        + "  RETURN history INTO ?; "
        + "END;";

    private Logger log = LoggerFactory.getLogger(SQLHistoryDAO.class);

    private DataSource dataSource;

    public List<TrackingData> getHistory(final String emrid) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<TrackingData> history = null;
        try {
            conn = this.dataSource.getConnection();
            pstmt = conn.prepareStatement(READ_HISTORY_SQL);
            pstmt.setString(1, emrid);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                InputStream is = rs.getBlob(1).getBinaryStream();
                ObjectInputStream oip = new ObjectInputStream(is);
                history = (List<TrackingData>) oip.readObject();
                oip.close();
                is.close();
                rs.close();
                pstmt.close();
            }
        } catch (SQLException e) {
            this.log.error(e.getMessage(), e);
        } catch (IOException e) {
            this.log.error(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            this.log.error(e.getMessage(), e);
        } finally {
            JdbcUtils.closeConnection(conn);
            JdbcUtils.closeStatement(pstmt);
            JdbcUtils.closeResultSet(rs);
        }
        return history;
    }

    public void saveHistory(final List<TrackingData> history, String emrid) {
        Connection conn = null;
        CallableStatement cstmt = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = this.dataSource.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(DELETE_HISTORY_SQL);
            pstmt.setString(1, emrid);
            pstmt.execute();
            if (history.size() > 0) {
                cstmt = conn.prepareCall(WRITE_HISTORY_SQL);
                cstmt.setString(1, emrid);
                cstmt.registerOutParameter(2, java.sql.Types.BLOB);
                cstmt.executeUpdate();
                Blob blob = cstmt.getBlob(2);
                OutputStream os = blob.setBinaryStream(1);
                ObjectOutputStream oop = new ObjectOutputStream(os);
                oop.writeObject(history);
                oop.flush();
                oop.close();
                os.close();
            }
            conn.commit();
        } catch (SQLException e) {
            this.log.error(e.getMessage(), e);
            try {
                conn.rollback();
            } catch (SQLException e1) {
                this.log.error(e1.getMessage(), e1);
            }
        } catch (IOException e) {
            this.log.error(e.getMessage(), e);
            try {
                conn.rollback();
            } catch (SQLException e1) {
                this.log.error(e1.getMessage(), e1);
            }
        } finally {
            JdbcUtils.closeConnection(conn);
            JdbcUtils.closeStatement(cstmt);
            JdbcUtils.closeStatement(pstmt);
            JdbcUtils.closeResultSet(rs);
        }
    }

    public void setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
