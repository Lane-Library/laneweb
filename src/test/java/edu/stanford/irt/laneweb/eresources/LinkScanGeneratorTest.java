package edu.stanford.irt.laneweb.eresources;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.newCapture;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;

public class LinkScanGeneratorTest {

    private Connection connection;

    private DataSource datasource;

    private LinkScanGenerator generator;

    private ResultSet resultSet;

    private Statement statement;

    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() throws Exception {
        this.datasource = createMock(DataSource.class);
        this.generator = new LinkScanGenerator(this.datasource);
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.connection = createMock(Connection.class);
        this.statement = createMock(Statement.class);
        this.resultSet = createMock(ResultSet.class);
    }

    @Test
    public void testDoGenerate() throws SQLException, SAXException {
        expect(this.datasource.getConnection()).andReturn(this.connection);
        expect(this.connection.createStatement()).andReturn(this.statement);
        expect(this.statement.executeQuery(isA(String.class))).andReturn(this.resultSet);
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("ul"), eq("ul"), isA(Attributes.class));
        expect(this.resultSet.next()).andReturn(true);
        expect(this.resultSet.getString(1)).andReturn("url");
        expect(this.resultSet.getString(2)).andReturn("type");
        expect(this.resultSet.getString(3)).andReturn("id");
        expect(this.resultSet.getString(4)).andReturn("title");
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("li"), eq("li"), isA(Attributes.class));
        this.xmlConsumer.characters(aryEq(" #1 ".toCharArray()), eq(0), eq(4));
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("ul"), eq("ul"), isA(Attributes.class));
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("li"), eq("li"), isA(Attributes.class));
        Capture<Attributes> attributes = newCapture();
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("a"), eq("a"), capture(attributes));
        this.xmlConsumer.characters(aryEq(" id: type-id title: title".toCharArray()), eq(0), eq(25));
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "a", "a");
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "li", "li");
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "ul", "ul");
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "li", "li");
        expect(this.resultSet.next()).andReturn(false);
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "ul", "ul");
        this.xmlConsumer.endDocument();
        this.resultSet.close();
        this.statement.close();
        this.connection.close();
        replay(this.datasource, this.xmlConsumer, this.connection, this.statement, this.resultSet);
        this.generator.doGenerate(this.xmlConsumer);
        assertEquals("url", attributes.getValue().getValue("href"));
        verify(this.datasource, this.xmlConsumer, this.connection, this.statement, this.resultSet);
    }

    @Test
    public void testDoGenerateNullTitleUrl() throws SQLException, SAXException {
        expect(this.datasource.getConnection()).andReturn(this.connection);
        expect(this.connection.createStatement()).andReturn(this.statement);
        expect(this.statement.executeQuery(isA(String.class))).andReturn(this.resultSet);
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("ul"), eq("ul"), isA(Attributes.class));
        expect(this.resultSet.next()).andReturn(true);
        expect(this.resultSet.getString(1)).andReturn(null);
        expect(this.resultSet.getString(2)).andReturn("type");
        expect(this.resultSet.getString(3)).andReturn("id");
        expect(this.resultSet.getString(4)).andReturn(null);
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("li"), eq("li"), isA(Attributes.class));
        this.xmlConsumer.characters(aryEq(" #1 ".toCharArray()), eq(0), eq(4));
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("ul"), eq("ul"), isA(Attributes.class));
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("li"), eq("li"), isA(Attributes.class));
        Capture<Attributes> attributes = newCapture();
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("a"), eq("a"), capture(attributes));
        this.xmlConsumer.characters(aryEq(" id: type-id title: NULL TITLE".toCharArray()), eq(0), eq(30));
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "a", "a");
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "li", "li");
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "ul", "ul");
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "li", "li");
        expect(this.resultSet.next()).andReturn(false);
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "ul", "ul");
        this.xmlConsumer.endDocument();
        this.resultSet.close();
        this.statement.close();
        this.connection.close();
        replay(this.datasource, this.xmlConsumer, this.connection, this.statement, this.resultSet);
        this.generator.doGenerate(this.xmlConsumer);
        assertEquals("NULL URL", attributes.getValue().getValue("href"));
        verify(this.datasource, this.xmlConsumer, this.connection, this.statement, this.resultSet);
    }

    @Test
    public void testDoGenerateThrowSAXException() throws SQLException, SAXException {
        expect(this.datasource.getConnection()).andReturn(this.connection);
        expect(this.connection.createStatement()).andReturn(this.statement);
        expect(this.statement.executeQuery(isA(String.class))).andReturn(this.resultSet);
        this.xmlConsumer.startDocument();
        expectLastCall().andThrow(new SAXException());
        this.resultSet.close();
        this.statement.close();
        this.connection.close();
        replay(this.datasource, this.xmlConsumer, this.connection, this.statement, this.resultSet);
        try {
            this.generator.doGenerate(this.xmlConsumer);
        } catch (LanewebException e) {
        }
        verify(this.datasource, this.xmlConsumer, this.connection, this.statement, this.resultSet);
    }

    @Test
    public void testDoGenerateThrowSQLException() throws SQLException, SAXException {
        expect(this.datasource.getConnection()).andReturn(this.connection);
        expect(this.connection.createStatement()).andThrow(new SQLException());
        this.connection.close();
        replay(this.datasource, this.xmlConsumer, this.connection, this.statement, this.resultSet);
        try {
            this.generator.doGenerate(this.xmlConsumer);
        } catch (LanewebException e) {
        }
        verify(this.datasource, this.xmlConsumer, this.connection, this.statement, this.resultSet);
    }
}
