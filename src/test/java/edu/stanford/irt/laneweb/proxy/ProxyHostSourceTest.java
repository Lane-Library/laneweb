package edu.stanford.irt.laneweb.proxy;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

public class ProxyHostSourceTest {

    private Connection connection;

    private DataSource dataSource;

    private ProxyHostSource hostSource;

    private ResultSet resultSet;

    private Statement statement;

    @Before
    public void setUp() {
        this.dataSource = createMock(DataSource.class);
        this.hostSource = new ProxyHostSource(this.dataSource);
        this.connection = createMock(Connection.class);
        this.statement = createMock(Statement.class);
        this.resultSet = createMock(ResultSet.class);
    }

    @Test
    public void testDatabaseProxyHostSet() throws SQLException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        expect(this.connection.createStatement()).andReturn(this.statement);
        expect(this.statement.executeQuery(isA(String.class))).andReturn(this.resultSet);
        expect(this.resultSet.next()).andReturn(true);
        expect(this.resultSet.getString(1)).andReturn("host");
        expect(this.resultSet.next()).andReturn(false);
        this.resultSet.close();
        this.statement.close();
        this.connection.close();
        replay(this.dataSource, this.connection, this.statement, this.resultSet);
        Set<String> proxyHosts = this.hostSource.getHosts();
        assertTrue(proxyHosts.contains("host"));
        assertTrue(proxyHosts.contains("bodoni.stanford.edu"));
        assertTrue(proxyHosts.contains("library.stanford.edu"));
        assertTrue(proxyHosts.contains("searchworks.stanford.edu"));
        verify(this.dataSource, this.connection, this.statement, this.resultSet);
    }

    @Test(expected = SQLException.class)
    public void testDatabaseProxyHostSetThrowsException() throws SQLException {
        expect(this.dataSource.getConnection()).andThrow(new SQLException());
        replay(this.dataSource);
        this.hostSource.getHosts();
    }
}
