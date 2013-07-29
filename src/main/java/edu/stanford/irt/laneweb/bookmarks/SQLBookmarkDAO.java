package edu.stanford.irt.laneweb.bookmarks;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.IOUtils;
import edu.stanford.irt.laneweb.util.JdbcUtils;

public class SQLBookmarkDAO implements BookmarkDAO {

    private static final String DELETE_BOOKMARKS_SQL = "DELETE FROM BOOKMARKS WHERE SUNETID = ?";

    private static final String READ_BOOKMARKS_SQL = "SELECT BOOKMARKS FROM BOOKMARKS WHERE SUNETID = ?";

    private static final String ROW_COUNT = "SELECT COUNT(*) FROM BOOKMARKS";

    private static final String WRITE_BOOKMARKS_SQL =
            "BEGIN " +
            "  INSERT INTO bookmarks(sunetid, bookmarks) " +
            "  VALUES (?, empty_blob()) " +
            "  RETURN bookmarks INTO ?; " +
            "END;";

    private DataSource dataSource;

    private Logger log;

    public SQLBookmarkDAO(final DataSource dataSource, Logger log) {
        this.dataSource = dataSource;
        this.log = log;
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
        ObjectInputStream oip = null;
        try {
            conn = this.dataSource.getConnection();
            pstmt = conn.prepareStatement(READ_BOOKMARKS_SQL);
            pstmt.setString(1, sunetid);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                oip = new ObjectInputStream(rs.getBlob(1).getBinaryStream());
                links = (List<Bookmark>) oip.readObject();
            }
        } catch (SQLException e) {
            this.log.error(e.getMessage(), e);
        } catch (IOException e) {
            this.log.error(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            this.log.error(e.getMessage(), e);
        } finally {
            IOUtils.closeStream(oip);
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(pstmt);
            JdbcUtils.closeConnection(conn);
        }
        return links;
    }

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
        ObjectOutputStream oop = null;
        try {
            conn = this.dataSource.getConnection();
            // TODO: this autocommit state probably persists, OK for now as this
            // is the only place updates happen.
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
                oop = new ObjectOutputStream(blob.setBinaryStream(1));
                oop.writeObject(links);
                oop.flush();
            }
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    this.log.error(e1.getMessage(), e1);
                }
            }
            throw new LanewebException(e);
        } catch (IOException e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                this.log.error(e1.getMessage(), e1);
            }
            throw new LanewebException(e);
        } finally {
            IOUtils.closeStream(oop);
            JdbcUtils.closeStatement(cstmt);
            JdbcUtils.closeStatement(pstmt);
            JdbcUtils.closeConnection(conn);
        }
    }
    /**
     * This main method is a small program to copy bookmarks data between
     * databases
     * 
     * @param args
     *            srcUrl srcUser srcPassword dstUrl dstUser dstPassword
     */
    /*
     * public static void main(String[] args) { DataSource srcDatasource = new
     * org.springframework.jdbc.datasource.SingleConnectionDataSource(args[0],
     * args[1], args[2], true); DataSource dstDatasource = new
     * org.springframework.jdbc.datasource.SingleConnectionDataSource(args[3],
     * args[4], args[5], true); Connection conn = null; Statement stmt = null;
     * ResultSet rs = null; BookmarkDAO srcDAO = new
     * SQLBookmarkDAO(srcDatasource); BookmarkDAO dstDAO = new
     * SQLBookmarkDAO(dstDatasource); String sunetid = null; try { conn =
     * srcDatasource.getConnection(); stmt = conn.createStatement(); rs =
     * stmt.executeQuery("SELECT SUNETID FROM BOOKMARKS"); while (rs.next()) {
     * sunetid = rs.getString(1); List<Bookmark> bookmarks =
     * srcDAO.getLinks(sunetid); System.out.println(sunetid + ":" + bookmarks);
     * dstDAO.saveLinks(sunetid, bookmarks); } } catch (SQLException e) { throw
     * new LanewebException(e); } finally { JdbcUtils.closeConnection(conn);
     * JdbcUtils.closeStatement(stmt); JdbcUtils.closeResultSet(rs); } }
     */
}
