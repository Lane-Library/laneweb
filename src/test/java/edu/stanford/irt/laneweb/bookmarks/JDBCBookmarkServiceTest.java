package edu.stanford.irt.laneweb.bookmarks;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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

public class JDBCBookmarkServiceTest {

    private Bookmark bookmark;

    private byte[] bytes;

    private Connection connection;

    private DataSource dataSource;

    private ResultSet resultSet;

    private JDBCBookmarkService service;

    private PreparedStatement statement;

    @Before
    public void setUp() throws Exception {
        this.dataSource = mock(DataSource.class);
        this.service = new JDBCBookmarkService(this.dataSource);
        this.connection = mock(Connection.class);
        this.statement = mock(PreparedStatement.class);
        this.resultSet = mock(ResultSet.class);
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
        assertEquals(this.bookmark, this.service.getLinks("userid").get(0));
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
        assertNull(this.service.getLinks("userid"));
        verify(this.dataSource, this.connection, this.statement, this.resultSet);
    }

    @Test(expected = NullPointerException.class)
    public void testGetLinksNullUserid() throws SQLException, IOException {
        replay(this.dataSource, this.connection, this.statement, this.resultSet);
        this.service.getLinks(null);
    }

    @Test(expected = BookmarkException.class)
    public void testGetLinksThrowSQLException() throws SQLException, IOException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        expect(this.connection.prepareStatement(isA(String.class))).andReturn(this.statement);
        this.statement.setString(1, "userid");
        expect(this.statement.executeQuery()).andThrow(new SQLException());
        this.statement.close();
        this.connection.close();
        replay(this.dataSource, this.connection, this.statement, this.resultSet);
        assertNull(this.service.getLinks("userid"));
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
        assertEquals(1, this.service.getRowCount());
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
        assertEquals(0, this.service.getRowCount());
        verify(this.dataSource, this.connection, this.statement, this.resultSet);
    }

    @Test(expected = BookmarkException.class)
    public void testGetRowCountThrowsException() throws SQLException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        expect(this.connection.createStatement()).andReturn(this.statement);
        expect(this.statement.executeQuery("SELECT COUNT(*) FROM BOOKMARKS")).andReturn(this.resultSet);
        expect(this.resultSet.next()).andThrow(new SQLException());
        this.resultSet.close();
        this.statement.close();
        this.connection.close();
        replay(this.dataSource, this.connection, this.statement, this.resultSet);
        this.service.getRowCount();
    }

    @Test
    public void testGetStatus() throws SQLException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        expect(this.connection.createStatement()).andReturn(this.statement);
        expect(this.statement.executeQuery("SELECT COUNT(*) FROM BOOKMARKS")).andReturn(this.resultSet);
        expect(this.resultSet.next()).andReturn(true);
        expect(this.resultSet.getInt(1)).andReturn(1);
        this.resultSet.close();
        this.statement.close();
        this.connection.close();
        replay(this.dataSource, this.connection, this.statement, this.resultSet);
        assertNotNull(this.service.getStatus());
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
        this.service.saveLinks("userid", Collections.singletonList(this.bookmark));
        verify(this.dataSource, this.connection, this.statement, this.resultSet);
    }

    @Test(expected = NullPointerException.class)
    public void testSaveLinksNullBookmarks() throws SQLException {
        replay(this.dataSource, this.connection, this.statement, this.resultSet);
        this.service.saveLinks("userid", null);
    }

    @Test(expected = NullPointerException.class)
    public void testSaveLinksNullUserid() throws SQLException {
        replay(this.dataSource, this.connection, this.statement, this.resultSet);
        this.service.saveLinks(null, Collections.singletonList(this.bookmark));
    }

    @Test(expected = BookmarkException.class)
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
        this.service.saveLinks("userid", Collections.emptyList());
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
        this.service.saveLinks("userid", Collections.emptyList());
        verify(this.dataSource, this.connection, this.statement, this.resultSet);
    }

    @Test(expected = BookmarkException.class)
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
        this.service.saveLinks("userid", Collections.emptyList());
    }

    @Test(expected = BookmarkException.class)
    public void testSaveLinksThrowsSQLException() throws SQLException {
        expect(this.dataSource.getConnection()).andThrow(new SQLException());
        replay(this.dataSource, this.connection, this.statement, this.resultSet);
        this.service.saveLinks("userid", Collections.emptyList());
    }
}
