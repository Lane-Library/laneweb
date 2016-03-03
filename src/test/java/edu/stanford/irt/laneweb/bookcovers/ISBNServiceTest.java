package edu.stanford.irt.laneweb.bookcovers;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.LanewebException;

public class ISBNServiceTest {

    private Connection connection;

    private DataSource dataSource;

    private ResultSet resultSet;

    private ISBNService service;

    private PreparedStatement statement;

    @Before
    public void setUp() {
        this.dataSource = createMock(DataSource.class);
        this.service = new ISBNService(this.dataSource);
        this.connection = createMock(Connection.class);
        this.statement = createMock(PreparedStatement.class);
        this.resultSet = createMock(ResultSet.class);
    }

    @Test
    public void testGetISBNs() throws SQLException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        expect(this.connection.prepareStatement(isA(String.class))).andReturn(this.statement);
        this.statement.setInt(1, 906);
        this.statement.setInt(2, 766);
        for (int i = 3; i <= 20; i++) {
            this.statement.setInt(i, 766);
        }
        expect(this.statement.executeQuery()).andReturn(this.resultSet);
        expect(this.resultSet.next()).andReturn(true).times(3);
        expect(this.resultSet.getInt(1)).andReturn(906);
        expect(this.resultSet.getString(2)).andReturn("10");
        expect(this.resultSet.getInt(1)).andReturn(766);
        expect(this.resultSet.getString(2)).andReturn("20");
        expect(this.resultSet.getInt(1)).andReturn(766);
        expect(this.resultSet.getString(2)).andReturn("21");
        expect(this.resultSet.next()).andReturn(false);
        this.resultSet.close();
        this.statement.close();
        this.connection.close();
        replay(this.dataSource, this.connection, this.statement, this.resultSet);
        assertEquals(2, this.service.getISBNs(Arrays.asList(new Integer[] { 906, 766 })).size());
        verify(this.dataSource, this.connection, this.resultSet);
    }

    @Test(expected = LanewebException.class)
    public void testGetISBNs6Ids() throws SQLException {
        this.service.getISBNs(Arrays.asList(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21 }));
    }

    @Test
    public void testGetISBNsConnectionException() throws SQLException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        expect(this.connection.prepareStatement(isA(String.class))).andThrow(new SQLException());
        this.connection.close();
        replay(this.dataSource, this.connection, this.statement, this.resultSet);
        try {
            this.service.getISBNs(Arrays.asList(new Integer[] { 906, 766 }));
        } catch (LanewebException e) {
        }
        verify(this.dataSource, this.connection, this.resultSet);
    }

    @Test
    public void testGetISBNsDataSourceException() throws SQLException {
        expect(this.dataSource.getConnection()).andThrow(new SQLException());
        replay(this.dataSource, this.connection, this.statement, this.resultSet);
        try {
            this.service.getISBNs(Arrays.asList(new Integer[] { 906, 766 }));
        } catch (LanewebException e) {
        }
        verify(this.dataSource, this.connection, this.resultSet);
    }

    @Test
    public void testGetISBNsResultSetException() throws SQLException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        expect(this.connection.prepareStatement(isA(String.class))).andReturn(this.statement);
        this.statement.setInt(1, 906);
        this.statement.setInt(2, 766);
        for (int i = 3; i <= 20; i++) {
            this.statement.setInt(i, 766);
        }
        expect(this.statement.executeQuery()).andReturn(this.resultSet);
        expect(this.resultSet.next()).andReturn(true);
        expect(this.resultSet.getInt(1)).andThrow(new SQLException());
        this.resultSet.close();
        this.statement.close();
        this.connection.close();
        replay(this.dataSource, this.connection, this.statement, this.resultSet);
        try {
            this.service.getISBNs(Arrays.asList(new Integer[] { 906, 766 }));
        } catch (LanewebException e) {
        }
        verify(this.dataSource, this.connection, this.resultSet);
    }

    @Test
    public void testGetISBNsStatementException() throws SQLException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        expect(this.connection.prepareStatement(isA(String.class))).andReturn(this.statement);
        this.statement.setInt(1, 906);
        this.statement.setInt(2, 766);
        for (int i = 3; i <= 20; i++) {
            this.statement.setInt(i, 766);
        }
        expect(this.statement.executeQuery()).andThrow(new SQLException());
        this.statement.close();
        this.connection.close();
        replay(this.dataSource, this.connection, this.statement, this.resultSet);
        try {
            this.service.getISBNs(Arrays.asList(new Integer[] { 906, 766 }));
        } catch (LanewebException e) {
        }
        verify(this.dataSource, this.connection, this.resultSet);
    }
}
