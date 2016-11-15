package edu.stanford.irt.laneweb.bookmarks;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.IOUtils;
import edu.stanford.irt.laneweb.util.JdbcUtils;

public class SQLBookmarkDAO implements BookmarkDAO {

    private static final int BLOB = 2;

    private static final String DELETE_BOOKMARKS_SQL = "DELETE FROM BOOKMARKS WHERE SUNETID = ?";

    private static final Logger LOG = LoggerFactory.getLogger(SQLBookmarkDAO.class);

    private static final String READ_BOOKMARKS_SQL = "SELECT BOOKMARKS FROM BOOKMARKS WHERE SUNETID = ?";

    private static final String ROW_COUNT = "SELECT COUNT(*) FROM BOOKMARKS";

    private static final int USER_ID = 1;

    private static final String WRITE_BOOKMARKS_SQL =
            "BEGIN " +
            "  INSERT INTO bookmarks(sunetid, bookmarks) " +
            "  VALUES (?, empty_blob()) " +
            "  RETURN bookmarks INTO ?; " +
            "END;";

    private DataSource dataSource;

    public SQLBookmarkDAO(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Object> getLinks(final String userid) {
        Objects.requireNonNull(userid, "null userid");
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Object> links = null;
        ObjectInputStream oip = null;
        try {
            conn = this.dataSource.getConnection();
            pstmt = conn.prepareStatement(READ_BOOKMARKS_SQL);
            pstmt.setString(USER_ID, userid);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                oip = new ObjectInputStream(rs.getBlob(1).getBinaryStream());
                links = (List<Object>) oip.readObject();
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            LOG.error(e.getMessage(), e);
        } finally {
            IOUtils.closeStream(oip);
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(pstmt);
            JdbcUtils.closeConnection(conn);
        }
        return links;
    }

    @Override
    public int getRowCount() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        int count = 0;
        try {
            conn = this.dataSource.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(ROW_COUNT);
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new LanewebException(e);
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(stmt);
            JdbcUtils.closeConnection(conn);
        }
        return count;
    }

    @Override
    public void saveLinks(final String userid, final List<Object> links) {
        Objects.requireNonNull(userid, "null userid");
        Objects.requireNonNull(links, "null links");
        Connection conn = getConnection();
        CallableStatement cstmt = null;
        PreparedStatement pstmt = null;
        ObjectOutputStream oop = null;
        try {
            pstmt = conn.prepareStatement(DELETE_BOOKMARKS_SQL);
            pstmt.setString(USER_ID, userid);
            pstmt.execute();
            if (!links.isEmpty()) {
                cstmt = conn.prepareCall(WRITE_BOOKMARKS_SQL);
                cstmt.setString(USER_ID, userid);
                cstmt.registerOutParameter(BLOB, java.sql.Types.BLOB);
                cstmt.executeUpdate();
                Blob blob = cstmt.getBlob(BLOB);
                oop = new ObjectOutputStream(blob.setBinaryStream(1));
                oop.writeObject(Serializable.class.cast(links));
                oop.flush();
            }
            conn.commit();
        } catch (IOException | SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                LOG.error(e1.getMessage(), e1);
            }
            throw new LanewebException(e);
        } finally {
            IOUtils.closeStream(oop);
            JdbcUtils.closeStatement(cstmt);
            JdbcUtils.closeStatement(pstmt);
            JdbcUtils.closeConnection(conn);
        }
    }

    private Connection getConnection() {
        try {
            Connection conn = this.dataSource.getConnection();
            conn.setAutoCommit(false);
            return conn;
        } catch (SQLException e) {
            throw new LanewebException(e);
        }
    }
}
