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
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.sql.DataSource;

import edu.stanford.irt.status.ApplicationStatus;
import edu.stanford.irt.status.Status;
import edu.stanford.irt.status.StatusItem;

public class JDBCBookmarkService implements BookmarkService {

    private static final int BYTES = 2;

    private static final String DELETE_BOOKMARKS_SQL = "DELETE FROM BOOKMARKS WHERE SUNETID = ?";

    private static final String INSERT_BOOKMARKS_SQL = "INSERT INTO BOOKMARKS (SUNETID, BOOKMARKS) VALUES (?, ?)";

    private static final long MAX_OK_TIME = 1_000;

    private static final String READ_BOOKMARKS_SQL = "SELECT BOOKMARKS FROM BOOKMARKS WHERE SUNETID = ?";

    private static final String ROW_COUNT = "SELECT COUNT(*) FROM BOOKMARKS";

    private static final String ROWCOUNT_FORMAT = "total row count = %s";

    private static final String TIME_FORMAT = "getRowcount connection took %sms";

    private static final int USER_ID = 1;

    private DataSource dataSource;

    public JDBCBookmarkService(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private static List<Bookmark> getLinksFromStatement(final PreparedStatement pstmt)
            throws SQLException, IOException, ClassNotFoundException {
        List<Bookmark> links = null;
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                try (ObjectInputStream oip = new ObjectInputStream(new ByteArrayInputStream(rs.getBytes(1)))) {
                    links = (List<Bookmark>) oip.readObject();
                }
            }
        }
        return links;
    }

    private static void saveLinksAndResetAutocommit(final String userid, final List<Bookmark> links,
            final Connection conn) throws SQLException, IOException {
        try (PreparedStatement pstmt = conn.prepareStatement(DELETE_BOOKMARKS_SQL)) {
            pstmt.setString(USER_ID, userid);
            pstmt.execute();
            writeLinksBlob(userid, links, conn);
            conn.commit();
        } catch (IOException | SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    private static void writeLinksBlob(final String userid, final List<Bookmark> links, final Connection conn)
            throws SQLException, IOException {
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
    }

    @Override
    public List<Bookmark> getLinks(final String userid) {
        Objects.requireNonNull(userid, "null userid");
        List<Bookmark> links = null;
        try (Connection conn = this.dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(READ_BOOKMARKS_SQL)) {
            pstmt.setString(USER_ID, userid);
            links = getLinksFromStatement(pstmt);
        } catch (SQLException | ClassNotFoundException | IOException e) {
            throw new BookmarkException(e);
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
            throw new BookmarkException(e);
        }
        return count;
    }

    @Override
    public ApplicationStatus getStatus() {
        List<StatusItem> list = new ArrayList<>();
        Instant start = Instant.now();
        int rowCount = getRowCount();
        long time = Duration.between(start, Instant.now()).toMillis();
        list.add(new StatusItem(Status.INFO, String.format(ROWCOUNT_FORMAT, Integer.valueOf(rowCount))));
        Status status = time < MAX_OK_TIME ? Status.OK : Status.WARN;
        list.add(new StatusItem(status, String.format(TIME_FORMAT, Long.valueOf(time))));
        return new ApplicationStatus(null, null, null, 0, null, list);
    }

    @Override
    public void saveLinks(final String userid, final List<Bookmark> links) {
        Objects.requireNonNull(userid, "null userid");
        Objects.requireNonNull(links, "null links");
        try (Connection conn = this.dataSource.getConnection()) {
            conn.setAutoCommit(false);
            saveLinksAndResetAutocommit(userid, links, conn);
        } catch (SQLException | IOException e) {
            throw new BookmarkException(e);
        }
    }
}
