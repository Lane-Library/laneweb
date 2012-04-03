package edu.stanford.irt.laneweb.bookmarks;

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

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.JdbcUtils;

public class SQLBookmarkDAO implements BookmarkDAO {

    private static final String DELETE_BOOKMARKS_SQL = "DELETE FROM BOOKMARKS WHERE SUNETID = ?";

    private static final String READ_BOOKMARKS_SQL = "SELECT BOOKMARKS FROM BOOKMARKS WHERE SUNETID = ?";

    private static final String WRITE_BOOKMARKS_SQL =
            "BEGIN " +
            "  INSERT INTO bookmarks(sunetid, bookmarks) " +
            "  VALUES (?, empty_blob()) " +
            "  RETURN bookmarks INTO ?; " +
            "END;";

    private DataSource dataSource;

    private Logger log = LoggerFactory.getLogger(SQLBookmarkDAO.class);

    public SQLBookmarkDAO(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @SuppressWarnings("unchecked")
    public List<Bookmark> getLinks(final String sunetid) {
        if (sunetid == null) {
            throw new LanewebException("null sunetid");
        }
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Bookmark> links = null;
        try {
            conn = this.dataSource.getConnection();
            pstmt = conn.prepareStatement(READ_BOOKMARKS_SQL);
            pstmt.setString(1, sunetid);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                InputStream is = rs.getBlob(1).getBinaryStream();
                ObjectInputStream oip = new ObjectInputStream(is);
                links = (List<Bookmark>) oip.readObject();
                oip.close();
                is.close();
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
        return links;
    }

    public void saveLinks(final String sunetid, final List<Bookmark> links) {
        if (sunetid == null) {
            throw new LanewebException("null sunetid");
        }
        if (links == null) {
            throw new LanewebException("null links");
        }
        Connection conn = null;
        CallableStatement cstmt = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = this.dataSource.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(DELETE_BOOKMARKS_SQL);
            pstmt.setString(1, sunetid);
            pstmt.execute();
            if (links.size() > 0) {
                cstmt = conn.prepareCall(WRITE_BOOKMARKS_SQL);
                cstmt.setString(1, sunetid);
                cstmt.registerOutParameter(2, java.sql.Types.BLOB);
                cstmt.executeUpdate();
                Blob blob = cstmt.getBlob(2);
                OutputStream os = blob.setBinaryStream(1);
                ObjectOutputStream oop = new ObjectOutputStream(os);
                oop.writeObject(links);
                oop.flush();
                oop.close();
                os.close();
            }
            conn.commit();
        } catch (SQLException e) {
            this.log.error(e.getMessage(), e);
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    this.log.error(e1.getMessage(), e1);
                }
            }
        } catch (IOException e) {
            this.log.error(e.getMessage(), e);
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    this.log.error(e1.getMessage(), e1);
                }
            }
        } finally {
            JdbcUtils.closeConnection(conn);
            JdbcUtils.closeStatement(cstmt);
            JdbcUtils.closeStatement(pstmt);
            JdbcUtils.closeResultSet(rs);
        }
    }
}
