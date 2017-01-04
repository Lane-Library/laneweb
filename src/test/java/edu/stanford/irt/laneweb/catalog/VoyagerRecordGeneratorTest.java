package edu.stanford.irt.laneweb.catalog;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertSame;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;

public class VoyagerRecordGeneratorTest {

    private CallableStatement callable;

    private Clob clob;

    private Connection connection;

    private DataSource dataSource;

    private VoyagerRecordGenerator generator;

    private XMLConsumer xmlConsumer;

    private XMLReader xmlReader;

    @Before
    public void setUp() throws IOException, SAXException {
        this.dataSource = createMock(DataSource.class);
        this.xmlReader = XMLReaderFactory.createXMLReader();
        InputStream sql = new ByteArrayInputStream("".getBytes());
        this.generator = new VoyagerRecordGenerator(this.dataSource, sql, 1, this.xmlReader);
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.connection = createMock(Connection.class);
        this.callable = createMock(CallableStatement.class);
        this.clob = createMock(Clob.class);
    }

    @Test
    public void testDoGenerate() throws SQLException, IOException, SAXException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        expect(this.connection.prepareCall(isA(String.class))).andReturn(this.callable);
        this.callable.registerOutParameter(1, Types.CLOB);
        expect(this.callable.execute()).andReturn(true);
        expect(this.callable.getClob(1)).andReturn(this.clob);
        expect(this.clob.getAsciiStream()).andReturn(new ByteArrayInputStream("<foo/>".getBytes()));
        this.clob.free();
        expectLastCall().times(2);
        this.callable.close();
        expectLastCall().times(2);
        this.connection.close();
        expectLastCall().times(2);
        replay(this.dataSource, this.connection, this.callable, this.clob);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.dataSource, this.connection, this.callable, this.clob);
    }

    @Test
    public void testDoGenerateIOException() throws SQLException, IOException {
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
        try {
            this.generator.doGenerate(this.xmlConsumer);
        } catch (LanewebException e) {
            assertSame(exception, e.getCause());
        }
        verify(this.dataSource, this.connection, this.callable, this.clob, input);
    }

    @Test(expected = LanewebException.class)
    public void testDoGenerateSQLException() throws SQLException {
        SQLException exception = new SQLException();
        expect(this.dataSource.getConnection()).andThrow(exception);
        replay(this.dataSource, this.connection, this.callable, this.clob);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.dataSource, this.connection, this.callable, this.clob);
    }
}
