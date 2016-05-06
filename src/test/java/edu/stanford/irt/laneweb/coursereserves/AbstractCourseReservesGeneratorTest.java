package edu.stanford.irt.laneweb.coursereserves;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;

public class AbstractCourseReservesGeneratorTest {

    private Connection connection;

    private DataSource dataSource;

    private AbstractCourseReservesGenerator generator;

    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() {
        this.dataSource = createMock(DataSource.class);
        this.generator = new AbstractCourseReservesGenerator(new ByteArrayInputStream("SQL".getBytes()),
                this.dataSource, null) {

            @Override
            protected void doGenerate(final Connection conn, final String sql, final SAXStrategy<ResultSet> saxStrategy,
                    final XMLConsumer xmlConsumer) throws SQLException {
            }
        };
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.connection = createMock(Connection.class);
    }

    @Test
    public void testDoGenerateXMLConsumer() throws SAXException, SQLException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        this.connection.close();
        replay(this.dataSource, this.connection);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.dataSource, this.connection);
    }
}
