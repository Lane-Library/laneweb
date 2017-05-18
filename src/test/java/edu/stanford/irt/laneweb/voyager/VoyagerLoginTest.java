package edu.stanford.irt.laneweb.voyager;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

public class VoyagerLoginTest {

    private Connection connection;

    private DataSource dataSource;

    private ResultSet resultSet;

    private PreparedStatement statement;

    private VoyagerLogin voyagerLogin;

    @Before
    public void setUp() throws Exception {
        this.dataSource = createMock(DataSource.class);
        this.voyagerLogin = new VoyagerLogin(this.dataSource);
        this.connection = createMock(Connection.class);
        this.statement = createMock(PreparedStatement.class);
        this.resultSet = createMock(ResultSet.class);
    }

    @Test
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
        assertEquals("/voyagerError.html", this.voyagerLogin.getVoyagerURL("999", "123", "a=b"));
        verify(this.dataSource, this.connection, this.statement);
    }

    @Test
    public void testErrorURL() {
        assertEquals("/voyagerError.html", this.voyagerLogin.getVoyagerURL(null, "123", "a=b"));
        assertEquals("/voyagerError.html", this.voyagerLogin.getVoyagerURL("", null, "a=b"));
        assertEquals("/voyagerError.html", this.voyagerLogin.getVoyagerURL("", null, "a=b"));
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
        assertEquals("http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?a=b&authenticate=Y",
                this.voyagerLogin.getVoyagerURL("999", "123", "a=b"));
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
        assertEquals("/voyagerError.html", this.voyagerLogin.getVoyagerURL("999", "123", "a=b"));
        verify(this.dataSource, this.connection, this.statement);
    }

    @Test
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
        assertEquals("/voyagerError.html", this.voyagerLogin.getVoyagerURL("999", "123", "a=b"));
        verify(this.dataSource, this.connection, this.statement);
    }
}
