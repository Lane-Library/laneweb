package edu.stanford.irt.laneweb.coursereserves;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;

public class CourseReservesGeneratorTest {

    private Connection connection;

    private CourseReservesGenerator generator;

    private ResultSet resultSet;

    private SAXStrategy<ResultSet> saxStrategy;

    private Statement statement;

    private XMLConsumer xmlConsumer;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        this.generator = new CourseReservesGenerator(new ByteArrayInputStream("SQL".getBytes()), null, null);
        this.connection = createMock(Connection.class);
        this.statement = createMock(Statement.class);
        this.resultSet = createMock(ResultSet.class);
        this.saxStrategy = createMock(SAXStrategy.class);
        this.xmlConsumer = createMock(XMLConsumer.class);
    }

    @Test
    public void testDoGenerateXMLConsumer() throws SQLException {
        expect(this.connection.createStatement()).andReturn(this.statement);
        expect(this.statement.executeQuery(isA(String.class))).andReturn(this.resultSet);
        this.saxStrategy.toSAX(this.resultSet, this.xmlConsumer);
        this.resultSet.close();
        this.statement.close();
        replay(this.connection, this.statement, this.saxStrategy, this.xmlConsumer);
        this.generator.doGenerate(this.connection, "SQL", this.saxStrategy, this.xmlConsumer);
        verify(this.connection, this.statement, this.saxStrategy, this.xmlConsumer);
    }
}
