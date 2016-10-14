package edu.stanford.irt.laneweb.bookmarks;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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

    private static final String DELETE_BOOKMARKS_SQL = "DELETE FROM BOOKMARKS WHERE ID = ?";

    private static final String INSERT_BOOKMARKS_SQL = "INSERT INTO BOOKMARKS (ID, BOOKMARKS) VALUES (?, ?)";

    private static final Logger LOG = LoggerFactory.getLogger(SQLBookmarkDAO.class);

    private static final String READ_BOOKMARKS_SQL = "SELECT BOOKMARKS FROM BOOKMARKS WHERE ID = ?";

    private static final String ROW_COUNT = "SELECT COUNT(*) FROM BOOKMARKS";

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
            pstmt.setString(1, userid);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                oip = new ObjectInputStream(new ByteArrayInputStream(rs.getBytes(1)));
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
        PreparedStatement insertStatement = null;
        PreparedStatement deleteStatement = null;
        ObjectOutputStream oop = null;
        try {
            deleteStatement = conn.prepareStatement(DELETE_BOOKMARKS_SQL);
            deleteStatement.setString(1, userid);
            deleteStatement.execute();
            if (!links.isEmpty()) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                oop = new ObjectOutputStream(baos);
                oop.writeObject(Serializable.class.cast(links));
                oop.flush();
                insertStatement = conn.prepareStatement(INSERT_BOOKMARKS_SQL);
                insertStatement.setString(1, userid);
                insertStatement.setBytes(2, baos.toByteArray());
                insertStatement.executeUpdate();
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
            JdbcUtils.closeStatement(insertStatement);
            JdbcUtils.closeStatement(deleteStatement);
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
