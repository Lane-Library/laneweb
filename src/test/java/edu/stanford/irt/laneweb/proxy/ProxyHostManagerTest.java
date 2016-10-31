package edu.stanford.irt.laneweb.proxy;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

public class ProxyHostManagerTest {

    private Connection connection;

    private DataSource dataSource;

    private ProxyHostManager manager;

    private ResultSet resultSet;

    private Statement statement;

    @Before
    public void setUp() throws Exception {
        this.dataSource = createMock(DataSource.class);
        this.manager = new ProxyHostManager(this.dataSource);
        this.connection = createMock(Connection.class);
        this.statement = createMock(Statement.class);
        this.resultSet = createMock(ResultSet.class);
    }

    @Test
    public void testDestroy() throws InterruptedException, SQLException {
        ExecutorService executor = createMock(ExecutorService.class);
        ProxyHostManager m = new ProxyHostManager(this.dataSource, executor);
        expect(executor.shutdownNow()).andReturn(null);
        replay(this.dataSource, executor);
        m.destroy();
        verify(this.dataSource, executor);
    }

    @Test
    public void testIsProxyableHost() throws SQLException, InterruptedException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        expect(this.connection.createStatement()).andReturn(this.statement);
        expect(this.statement.executeQuery(isA(String.class))).andReturn(this.resultSet);
        expect(this.resultSet.next()).andReturn(false);
        this.connection.close();
        this.statement.close();
        this.resultSet.close();
        replay(this.dataSource, this.connection, this.statement, this.resultSet);
        assertTrue(this.manager.isProxyableHost("library.stanford.edu"));
        Thread.sleep(100);
        verify(this.dataSource, this.connection, this.statement, this.resultSet);
    }

    @Test
    public void testIsProxyableHostNull() {
        assertFalse(this.manager.isProxyableHost(null));
    }

    @Test
    public void testIsProxyableLink() throws SQLException, InterruptedException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        expect(this.connection.createStatement()).andReturn(this.statement);
        expect(this.statement.executeQuery(isA(String.class))).andReturn(this.resultSet);
        expect(this.resultSet.next()).andReturn(false);
        this.connection.close();
        this.statement.close();
        this.resultSet.close();
        replay(this.dataSource, this.connection, this.statement, this.resultSet);
        assertTrue(this.manager.isProxyableLink("http://library.stanford.edu/foo"));
        Thread.sleep(100);
        verify(this.dataSource, this.connection, this.statement, this.resultSet);
    }

    @Test
    public void testIsProxyableLinkMalformedURL() {
        assertFalse(this.manager.isProxyableLink("://library.stanford.edu/foo"));
    }

    @Test
    public void testIsProxyableLinkNull() {
        assertFalse(this.manager.isProxyableLink(null));
    }

    @Test
    public void testUpdate() throws InterruptedException, SQLException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        expect(this.connection.createStatement()).andReturn(this.statement);
        expect(this.statement.executeQuery(isA(String.class))).andReturn(this.resultSet);
        expect(this.resultSet.next()).andReturn(true);
        expect(this.resultSet.getString(1)).andReturn("foo.bar");
        expect(this.resultSet.next()).andReturn(false);
        this.resultSet.close();
        this.statement.close();
        this.connection.close();
        replay(this.dataSource, this.connection, this.statement, this.resultSet);
        this.manager.isProxyableHost("foo.bar");
        Thread.sleep(200L);
        assertTrue(this.manager.isProxyableHost("foo.bar"));
        verify(this.dataSource, this.connection, this.statement, this.resultSet);
    }
}