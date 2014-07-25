package edu.stanford.irt.laneweb.suggest;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.eresources.Eresource;

public class AbstractSuggestCollectionManagerTest {

    private static class TestAbstractSuggestCollectionManager extends AbstractSuggestCollectionManager {

        public TestAbstractSuggestCollectionManager(final DataSource dataSource, final Properties sqlStatements) {
            super(dataSource, sqlStatements);
        }

        @Override
        protected Collection<String> searchStringToParams(final String query) {
            List<String> result = new LinkedList<String>();
            result.add(query);
            return result;
        }
    }

    private Connection connection;

    private DataSource dataSource;

    private AbstractSuggestCollectionManager manager;

    private ResultSet resultSet;

    private Properties sqlStatements;

    private PreparedStatement statement;

    @Before
    public void setUp() throws Exception {
        this.dataSource = createMock(DataSource.class);
        this.sqlStatements = createMock(Properties.class);
        this.manager = new TestAbstractSuggestCollectionManager(this.dataSource, this.sqlStatements);
        this.connection = createMock(Connection.class);
        this.resultSet = createMock(ResultSet.class);
        this.statement = createMock(PreparedStatement.class);
    }

    @Test
    public void testParseResultSet() throws SQLException {
        expect(this.resultSet.next()).andReturn(true);
        expect(this.resultSet.getInt("ERESOURCE_ID")).andReturn(10);
        expect(this.resultSet.getString("TITLE")).andReturn("title");
        expect(this.resultSet.next()).andReturn(true);
        expect(this.resultSet.getInt("ERESOURCE_ID")).andReturn(10);
        expect(this.resultSet.getString("TITLE")).andReturn("anothertitle");
        expect(this.resultSet.next()).andReturn(true);
        expect(this.resultSet.getInt("ERESOURCE_ID")).andReturn(10);
        expect(this.resultSet.getString("TITLE")).andReturn("anothertitle");
        expect(this.resultSet.next()).andReturn(true);
        expect(this.resultSet.getInt("ERESOURCE_ID")).andReturn(30);
        expect(this.resultSet.getString("TITLE")).andReturn("anothertitle");
        expect(this.resultSet.next()).andReturn(false);
        replay(this.dataSource, this.sqlStatements, this.resultSet);
        List<Eresource> list = this.manager.parseResultSet(this.resultSet, "query");
        assertEquals("10", list.get(0).getId());
        assertEquals("title", list.get(0).getTitle());
        assertEquals(3, list.size());
        verify(this.dataSource, this.sqlStatements, this.resultSet);
    }

    @Test
    public void testSearch() throws SQLException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        expect(this.sqlStatements.getProperty("search")).andReturn("SQL");
        expect(this.connection.prepareStatement("SQL")).andReturn(this.statement);
        this.statement.setString(1, "query");
        expect(this.statement.executeQuery()).andReturn(this.resultSet);
        expect(this.resultSet.next()).andReturn(false);
        this.resultSet.close();
        this.statement.close();
        this.connection.close();
        replay(this.dataSource, this.sqlStatements, this.resultSet, this.connection, this.statement);
        this.manager.search("query");
        verify(this.dataSource, this.sqlStatements, this.resultSet, this.connection, this.statement);
    }

    @Test
    public void testSearchType() throws SQLException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        expect(this.sqlStatements.getProperty("search.type")).andReturn("SQL");
        expect(this.connection.prepareStatement("SQL")).andReturn(this.statement);
        this.statement.setString(1, "query");
        this.statement.setString(2, "type");
        expect(this.statement.executeQuery()).andReturn(this.resultSet);
        expect(this.resultSet.next()).andReturn(false);
        this.resultSet.close();
        this.statement.close();
        this.connection.close();
        replay(this.dataSource, this.sqlStatements, this.resultSet, this.connection, this.statement);
        this.manager.searchType("type", "query");
        verify(this.dataSource, this.sqlStatements, this.resultSet, this.connection, this.statement);
    }
}
