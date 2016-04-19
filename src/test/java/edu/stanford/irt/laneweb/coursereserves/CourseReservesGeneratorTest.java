package edu.stanford.irt.laneweb.coursereserves;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.model.Model;

public class CourseReservesGeneratorTest {

    private Connection connection;

    private DataSource dataSource;

    private CourseReservesGenerator generator;

    private ResultSet resultSet;

    private SAXStrategy<ResultSet> saxStrategy;

    private PreparedStatement statement;

    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() {
        this.dataSource = createMock(DataSource.class);
        this.saxStrategy = createMock(SAXStrategy.class);
        this.generator = new CourseReservesGenerator(this.dataSource, this.saxStrategy);
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.connection = createMock(Connection.class);
        this.statement = createMock(PreparedStatement.class);
        this.resultSet = createMock(ResultSet.class);
    }

    @Test
    public void testDoGenerateXMLConsumer() throws SAXException, SQLException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        expect(this.connection.prepareStatement(isA(String.class))).andReturn(this.statement);
        this.statement.setString(1, "12");
        expect(this.statement.executeQuery()).andReturn(this.resultSet);
        this.saxStrategy.toSAX(this.resultSet, this.xmlConsumer);
        this.resultSet.close();
        this.statement.close();
        this.connection.close();
        replay(this.dataSource, this.saxStrategy, this.connection, this.statement);
        this.generator.setModel(Collections.singletonMap(Model.ID, "12"));
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.dataSource, this.saxStrategy, this.connection, this.statement);
    }
}
