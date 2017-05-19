package edu.stanford.irt.laneweb.catalog.equipment;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collections;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.LanewebException;

public class JDBCEquipmentStatusServiceTest {

    private CallableStatement callable;

    private Clob clob;

    private Connection connection;

    private DataSource dataSource;

    private ResultSet resultSet;

    private JDBCEquipmentStatusService service;

    private PreparedStatement statement;

    @Before
    public void setUp() throws IOException {
        this.dataSource = createMock(DataSource.class);
        this.service = new JDBCEquipmentStatusService(this.dataSource, "sql");
        this.connection = createMock(Connection.class);
        this.statement = createMock(PreparedStatement.class);
        this.resultSet = createMock(ResultSet.class);
        this.callable = createMock(CallableStatement.class);
        this.clob = createMock(Clob.class);
    }

    @Test
    public void testGetRecords() throws SQLException, IOException, SAXException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        expect(this.connection.prepareCall(isA(String.class))).andReturn(this.callable);
        this.callable.registerOutParameter(1, Types.CLOB);
        expect(this.callable.execute()).andReturn(true);
        expect(this.callable.getClob(1)).andReturn(this.clob);
        expect(this.clob.getAsciiStream()).andReturn(new ByteArrayInputStream("<foo/>".getBytes()));
        this.clob.free();
        this.callable.close();
        this.connection.close();
        replay(this.dataSource, this.connection, this.callable, this.clob);
        InputStream input = this.service.getRecords(Collections.emptyList());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i;
        while ((i = input.read()) != -1) {
            baos.write(i);
        }
        input.close();
        assertEquals("<foo/>", new String(baos.toByteArray()));
        verify(this.dataSource, this.connection, this.callable, this.clob);
    }

    @Test
    public void testGetRecordsIOException() throws SQLException, IOException {
        InputStream input = createMock(InputStream.class);
        IOException exception = new IOException();
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        expect(this.connection.prepareCall(isA(String.class))).andReturn(this.callable);
        this.callable.registerOutParameter(1, Types.CLOB);
        expect(this.callable.execute()).andReturn(true);
        expect(this.callable.getClob(1)).andReturn(this.clob);
        expect(this.clob.getAsciiStream()).andReturn(input);
        expect(input.read()).andThrow(exception);
        input.close();
        this.clob.free();
        this.callable.close();
        this.connection.close();
        replay(this.dataSource, this.connection, this.callable, this.clob, input);
        try (InputStream stream = this.service.getRecords(Collections.emptyList())) {
            stream.read();
        } catch (IOException e) {
            assertSame(exception, e);
        }
        verify(this.dataSource, this.connection, this.callable, this.clob, input);
    }

    @Test(expected = LanewebException.class)
    public void testGetRecordsSQLException() throws SQLException, IOException {
        SQLException exception = new SQLException();
        expect(this.dataSource.getConnection()).andThrow(exception);
        replay(this.dataSource, this.connection, this.callable, this.clob);
        this.service.getRecords(Collections.emptyList()).read();
        verify(this.dataSource, this.connection, this.callable, this.clob);
    }

    @Test
    public void testGetStatus() throws IOException, SAXException, SQLException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        expect(this.connection.prepareStatement(isA(String.class))).andReturn(this.statement);
        this.statement.setString(1, "304254,296290");
        this.statement.setString(2, "304254,296290");
        expect(this.statement.executeQuery()).andReturn(this.resultSet);
        expect(this.resultSet.next()).andReturn(true).times(2);
        expect(this.resultSet.getString(1)).andReturn("304254");
        expect(this.resultSet.getString(2)).andReturn("1");
        expect(this.resultSet.getString(1)).andReturn("296290");
        expect(this.resultSet.getString(2)).andReturn("10");
        expect(this.resultSet.next()).andReturn(false);
        this.resultSet.close();
        this.statement.close();
        this.connection.close();
        replay(this.dataSource, this.connection, this.statement, this.resultSet);
        this.service.getStatus("304254,296290");
        verify(this.dataSource, this.connection, this.statement, this.resultSet);
    }
}
