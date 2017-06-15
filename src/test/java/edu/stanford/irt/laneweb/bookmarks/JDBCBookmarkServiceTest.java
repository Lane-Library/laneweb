package edu.stanford.irt.laneweb.bookmarks;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.LanewebException;

public class JDBCBookmarkServiceTest {

    private Bookmark bookmark;

    private byte[] bytes;

    private Connection connection;

    private JDBCBookmarkService dao;

    private DataSource dataSource;

    private ResultSet resultSet;

    private PreparedStatement statement;

    @Before
    public void setUp() throws Exception {
        this.dataSource = createMock(DataSource.class);
        this.dao = new JDBCBookmarkService(this.dataSource);
        this.connection = createMock(Connection.class);
        this.statement = createMock(PreparedStatement.class);
        this.resultSet = createMock(ResultSet.class);
        this.bookmark = new Bookmark("label", "url");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(Collections.singletonList(this.bookmark));
        this.bytes = baos.toByteArray();
    }

    @Test
    public void testGetLinks() throws SQLException, IOException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        expect(this.connection.prepareStatement(isA(String.class))).andReturn(this.statement);
        this.statement.setString(1, "userid");
        expect(this.statement.executeQuery()).andReturn(this.resultSet);
        expect(this.resultSet.next()).andReturn(true);
        expect(this.resultSet.getBytes(1)).andReturn(this.bytes);
        this.resultSet.close();
        this.statement.close();
        this.connection.close();
        replay(this.dataSource, this.connection, this.statement, this.resultSet);
        assertEquals(this.bookmark, this.dao.getLinks("userid").get(0));
        verify(this.dataSource, this.connection, this.statement, this.resultSet);
    }

    @Test
    public void testGetLinksNone() throws SQLException, IOException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        expect(this.connection.prepareStatement(isA(String.class))).andReturn(this.statement);
        this.statement.setString(1, "userid");
        expect(this.statement.executeQuery()).andReturn(this.resultSet);
        expect(this.resultSet.next()).andReturn(false);
        this.resultSet.close();
        this.statement.close();
        this.connection.close();
        replay(this.dataSource, this.connection, this.statement, this.resultSet);
        assertNull(this.dao.getLinks("userid"));
        verify(this.dataSource, this.connection, this.statement, this.resultSet);
    }

    @Test
    public void testGetLinksNullUserid() throws SQLException, IOException {
        replay(this.dataSource, this.connection, this.statement, this.resultSet);
        try {
            this.dao.getLinks(null);
        } catch (NullPointerException e) {
        }
        verify(this.dataSource, this.connection, this.statement, this.resultSet);
    }

    @Test
    public void testGetLinksThrowSQLException() throws SQLException, IOException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        expect(this.connection.prepareStatement(isA(String.class))).andReturn(this.statement);
        this.statement.setString(1, "userid");
        expect(this.statement.executeQuery()).andThrow(new SQLException());
        this.statement.close();
        this.connection.close();
        replay(this.dataSource, this.connection, this.statement, this.resultSet);
        assertNull(this.dao.getLinks("userid"));
        verify(this.dataSource, this.connection, this.statement, this.resultSet);
    }

    @Test
    public void testGetRowCount() throws SQLException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        expect(this.connection.createStatement()).andReturn(this.statement);
        expect(this.statement.executeQuery("SELECT COUNT(*) FROM BOOKMARKS")).andReturn(this.resultSet);
        expect(this.resultSet.next()).andReturn(true);
        expect(this.resultSet.getInt(1)).andReturn(1);
        this.resultSet.close();
        this.statement.close();
        this.connection.close();
        replay(this.dataSource, this.connection, this.statement, this.resultSet);
        assertEquals(1, this.dao.getRowCount());
        verify(this.dataSource, this.connection, this.statement, this.resultSet);
    }

    @Test
    public void testGetRowCountNone() throws SQLException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        expect(this.connection.createStatement()).andReturn(this.statement);
        expect(this.statement.executeQuery("SELECT COUNT(*) FROM BOOKMARKS")).andReturn(this.resultSet);
        expect(this.resultSet.next()).andReturn(false);
        this.resultSet.close();
        this.statement.close();
        this.connection.close();
        replay(this.dataSource, this.connection, this.statement, this.resultSet);
        assertEquals(0, this.dao.getRowCount());
        verify(this.dataSource, this.connection, this.statement, this.resultSet);
    }

    @Test
    public void testGetRowCountThrowsException() throws SQLException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        expect(this.connection.createStatement()).andReturn(this.statement);
        expect(this.statement.executeQuery("SELECT COUNT(*) FROM BOOKMARKS")).andReturn(this.resultSet);
        expect(this.resultSet.next()).andThrow(new SQLException());
        this.resultSet.close();
        this.statement.close();
        this.connection.close();
        replay(this.dataSource, this.connection, this.statement, this.resultSet);
        try {
            this.dao.getRowCount();
        } catch (LanewebException e) {
        }
        verify(this.dataSource, this.connection, this.statement, this.resultSet);
    }

    @Test
    public void testSaveLinks() throws SQLException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        this.connection.setAutoCommit(false);
        expect(this.connection.prepareStatement(isA(String.class))).andReturn(this.statement);
        this.statement.setString(1, "userid");
        expect(this.statement.execute()).andReturn(true);
        expect(this.connection.prepareStatement(isA(String.class))).andReturn(this.statement);
        this.statement.setString(1, "userid");
        this.statement.setBytes(eq(2), aryEq(this.bytes));
        expect(this.statement.executeUpdate()).andReturn(1);
        this.connection.commit();
        this.statement.close();
        expectLastCall().times(2);
        this.connection.setAutoCommit(true);
        this.connection.close();
        replay(this.dataSource, this.connection, this.statement, this.resultSet);
        this.dao.saveLinks("userid", Collections.singletonList(this.bookmark));
        verify(this.dataSource, this.connection, this.statement, this.resultSet);
    }

    @Test
    public void testSaveLinksNullBookmarks() throws SQLException {
        replay(this.dataSource, this.connection, this.statement, this.resultSet);
        try {
            this.dao.saveLinks("userid", null);
        } catch (NullPointerException e) {
        }
        verify(this.dataSource, this.connection, this.statement, this.resultSet);
    }

    @Test
    public void testSaveLinksNullUserid() throws SQLException {
        replay(this.dataSource, this.connection, this.statement, this.resultSet);
        try {
            this.dao.saveLinks(null, Collections.singletonList(this.bookmark));
        } catch (NullPointerException e) {
        }
        verify(this.dataSource, this.connection, this.statement, this.resultSet);
    }

    @Test
    public void testSaveLinksRollbackThrowsSQLException() throws SQLException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        this.connection.setAutoCommit(false);
        expect(this.connection.prepareStatement(isA(String.class))).andReturn(this.statement);
        this.statement.setString(1, "userid");
        expect(this.statement.execute()).andThrow(new SQLException());
        this.connection.rollback();
        expectLastCall().andThrow(new SQLException());
        this.statement.close();
        this.connection.setAutoCommit(true);
        this.connection.close();
        replay(this.dataSource, this.connection, this.statement, this.resultSet);
        try {
            this.dao.saveLinks("userid", Collections.emptyList());
        } catch (LanewebException e) {
        }
        verify(this.dataSource, this.connection, this.statement, this.resultSet);
    }

    @Test
    public void testSaveLinksSizeZero() throws SQLException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        this.connection.setAutoCommit(false);
        expect(this.connection.prepareStatement(isA(String.class))).andReturn(this.statement);
        this.statement.setString(1, "userid");
        expect(this.statement.execute()).andReturn(true);
        this.connection.commit();
        this.statement.close();
        this.connection.setAutoCommit(true);
        this.connection.close();
        replay(this.dataSource, this.connection, this.statement, this.resultSet);
        this.dao.saveLinks("userid", Collections.emptyList());
        verify(this.dataSource, this.connection, this.statement, this.resultSet);
    }

    @Test
    public void testSaveLinksStatementThrowsSQLException() throws SQLException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        this.connection.setAutoCommit(false);
        expect(this.connection.prepareStatement(isA(String.class))).andReturn(this.statement);
        this.statement.setString(1, "userid");
        expect(this.statement.execute()).andThrow(new SQLException());
        this.connection.rollback();
        this.statement.close();
        this.connection.setAutoCommit(true);
        this.connection.close();
        replay(this.dataSource, this.connection, this.statement, this.resultSet);
        try {
            this.dao.saveLinks("userid", Collections.emptyList());
        } catch (LanewebException e) {
        }
        verify(this.dataSource, this.connection, this.statement, this.resultSet);
    }

    @Test
    public void testSaveLinksThrowsSQLException() throws SQLException {
        expect(this.dataSource.getConnection()).andThrow(new SQLException());
        replay(this.dataSource, this.connection, this.statement, this.resultSet);
        try {
            this.dao.saveLinks("userid", Collections.emptyList());
        } catch (LanewebException e) {
        }
        verify(this.dataSource, this.connection, this.statement, this.resultSet);
    }
}
