package edu.stanford.irt.laneweb.bookmarks;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collections;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

public class SQLBookmarkDAOTest {

    private Blob blob;

    private Bookmark bookmark;

    private byte[] bytes;

    private CallableStatement callableStatement;

    private Connection connection;

    private SQLBookmarkDAO dao;

    private DataSource dataSource;

    private ResultSet resultSet;

    private PreparedStatement statement;

    @Before
    public void setUp() throws Exception {
        this.dataSource = createMock(DataSource.class);
        this.dao = new SQLBookmarkDAO(this.dataSource);
        this.connection = createMock(Connection.class);
        this.statement = createMock(PreparedStatement.class);
        this.resultSet = createMock(ResultSet.class);
        this.blob = createMock(Blob.class);
        this.callableStatement = createMock(CallableStatement.class);
        this.bookmark = new Bookmark("label", "url");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(Collections.<Bookmark> singletonList(this.bookmark));
        this.bytes = baos.toByteArray();
    }

    @Test
    public void testGetLinks() throws SQLException, IOException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        expect(this.connection.prepareStatement(isA(String.class))).andReturn(this.statement);
        this.statement.setString(1, "ditenus");
        expect(this.statement.executeQuery()).andReturn(this.resultSet);
        expect(this.resultSet.next()).andReturn(true);
        expect(this.resultSet.getBlob(1)).andReturn(this.blob);
        expect(this.blob.getBinaryStream()).andReturn(new ByteArrayInputStream(this.bytes));
        this.resultSet.close();
        this.statement.close();
        this.connection.close();
        replay(this.dataSource, this.connection, this.statement, this.resultSet, this.blob);
        assertEquals(this.bookmark, this.dao.getLinks("ditenus").get(0));
        verify(this.dataSource, this.connection, this.statement, this.resultSet, this.blob);
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
    public void testSaveLinks() throws SQLException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        this.connection.setAutoCommit(false);
        expect(this.connection.prepareStatement(isA(String.class))).andReturn(this.statement);
        this.statement.setString(1, "ditenus");
        expect(this.statement.execute()).andReturn(true);
        expect(this.connection.prepareCall(isA(String.class))).andReturn(this.callableStatement);
        this.callableStatement.setString(1, "ditenus");
        this.callableStatement.registerOutParameter(2, Types.BLOB);
        expect(this.callableStatement.executeUpdate()).andReturn(1);
        expect(this.callableStatement.getBlob(2)).andReturn(this.blob);
        expect(this.blob.setBinaryStream(1)).andReturn(new ByteArrayOutputStream());
        this.connection.commit();
        this.callableStatement.close();
        this.statement.close();
        this.connection.close();
        replay(this.dataSource, this.connection, this.statement, this.resultSet, this.blob, this.callableStatement);
        this.dao.saveLinks("ditenus", Collections.<Bookmark> singletonList(this.bookmark));
        verify(this.dataSource, this.connection, this.statement, this.resultSet, this.blob, this.callableStatement);
    }
}
