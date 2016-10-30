package edu.stanford.irt.laneweb.proxy;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.LanewebException;

public class EzproxyServersWriterTest {

    private Connection connection;

    private DataSource dataSource;

    private String expected = "T value\nU value\nHJ value\n\nT bodoni.stanford.edu\nU http://bodoni.stanford.edu\nHJ bodoni.stanford.edu\n\nT library.stanford.edu\nU http://library.stanford.edu\nHJ library.stanford.edu\n\nT searchworks.stanford.edu\nU http://searchworks.stanford.edu\nHJ searchworks.stanford.edu";

    private ResultSet resultSet;

    private Statement statement;

    private EzproxyServersWriter writer;

    @Before
    public void setUp() throws Exception {
        this.dataSource = createMock(DataSource.class);
        Properties props = new Properties();
        props.setProperty("ezproxy-servers.query", "sql query");
        this.writer = new EzproxyServersWriter(this.dataSource, props);
        this.connection = createMock(Connection.class);
        this.statement = createMock(Statement.class);
        this.resultSet = createMock(ResultSet.class);
    }

    @Test
    public void testNullOutputStream() throws IOException {
        try {
            this.writer.write(null);
            fail("Should throw NullPointerException");
        } catch (NullPointerException e) {
        }
    }

    @Test
    public void testWrite() throws SQLException, IOException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        expect(this.connection.createStatement()).andReturn(this.statement);
        expect(this.statement.executeQuery("sql query")).andReturn(this.resultSet);
        expect(this.resultSet.next()).andReturn(true);
        expect(this.resultSet.getString(1)).andReturn("value");
        expect(this.resultSet.next()).andReturn(false);
        this.resultSet.close();
        this.statement.close();
        this.connection.close();
        replay(this.dataSource, this.connection, this.statement, this.resultSet);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        this.writer.write(baos);
        assertEquals(this.expected, new String(baos.toByteArray()));
        verify(this.dataSource, this.connection, this.statement, this.resultSet);
    }

    @Test
    public void testWriteThrowsSQLException() throws SQLException, IOException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        expect(this.connection.createStatement()).andReturn(this.statement);
        expect(this.statement.executeQuery("sql query")).andReturn(this.resultSet);
        expect(this.resultSet.next()).andReturn(true);
        expect(this.resultSet.getString(1)).andReturn("value");
        expect(this.resultSet.next()).andThrow(new SQLException());
        this.resultSet.close();
        this.statement.close();
        this.connection.close();
        replay(this.dataSource, this.connection, this.statement, this.resultSet);
        try {
            this.writer.write(new ByteArrayOutputStream());
        } catch (LanewebException e) {
        }
        verify(this.dataSource, this.connection, this.statement, this.resultSet);
    }
}
