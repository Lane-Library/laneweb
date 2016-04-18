package edu.stanford.irt.laneweb.coursereserves;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;

public class DepartmentsGeneratorTest {

    private Connection connection;

    private DataSource dataSource;

    private DepartmentsGenerator generator;

    private ResultSet resultSet;

    private SAXStrategy<ResultSet> saxStrategy;

    private Statement statement;

    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() {
        this.dataSource = createMock(DataSource.class);
        this.saxStrategy = createMock(SAXStrategy.class);
        this.generator = new DepartmentsGenerator(this.dataSource, this.saxStrategy);
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.connection = createMock(Connection.class);
        this.statement = createMock(Statement.class);
        this.resultSet = createMock(ResultSet.class);
    }

    @Test
    public void testDoGenerateXMLConsumer() throws SQLException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        expect(this.connection.createStatement()).andReturn(this.statement);
        expect(this.statement.executeQuery(isA(String.class))).andReturn(this.resultSet);
        this.resultSet.close();
        this.statement.close();
        this.connection.close();
        this.saxStrategy.toSAX(this.resultSet, this.xmlConsumer);
        replay(this.dataSource, this.saxStrategy, this.connection, this.statement);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.dataSource, this.saxStrategy, this.connection, this.statement);
    }
}
