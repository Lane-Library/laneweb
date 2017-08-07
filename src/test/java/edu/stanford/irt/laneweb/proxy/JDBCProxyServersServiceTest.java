package edu.stanford.irt.laneweb.proxy;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.LanewebException;

public class JDBCProxyServersServiceTest {

    private Connection connection;

    private DataSource dataSource;

    private String expected = "T value\nU https://value\nHJ value\nHJ value:443\n\nT bodoni.stanford.edu\nU https://bodoni.stanford.edu\nHJ bodoni.stanford.edu\nHJ bodoni.stanford.edu:443\n\nT library.stanford.edu\nU https://library.stanford.edu\nHJ library.stanford.edu\nHJ library.stanford.edu:443\n\nT searchworks.stanford.edu\nU https://searchworks.stanford.edu\nHJ searchworks.stanford.edu\nHJ searchworks.stanford.edu:443\n\n";

    private ResultSet resultSet;

    private ProxyServersService service;

    private Statement statement;

    @Before
    public void setUp() throws Exception {
        this.dataSource = createMock(DataSource.class);
        this.service = new JDBCProxyServersService(this.dataSource, "proxyHostsSQL");
        this.connection = createMock(Connection.class);
        this.statement = createMock(Statement.class);
        this.resultSet = createMock(ResultSet.class);
    }

    @Test
    public void testDatabaseProxyHostSet() throws SQLException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        expect(this.connection.createStatement()).andReturn(this.statement);
        expect(this.statement.executeQuery("proxyHostsSQL")).andReturn(this.resultSet);
        expect(this.resultSet.next()).andReturn(true);
        expect(this.resultSet.getString(1)).andReturn("host");
        expect(this.resultSet.next()).andReturn(false);
        this.resultSet.close();
        this.statement.close();
        this.connection.close();
        replay(this.dataSource, this.connection, this.statement, this.resultSet);
        Set<String> proxyHosts = this.service.getHosts();
        assertTrue(proxyHosts.contains("host"));
        assertTrue(proxyHosts.contains("bodoni.stanford.edu"));
        assertTrue(proxyHosts.contains("library.stanford.edu"));
        assertTrue(proxyHosts.contains("searchworks.stanford.edu"));
        verify(this.dataSource, this.connection, this.statement, this.resultSet);
    }

    @Test(expected = LanewebException.class)
    public void testDatabaseProxyHostSetThrowsException() throws SQLException {
        expect(this.dataSource.getConnection()).andThrow(new SQLException());
        replay(this.dataSource);
        this.service.getHosts();
    }

    @Test
    public void testNullOutputStream() throws IOException {
        try {
            this.service.write(null);
            fail("Should throw NullPointerException");
        } catch (NullPointerException e) {
        }
    }

    @Test
    public void testWrite() throws SQLException, IOException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        expect(this.connection.createStatement()).andReturn(this.statement);
        expect(this.statement.executeQuery("proxyHostsSQL")).andReturn(this.resultSet);
        expect(this.resultSet.next()).andReturn(true);
        expect(this.resultSet.getString(1)).andReturn("value");
        expect(this.resultSet.next()).andReturn(false);
        this.resultSet.close();
        this.statement.close();
        this.connection.close();
        replay(this.dataSource, this.connection, this.statement, this.resultSet);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        this.service.write(baos);
        assertEquals(this.expected, new String(baos.toByteArray()));
        verify(this.dataSource, this.connection, this.statement, this.resultSet);
    }

    @Test
    public void testWriteThrowsSQLException() throws SQLException, IOException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        expect(this.connection.createStatement()).andReturn(this.statement);
        expect(this.statement.executeQuery("proxyHostsSQL")).andReturn(this.resultSet);
        expect(this.resultSet.next()).andReturn(true);
        expect(this.resultSet.getString(1)).andReturn("value");
        expect(this.resultSet.next()).andThrow(new SQLException());
        this.resultSet.close();
        this.statement.close();
        this.connection.close();
        replay(this.dataSource, this.connection, this.statement, this.resultSet);
        try {
            this.service.write(new ByteArrayOutputStream());
        } catch (LanewebException e) {
        }
        verify(this.dataSource, this.connection, this.statement, this.resultSet);
    }
}
