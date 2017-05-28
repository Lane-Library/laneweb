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

public class JDBCBookmarkService implements BookmarkService {

    private static final int BYTES = 2;

    private static final String DELETE_BOOKMARKS_SQL = "DELETE FROM BOOKMARKS WHERE ID = ?";

    private static final String INSERT_BOOKMARKS_SQL = "INSERT INTO BOOKMARKS (ID, BOOKMARKS) VALUES (?, ?)";

    private static final Logger LOG = LoggerFactory.getLogger(JDBCBookmarkService.class);

    private static final String READ_BOOKMARKS_SQL = "SELECT BOOKMARKS FROM BOOKMARKS WHERE ID = ?";

    private static final String ROW_COUNT = "SELECT COUNT(*) FROM BOOKMARKS";

    private static final int USER_ID = 1;

    private DataSource dataSource;

    public JDBCBookmarkService(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Bookmark> getLinks(final String userid) {
        Objects.requireNonNull(userid, "null userid");
        List<Bookmark> links = null;
        try (Connection conn = this.dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(READ_BOOKMARKS_SQL)) {
            pstmt.setString(USER_ID, userid);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    try (ObjectInputStream oip = new ObjectInputStream(new ByteArrayInputStream(rs.getBytes(1)))) {
                        links = (List<Bookmark>) oip.readObject();
                    }
                }
            }
        } catch (SQLException | IOException | ClassNotFoundException e) {
            LOG.error(e.getMessage(), e);
        }
        return links;
    }

    @Override
    public int getRowCount() {
        int count = 0;
        try (Connection conn = this.dataSource.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(ROW_COUNT)) {
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new LanewebException(e);
        }
        return count;
    }

    @Override
    public void saveLinks(final String userid, final List<Bookmark> links) {
        Objects.requireNonNull(userid, "null userid");
        Objects.requireNonNull(links, "null links");
        Connection conn = getConnection();
        try (PreparedStatement deleteStatement = conn.prepareStatement(DELETE_BOOKMARKS_SQL)) {
            deleteStatement.setString(USER_ID, userid);
            deleteStatement.execute();
            if (!links.isEmpty()) {
                try (PreparedStatement insertStatement = conn.prepareStatement(INSERT_BOOKMARKS_SQL);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ObjectOutputStream oop = new ObjectOutputStream(baos)) {
                    oop.writeObject(Serializable.class.cast(links));
                    oop.flush();
                    insertStatement.setString(USER_ID, userid);
                    insertStatement.setBytes(BYTES, baos.toByteArray());
                    insertStatement.executeUpdate();
                }
            }
            conn.commit();
        } catch (IOException | SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                throw new LanewebException(e1);
            }
            throw new LanewebException(e);
        } finally {
            releaseConnection(conn);
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

    private void releaseConnection(final Connection conn) {
        try {
            conn.setAutoCommit(true);
            conn.close();
        } catch (SQLException e) {
            throw new LanewebException(e);
        }
    }
}