package edu.stanford.irt.laneweb.bookmarks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.irt.laneweb.util.JdbcUtils;

public class SQLBookmarksDAO implements BookmarksDAO {

    private static final String ADD_LINK = "INSERT INTO BOOKMARKS (EMRID, URL, LABEL, ORD) VALUES(?,?,?,?)";

    private static final String GET_LINKS = "SELECT * FROM BOOKMARKS WHERE EMRID = ? ORDER BY ORD";

    private static final String REMOVE_LINKS = "DELETE FROM BOOKMARKS WHERE EMRID = ?";

    private DataSource dataSource;

    private Logger log = LoggerFactory.getLogger(SQLBookmarksDAO.class);

    public Bookmarks getBookmarks(final String emrid) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Bookmarks bookmarks = new Bookmarks();
            conn = this.dataSource.getConnection();
            stmt = conn.prepareStatement(GET_LINKS);
            stmt.setString(1, emrid);
            rs = stmt.executeQuery();
            while (rs.next()) {
                bookmarks.add(new Bookmark(rs.getString("LABEL"), rs.getString("URL")));
            }
            return bookmarks;
        } catch (SQLException e) {
            this.log.error(e.getMessage(), e);
            return null;
        } finally {
            JdbcUtils.closeConnection(conn);
            JdbcUtils.closeStatement(stmt);
            JdbcUtils.closeResultSet(rs);
        }
    }

    public void saveBookmarks(final String emrid, final Bookmarks bookmarks) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = this.dataSource.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(REMOVE_LINKS);
            stmt.setString(1, emrid);
            stmt.executeUpdate();
            stmt = conn.prepareStatement(ADD_LINK);
            int i = 0;
            for (Bookmark bookmark : bookmarks) {
                stmt.setString(1, emrid);
                stmt.setString(2, bookmark.getUrl());
                stmt.setString(3, bookmark.getLabel());
                stmt.setInt(4, i++);
                stmt.addBatch();
            }
            stmt.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            this.log.error(e.getMessage(), e);
            try {
                conn.rollback();
            } catch (SQLException e1) {
                this.log.error(e1.getMessage(), e1);
            }
        } finally {
            JdbcUtils.closeConnection(conn);
            JdbcUtils.closeStatement(stmt);
            JdbcUtils.closeResultSet(rs);
        }
    }

    public void setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
