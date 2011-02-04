package edu.stanford.irt.laneweb.bookmarks;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import oracle.sql.BLOB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.irt.laneweb.util.JdbcUtils;

public class SQLBookmarksDAO implements BookmarksDAO {
    
    private static final String DELETE_BOOKMARKS_SQL = "DELETE FROM BOOKMARKS WHERE EMRID = ?";

    private static final String READ_BOOKMARKS_SQL = "SELECT BOOKMARKS FROM BOOKMARKS WHERE EMRID = ?";

    private static final String WRITE_BOOKMARKS_SQL =
        "BEGIN "
        +"  INSERT INTO bookmarks(emrid, bookmarks) "
        + "  VALUES (?, empty_blob()) "
        + "  RETURN bookmarks INTO ?; "
        + "END;";

    private Logger log = LoggerFactory.getLogger(SQLBookmarksDAO.class);

    private DataSource dataSource;

    public Bookmarks getBookmarks(final String emrid) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Bookmarks bookmarks = null;
        try {
            conn = this.dataSource.getConnection();
            pstmt = conn.prepareStatement(READ_BOOKMARKS_SQL);
            pstmt.setString(1, emrid);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                InputStream is = rs.getBlob(1).getBinaryStream();
                ObjectInputStream oip = new ObjectInputStream(is);
                bookmarks = (Bookmarks) oip.readObject();
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
        return bookmarks;
    }

    public void saveBookmarks(final Bookmarks bookmarks) {
        Connection conn = null;
        CallableStatement cstmt = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String emrid = bookmarks.getEmrid();
        try {
            conn = this.dataSource.getConnection();
            if (bookmarks.size() == 0) {
                pstmt = conn.prepareStatement(DELETE_BOOKMARKS_SQL);
                pstmt.setString(1, emrid);
                pstmt.execute();
            } else {
            conn.setAutoCommit(false);
            cstmt = conn.prepareCall(WRITE_BOOKMARKS_SQL);
            cstmt.setString(1, emrid);
            cstmt.registerOutParameter(2, java.sql.Types.BLOB);
            cstmt.executeUpdate();
            BLOB blob = (BLOB) cstmt.getBlob(2);
            OutputStream os = blob.getBinaryOutputStream();
            ObjectOutputStream oop = new ObjectOutputStream(os);
            oop.writeObject(bookmarks);
            oop.flush();
            oop.close();
            os.close();
            conn.commit();
            }
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
