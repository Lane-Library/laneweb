package edu.stanford.irt.laneweb.audio;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

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
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.lane.catalog.Record;

public class AudioGeneratorTest {

    private CallableStatement callable;

    private Clob clob;

    private Connection connection;

    private DataSource dataSource;

    private AudioGenerator generator;

    private SAXStrategy<Record> saxStrategy;

    private InputStream sql;

    private XMLConsumer xmlConsumer;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws IOException {
        this.dataSource = createMock(DataSource.class);
        this.sql = getClass().getResourceAsStream("getAudio.fnc");
        this.saxStrategy = createMock(SAXStrategy.class);
        this.generator = new AudioGenerator(this.dataSource, this.sql, this.saxStrategy);
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.connection = createMock(Connection.class);
        this.callable = createMock(CallableStatement.class);
        this.clob = createMock(Clob.class);
    }

    @Test
    public void testDoGenerateXMLConsumer() throws SQLException, SAXException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        expect(this.connection.prepareCall(isA(String.class))).andReturn(this.callable);
        this.callable.registerOutParameter(1, Types.CLOB);
        expect(this.callable.execute()).andReturn(true);
        expect(this.callable.getClob(1)).andReturn(this.clob);
        expect(this.clob.getAsciiStream()).andReturn(new ByteArrayInputStream(new byte[0]));
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startElement(eq("http://www.loc.gov/MARC21/slim"), eq("collection"), eq("collection"),
                isA(Attributes.class));
        this.xmlConsumer.endElement("http://www.loc.gov/MARC21/slim", "collection", "collection");
        this.xmlConsumer.endDocument();
        this.clob.free();
        this.callable.close();
        this.connection.close();
        replay(this.dataSource, this.connection, this.callable, this.clob, this.xmlConsumer);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.dataSource, this.connection, this.callable, this.clob, this.xmlConsumer);
    }
}
