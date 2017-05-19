package edu.stanford.irt.laneweb.voyager;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.LanewebException;

public class JDBCLoginServiceTest {

    private Connection connection;

    private DataSource dataSource;

    private ResultSet resultSet;

    private JDBCLoginService service;

    private PreparedStatement statement;

    @Before
    public void setUp() throws Exception {
        this.dataSource = createMock(DataSource.class);
        this.service = new JDBCLoginService(this.dataSource);
        this.connection = createMock(Connection.class);
        this.statement = createMock(PreparedStatement.class);
        this.resultSet = createMock(ResultSet.class);
    }

    @Test(expected = LanewebException.class)
    public void testDeleteError() throws SQLException {
        this.statement.setString(1, "0999");
        expect(this.statement.executeQuery()).andReturn(this.resultSet);
        expect(this.resultSet.next()).andReturn(true);
        expect(this.resultSet.getInt(1)).andReturn(Integer.valueOf(1));
        this.resultSet.close();
        this.statement.close();
        this.statement.setString(1, "0999");
        this.statement.setString(2, "123");
        expect(this.statement.executeUpdate()).andThrow(new SQLException("oops"));
        this.statement.close();
        expect(this.connection.prepareStatement(isA(String.class))).andReturn(this.statement).times(3);
        this.connection.close();
        expect(this.dataSource.getConnection()).andReturn(this.connection).times(2);
        replay(this.dataSource, this.connection, this.statement, this.resultSet);
        this.service.login("0999", "123");
    }

    @Test
    public void testLaneLoginURL() throws SQLException {
        this.statement.setString(1, "0999");
        expect(this.statement.executeQuery()).andReturn(this.resultSet);
        expect(this.resultSet.next()).andReturn(true);
        expect(this.resultSet.getInt(1)).andReturn(Integer.valueOf(1));
        this.resultSet.close();
        this.statement.close();
        this.statement.setString(1, "0999");
        this.statement.setString(2, "123");
        expect(this.statement.executeUpdate()).andReturn(Integer.valueOf(0));
        this.statement.setString(1, "0999");
        this.statement.setString(2, "123");
        expect(this.statement.executeUpdate()).andReturn(Integer.valueOf(1));
        this.statement.close();
        this.statement.close();
        expect(this.connection.prepareStatement(isA(String.class))).andReturn(this.statement).times(3);
        this.connection.close();
        expectLastCall().times(2);
        expect(this.dataSource.getConnection()).andReturn(this.connection).times(2);
        replay(this.dataSource, this.connection, this.statement, this.resultSet);
        assertTrue(this.service.login("0999", "123"));
        verify(this.dataSource, this.connection, this.statement);
    }

    @Test
    public void testMissingUnivid() throws SQLException {
        this.statement.setString(1, "0999");
        expect(this.statement.executeQuery()).andReturn(this.resultSet);
        expect(this.resultSet.next()).andReturn(true);
        expect(this.resultSet.getInt(1)).andReturn(Integer.valueOf(0));
        this.resultSet.close();
        this.statement.close();
        expect(this.connection.prepareStatement(isA(String.class))).andReturn(this.statement);
        this.connection.close();
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        replay(this.dataSource, this.connection, this.statement, this.resultSet);
        assertFalse(this.service.login("0999", "123"));
        verify(this.dataSource, this.connection, this.statement);
    }

    @Test(expected = LanewebException.class)
    public void testUpdateError() throws SQLException {
        this.statement.setString(1, "0999");
        expect(this.statement.executeQuery()).andReturn(this.resultSet);
        expect(this.resultSet.next()).andReturn(true);
        expect(this.resultSet.getInt(1)).andReturn(Integer.valueOf(1));
        this.resultSet.close();
        this.statement.close();
        this.statement.setString(1, "0999");
        this.statement.setString(2, "123");
        expect(this.statement.executeUpdate()).andReturn(Integer.valueOf(0));
        this.statement.setString(1, "0999");
        this.statement.setString(2, "123");
        expect(this.statement.executeUpdate()).andThrow(new SQLException("oops"));
        this.statement.close();
        this.statement.close();
        expect(this.connection.prepareStatement(isA(String.class))).andReturn(this.statement).times(3);
        this.connection.close();
        expect(this.dataSource.getConnection()).andReturn(this.connection).times(2);
        replay(this.dataSource, this.connection, this.statement, this.resultSet);
        this.service.login("0999", "123");
    }
}
