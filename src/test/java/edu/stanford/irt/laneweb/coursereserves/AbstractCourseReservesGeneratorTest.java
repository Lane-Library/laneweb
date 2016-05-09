package edu.stanford.irt.laneweb.coursereserves;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;

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

    @Test(expected = LanewebException.class)
    public void testDoGenerateDataSourceThrows() throws SAXException, SQLException {
        expect(this.dataSource.getConnection()).andThrow(new SQLException());
        this.connection.close();
        replay(this.dataSource, this.connection);
        this.generator.doGenerate(this.xmlConsumer);
    }

    @Test
    public void testDoGenerateXMLConsumer() throws SAXException, SQLException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        this.connection.close();
        replay(this.dataSource, this.connection);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.dataSource, this.connection);
    }

    @Test(expected = LanewebException.class)
    public void testIOException() throws IOException {
        InputStream input = createMock(InputStream.class);
        expect(input.read(isA(byte[].class), eq(0), eq(8192))).andThrow(new IOException());
        replay(input);
        new AbstractCourseReservesGenerator(input, null, null) {

            @Override
            protected void doGenerate(final Connection conn, final String sql, final SAXStrategy<ResultSet> saxStrategy,
                    final XMLConsumer xmlConsumer) throws SQLException {
            }
        };
    }
}
