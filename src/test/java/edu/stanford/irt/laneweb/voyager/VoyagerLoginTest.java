package edu.stanford.irt.laneweb.voyager;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.user.User;

public class VoyagerLoginTest {

    private Connection connection;

    private DataSource dataSource;

    private User person;

    private PreparedStatement statement;

    private VoyagerLogin voyagerLogin;

    @Before
    public void setUp() throws Exception {
        this.voyagerLogin = new VoyagerLogin();
        this.person = createMock(User.class);
        this.dataSource = createMock(DataSource.class);
        this.connection = createMock(Connection.class);
        this.statement = createMock(PreparedStatement.class);
    }

    @Test
    public void testDeleteError() throws SQLException {
        this.statement.setString(1, "0999");
        this.statement.setString(2, "123");
        expect(this.statement.executeUpdate()).andThrow(new SQLException("oops"));
        this.statement.close();
        replay(this.statement);
        expect(this.connection.prepareStatement(isA(String.class))).andReturn(this.statement);
        this.connection.close();
        replay(this.connection);
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        replay(this.dataSource);
        expect(this.person.getUnivId()).andReturn("999");
        replay(this.person);
        this.voyagerLogin.setDataSource(this.dataSource);
        assertEquals("/voyagerError.html", this.voyagerLogin.getVoyagerURL(this.person, "123", "a=b"));
        verify(this.person);
        verify(this.dataSource);
        verify(this.connection);
        verify(this.statement);
    }

    @Test
    public void testErrorURL() {
        replay(this.person);
        assertEquals("/voyagerError.html", this.voyagerLogin.getVoyagerURL(null, "123", "a=b"));
        assertEquals("/voyagerError.html", this.voyagerLogin.getVoyagerURL(this.person, null, "a=b"));
        verify(this.person);
    }

    @Test
    public void testLoginURL() throws SQLException {
        this.statement.setString(1, "0999");
        this.statement.setString(2, "123");
        expect(this.statement.executeUpdate()).andReturn(new Integer(0));
        this.statement.setString(1, "0999");
        this.statement.setString(2, "123");
        expect(this.statement.executeUpdate()).andReturn(new Integer(1));
        this.statement.close();
        replay(this.statement);
        expect(this.connection.prepareStatement(isA(String.class))).andReturn(this.statement);
        expect(this.connection.prepareStatement(isA(String.class))).andReturn(this.statement);
        this.connection.close();
        replay(this.connection);
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        replay(this.dataSource);
        expect(this.person.getUnivId()).andReturn("999");
        replay(this.person);
        this.voyagerLogin.setDataSource(this.dataSource);
        assertEquals("http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?a=b&authenticate=Y", this.voyagerLogin.getVoyagerURL(this.person, "123", "a=b"));
        verify(this.person);
        verify(this.dataSource);
        verify(this.connection);
        verify(this.statement);
    }

    @Test
    public void testUpdateError() throws SQLException {
        this.statement.setString(1, "0999");
        this.statement.setString(2, "123");
        expect(this.statement.executeUpdate()).andReturn(new Integer(0));
        this.statement.setString(1, "0999");
        this.statement.setString(2, "123");
        expect(this.statement.executeUpdate()).andThrow(new SQLException("oops"));
        this.statement.close();
        replay(this.statement);
        expect(this.connection.prepareStatement(isA(String.class))).andReturn(this.statement);
        expect(this.connection.prepareStatement(isA(String.class))).andReturn(this.statement);
        this.connection.close();
        replay(this.connection);
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        replay(this.dataSource);
        expect(this.person.getUnivId()).andReturn("999");
        replay(this.person);
        this.voyagerLogin.setDataSource(this.dataSource);
        assertEquals("/voyagerError.html", this.voyagerLogin.getVoyagerURL(this.person, "123", "a=b"));
        verify(this.person);
        verify(this.dataSource);
        verify(this.connection);
        verify(this.statement);
    }
}
