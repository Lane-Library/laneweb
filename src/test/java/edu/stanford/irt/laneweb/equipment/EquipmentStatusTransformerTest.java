package edu.stanford.irt.laneweb.equipment;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.TestXMLConsumer;

public class EquipmentStatusTransformerTest {

    private Connection connection;

    private DataSource dataSource;

    private ResultSet resultSet;

    private PreparedStatement statement;

    private EquipmentStatusTransformer transformer;

    private TestXMLConsumer xmlConsumer;

    private XMLReader xmlReader;

    @Before
    public void setUp() throws SAXException {
        this.dataSource = createMock(DataSource.class);
        this.transformer = new EquipmentStatusTransformer(this.dataSource);
        this.xmlConsumer = new TestXMLConsumer();
        this.transformer.setXMLConsumer(this.xmlConsumer);
        this.xmlReader = XMLReaderFactory.createXMLReader();
        this.xmlReader.setContentHandler(this.transformer);
        this.connection = createMock(Connection.class);
        this.statement = createMock(PreparedStatement.class);
        this.resultSet = createMock(ResultSet.class);
    }

    @Test
    public void test() throws IOException, SAXException, SQLException {
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
        this.xmlReader.parse(new InputSource(getClass().getResourceAsStream("equipment.html")));
        assertEquals(this.xmlConsumer.getExpectedResult(this, "EquipmentStatusTransformerTest-test.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.dataSource, this.connection, this.statement, this.resultSet);
    }

    @Test(expected = LanewebException.class)
    public void testSQLException() throws SQLException, IOException, SAXException {
        expect(this.dataSource.getConnection()).andThrow(new SQLException());
        replay(this.dataSource);
        this.xmlReader.parse(new InputSource(getClass().getResourceAsStream("equipment.html")));
    }
}
