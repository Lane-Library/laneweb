package edu.stanford.irt.laneweb.eresources;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;


public class AbstractCollectionManagerTest {

    private static final class TestAbstractCollectionManager extends AbstractCollectionManager {

        public TestAbstractCollectionManager(final DataSource dataSource, final Properties sqlStatements) {
            super(dataSource, sqlStatements);
        }

        @Override
        protected List<Eresource> parseResultSet(final ResultSet rs, final String query) throws SQLException {
            return Collections.emptyList();
        }
    }

    private Connection connection;

    private DataSource dataSource;

    private AbstractCollectionManager manager;

    private ResultSet resultSet;

    private Properties sqlStatements;

    private PreparedStatement statement;

    @Before
    public void setUp() throws Exception {
        this.dataSource = createMock(DataSource.class);
        this.sqlStatements = createMock(Properties.class);
        this.manager = new TestAbstractCollectionManager(this.dataSource, this.sqlStatements);
        this.connection = createMock(Connection.class);
        this.statement = createMock(PreparedStatement.class);
        this.resultSet = createMock(ResultSet.class);
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        expect(this.connection.prepareStatement(isA(String.class))).andReturn(this.statement);
        expect(this.statement.executeQuery()).andReturn(this.resultSet);
        this.resultSet.close();
        this.statement.close();
        this.connection.close();
        replay(this.dataSource, this.connection);
    }

    @Test
    public void testGetCore() throws SQLException {
        expect(this.sqlStatements.getProperty("browse.core")).andReturn("");
        this.statement.setString(1, "type");
        expect(this.resultSet.next()).andReturn(false);
        replay(this.statement, this.resultSet, this.sqlStatements);
        this.manager.getCore("type");
    }

    @Test
    public void testGetCoreResults() throws SQLException {
        expect(this.sqlStatements.getProperty("browse.core")).andReturn("");
        this.statement.setString(1, "type");
        expect(this.resultSet.next()).andReturn(true);
        expect(this.resultSet.getInt(isA(String.class))).andReturn(0).times(4);
        expect(this.resultSet.getString(isA(String.class))).andReturn("string").times(10);
        expect(this.resultSet.next()).andReturn(false);
        replay(this.statement, this.resultSet, this.sqlStatements);
        this.manager.getCore("type");
    }

    @Test
    public void testGetMesh() throws SQLException {
        expect(this.sqlStatements.getProperty("browse.mesh")).andReturn("");
        this.statement.setString(1, "mesh");
        this.statement.setString(2, "type");
        expect(this.resultSet.next()).andReturn(false);
        replay(this.statement, this.resultSet, this.sqlStatements);
        this.manager.getMesh("type", "mesh");
    }

    @Test
    public void testGetSubset() throws SQLException {
        expect(this.sqlStatements.getProperty("browse.subset")).andReturn("");
        this.statement.setString(1, "subset");
        expect(this.resultSet.next()).andReturn(false);
        replay(this.statement, this.resultSet, this.sqlStatements);
        this.manager.getSubset("subset");
    }

    @Test
    public void testGetTypeString() throws SQLException {
        expect(this.sqlStatements.getProperty("browse")).andReturn("");
        this.statement.setString(1, "type");
        expect(this.resultSet.next()).andReturn(false);
        replay(this.statement, this.resultSet, this.sqlStatements);
        this.manager.getType("type");
    }

    @Test
    public void testGetTypeStringChar() throws SQLException {
        expect(this.sqlStatements.getProperty("browse.alpha")).andReturn("");
        this.statement.setString(1, "string");
        this.statement.setString(2, "c");
        expect(this.resultSet.next()).andReturn(false);
        replay(this.statement, this.resultSet, this.sqlStatements);
        this.manager.getType("string", 'c');
    }

    @Test
    public void testSearch() throws SQLException {
        expect(this.sqlStatements.getProperty("search")).andReturn("");
        this.statement.setString(1, "((${query})) ");
        this.statement.setString(2, "((${query})) ");
        expect(this.resultSet.next()).andReturn(false);
        replay(this.statement, this.resultSet, this.sqlStatements);
        this.manager.search("query");
    }

    @Test
    public void testSearchCountSetOfStringSetOfStringString() throws SQLException {
        expect(this.sqlStatements.getProperty("search.count.0")).andReturn("");
        expect(this.sqlStatements.getProperty("search.count.1")).andReturn("");
        this.statement.setString(1, "((${query})) ");
        this.statement.setString(2, "type");
        this.statement.setString(3, "type");
        expect(this.resultSet.next()).andReturn(false);
        replay(this.statement, this.resultSet, this.sqlStatements);
        this.manager.searchCount(Collections.singleton("type"), Collections.singleton("subset"), "query");
    }

    @Test
    public void testSearchCountSetOfStringString() throws SQLException {
        expect(this.sqlStatements.getProperty("search.count.0")).andReturn("");
        expect(this.sqlStatements.getProperty("search.count.1")).andReturn("");
        this.statement.setString(1, "((${query})) ");
        this.statement.setString(2, "type");
        this.statement.setString(3, "type");
        expect(this.resultSet.next()).andReturn(false);
        replay(this.statement, this.resultSet, this.sqlStatements);
        this.manager.searchCount(Collections.singleton("type"), "query");
    }

    @Test
    public void testSearchType() throws SQLException {
        expect(this.sqlStatements.getProperty("search.type")).andReturn("");
        this.statement.setString(1, "((${query})) ");
        this.statement.setString(2, "((${query})) ");
        this.statement.setString(3, "type");
        expect(this.resultSet.next()).andReturn(false);
        replay(this.statement, this.resultSet, this.sqlStatements);
        this.manager.searchType("type", "query");
    }
}
