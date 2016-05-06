package edu.stanford.irt.laneweb.coursereserves;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;

public class CourseReservesDepartmentGeneratorTest {

    private Connection connection;

    private CourseReservesDepartmentGenerator generator;

    private ResultSet resultSet;

    private SAXStrategy<ResultSet> saxStrategy;

    private PreparedStatement statement;

    private XMLConsumer xmlConsumer;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        this.generator = new CourseReservesDepartmentGenerator(new ByteArrayInputStream("SQL".getBytes()), null, null);
        this.connection = createMock(Connection.class);
        this.statement = createMock(PreparedStatement.class);
        this.resultSet = createMock(ResultSet.class);
        this.saxStrategy = createMock(SAXStrategy.class);
        this.xmlConsumer = createMock(XMLConsumer.class);
    }

    @Test
    public void testDoGenerateXMLConsumer() throws SQLException {
        expect(this.connection.prepareStatement("SQL")).andReturn(this.statement);
        this.statement.setString(1, "10");
        expect(this.statement.executeQuery()).andReturn(this.resultSet);
        this.saxStrategy.toSAX(this.resultSet, this.xmlConsumer);
        this.resultSet.close();
        this.statement.close();
        replay(this.connection, this.statement, this.saxStrategy, this.xmlConsumer);
        this.generator.setModel(Collections.singletonMap("id", "10"));
        this.generator.doGenerate(this.connection, "SQL", this.saxStrategy, this.xmlConsumer);
        verify(this.connection, this.statement, this.saxStrategy, this.xmlConsumer);
    }
}
