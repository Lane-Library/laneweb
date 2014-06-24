package edu.stanford.irt.laneweb.bassett;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

public class BassettCollectionManagerTest {

    private Connection connection;

    private DataSource dataSource;

    private BassettCollectionManager manager;

    private ResultSet resultSet;

    private Properties sqlStatements;

    private PreparedStatement statement;

    @Before
    public void setUp() throws Exception {
        this.dataSource = createMock(DataSource.class);
        this.sqlStatements = createMock(Properties.class);
        this.manager = new BassettCollectionManager(this.dataSource, this.sqlStatements);
        this.connection = createMock(Connection.class);
        this.statement = createMock(PreparedStatement.class);
        this.resultSet = createMock(ResultSet.class);
    }

    @Test
    public void testGetById() throws SQLException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        expect(this.sqlStatements.getProperty("search.number")).andReturn("");
        expect(this.connection.prepareStatement(isA(String.class))).andReturn(this.statement);
        this.statement.setString(1, "bn");
        expect(this.statement.executeQuery()).andReturn(this.resultSet);
        expect(this.resultSet.next()).andReturn(true);
        expect(this.resultSet.getInt("ERESOURCE_ID")).andReturn(1);
        expect(this.resultSet.getString(isA(String.class))).andReturn("string").times(9);
        expect(this.resultSet.next()).andReturn(false);
        this.resultSet.close();
        this.statement.close();
        this.connection.close();
        replay(this.dataSource, this.connection, this.statement, this.resultSet, this.sqlStatements);
        this.manager.getById("bn");
        verify(this.dataSource, this.connection, this.statement, this.resultSet, this.sqlStatements);
    }

    @Test
    public void testGetRegion() throws SQLException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        expect(this.sqlStatements.getProperty("browse.region")).andReturn("");
        expect(this.connection.prepareStatement(isA(String.class))).andReturn(this.statement);
        this.statement.setString(1, "region");
        expect(this.statement.executeQuery()).andReturn(this.resultSet);
        expect(this.resultSet.next()).andReturn(true);
        expect(this.resultSet.getInt("ERESOURCE_ID")).andReturn(1);
        expect(this.resultSet.getString(isA(String.class))).andReturn("string").times(7);
        expect(this.resultSet.next()).andReturn(false);
        this.resultSet.close();
        this.statement.close();
        this.connection.close();
        replay(this.dataSource, this.connection, this.statement, this.resultSet, this.sqlStatements);
        this.manager.getRegion("region");
        verify(this.dataSource, this.connection, this.statement, this.resultSet, this.sqlStatements);
    }

    @Test
    public void testGetRegionWithSubregion() throws SQLException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        expect(this.sqlStatements.getProperty("browse.subregion")).andReturn("");
        expect(this.connection.prepareStatement(isA(String.class))).andReturn(this.statement);
        this.statement.setString(1, "region");
        this.statement.setString(2, "subregion");
        expect(this.statement.executeQuery()).andReturn(this.resultSet);
        expect(this.resultSet.next()).andReturn(true);
        expect(this.resultSet.getInt("ERESOURCE_ID")).andReturn(1);
        expect(this.resultSet.getString(isA(String.class))).andReturn("string").times(7);
        expect(this.resultSet.next()).andReturn(false);
        this.resultSet.close();
        this.statement.close();
        this.connection.close();
        replay(this.dataSource, this.connection, this.statement, this.resultSet, this.sqlStatements);
        this.manager.getRegion("region--subregion");
        verify(this.dataSource, this.connection, this.statement, this.resultSet, this.sqlStatements);
    }

    @Test
    public void testSearch() throws SQLException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        expect(this.sqlStatements.getProperty("search")).andReturn("");
        expect(this.connection.prepareStatement(isA(String.class))).andReturn(this.statement);
        this.statement.setString(eq(1), isA(String.class));
        this.statement.setString(eq(2), isA(String.class));
        expect(this.statement.executeQuery()).andReturn(this.resultSet);
        expect(this.resultSet.next()).andReturn(true);
        expect(this.resultSet.getInt("ERESOURCE_ID")).andReturn(1);
        expect(this.resultSet.getString("TITLE")).andReturn("title");
        expect(this.resultSet.getString("IMAGE")).andReturn("image");
        expect(this.resultSet.getString("LATIN_LEGEND")).andReturn("latinLegend");
        expect(this.resultSet.getString("BASSETT_NUMBER")).andReturn("bassettNumber");
        expect(this.resultSet.getString("DIAGRAM")).andReturn("diagram");
        expect(this.resultSet.getString("SUB_REGION")).andReturn("subregion");
        expect(this.resultSet.getString("REGION")).andReturn("region");
        expect(this.resultSet.next()).andReturn(true);
        expect(this.resultSet.getInt("ERESOURCE_ID")).andReturn(2);
        expect(this.resultSet.getString("TITLE")).andReturn("query");
        expect(this.resultSet.getString("IMAGE")).andReturn("image");
        expect(this.resultSet.getString("LATIN_LEGEND")).andReturn("latinLegend");
        expect(this.resultSet.getString("BASSETT_NUMBER")).andReturn("bassettNumber");
        expect(this.resultSet.getString("DIAGRAM")).andReturn("diagram");
        expect(this.resultSet.getString("SUB_REGION")).andReturn(null);
        expect(this.resultSet.getString("REGION")).andReturn("region");
        expect(this.resultSet.next()).andReturn(true);
        expect(this.resultSet.getInt("ERESOURCE_ID")).andReturn(2);
        expect(this.resultSet.getString("TITLE")).andReturn("title");
        expect(this.resultSet.getString("SUB_REGION")).andReturn("subregion");
        expect(this.resultSet.getString("REGION")).andReturn("region");
        expect(this.resultSet.next()).andReturn(false);
        this.resultSet.close();
        this.statement.close();
        this.connection.close();
        replay(this.dataSource, this.connection, this.statement, this.resultSet, this.sqlStatements);
        this.manager.search("query");
        verify(this.dataSource, this.connection, this.statement, this.resultSet, this.sqlStatements);
    }

    @Test(expected = IllegalStateException.class)
    public void testSearchThrowsException() throws SQLException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        expect(this.sqlStatements.getProperty("search")).andReturn("");
        expect(this.connection.prepareStatement(isA(String.class))).andReturn(this.statement);
        this.statement.setString(eq(1), isA(String.class));
        this.statement.setString(eq(2), isA(String.class));
        expect(this.statement.executeQuery()).andThrow(new SQLException());
        this.statement.close();
        this.connection.close();
        replay(this.dataSource, this.connection, this.statement, this.resultSet, this.sqlStatements);
        this.manager.search("query");
        verify(this.dataSource, this.connection, this.statement, this.resultSet, this.sqlStatements);
    }

    @Test
    public void testSearchCount() throws SQLException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        expect(this.sqlStatements.getProperty("search.count")).andReturn("");
        expect(this.connection.prepareStatement(isA(String.class))).andReturn(this.statement);
        this.statement.setString(eq(1), isA(String.class));
        this.statement.setString(eq(2), isA(String.class));
        expect(this.statement.executeQuery()).andReturn(this.resultSet);
        expect(this.resultSet.next()).andReturn(true);
        expect(this.resultSet.getInt("SUB_REGION_COUNT")).andReturn(10);
        expect(this.resultSet.getString("REGION")).andReturn("region1");
        expect(this.resultSet.getString("SUB_REGION")).andReturn("subregion");
        expect(this.resultSet.next()).andReturn(true);
        expect(this.resultSet.getInt("SUB_REGION_COUNT")).andReturn(20);
        expect(this.resultSet.getString("REGION")).andReturn("region2");
        expect(this.resultSet.getString("SUB_REGION")).andReturn("0");
        expect(this.resultSet.next()).andReturn(true);
        expect(this.resultSet.getInt("SUB_REGION_COUNT")).andReturn(30);
        expect(this.resultSet.getString("REGION")).andReturn("region3");
        expect(this.resultSet.getString("SUB_REGION")).andReturn(null);
        expect(this.resultSet.next()).andReturn(false);
        this.resultSet.close();
        this.statement.close();
        this.connection.close();
        replay(this.dataSource, this.connection, this.statement, this.resultSet, this.sqlStatements);
        Map<String, Integer> map = this.manager.searchCount("query");
        assertEquals(Integer.valueOf(20), map.get("region2"));
        assertEquals(Integer.valueOf(10), map.get("region1--subregion"));
        assertFalse(map.containsKey("region3"));
        verify(this.dataSource, this.connection, this.statement, this.resultSet, this.sqlStatements);
    }

    @Test(expected = IllegalStateException.class)
    public void testSearchCountThrowsException() throws SQLException {
        expect(this.dataSource.getConnection()).andThrow(new SQLException());
        replay(this.dataSource, this.connection, this.statement, this.resultSet, this.sqlStatements);
        this.manager.searchCount("query");
        verify(this.dataSource, this.connection, this.statement, this.resultSet, this.sqlStatements);
    }

    @Test
    public void testSearchRegion() throws SQLException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        expect(this.sqlStatements.getProperty("search.region")).andReturn("");
        expect(this.connection.prepareStatement(isA(String.class))).andReturn(this.statement);
        this.statement.setString(eq(1), isA(String.class));
        this.statement.setString(eq(2), isA(String.class));
        this.statement.setString(3, "region");
        expect(this.statement.executeQuery()).andReturn(this.resultSet);
        expect(this.resultSet.next()).andReturn(true);
        expect(this.resultSet.getInt("ERESOURCE_ID")).andReturn(1);
        expect(this.resultSet.getString(isA(String.class))).andReturn("string").times(7);
        expect(this.resultSet.next()).andReturn(false);
        this.resultSet.close();
        this.statement.close();
        this.connection.close();
        replay(this.dataSource, this.connection, this.statement, this.resultSet, this.sqlStatements);
        this.manager.searchRegion("region", "query");
        verify(this.dataSource, this.connection, this.statement, this.resultSet, this.sqlStatements);
    }

    @Test
    public void testSearchRegionWithSubregion() throws SQLException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        expect(this.sqlStatements.getProperty("search.subregion")).andReturn("");
        expect(this.connection.prepareStatement(isA(String.class))).andReturn(this.statement);
        this.statement.setString(eq(1), isA(String.class));
        this.statement.setString(eq(2), isA(String.class));
        this.statement.setString(3, "region");
        this.statement.setString(4, "subregion");
        expect(this.statement.executeQuery()).andReturn(this.resultSet);
        expect(this.resultSet.next()).andReturn(true);
        expect(this.resultSet.getInt("ERESOURCE_ID")).andReturn(1);
        expect(this.resultSet.getString(isA(String.class))).andReturn("string").times(7);
        expect(this.resultSet.next()).andReturn(false);
        this.resultSet.close();
        this.statement.close();
        this.connection.close();
        replay(this.dataSource, this.connection, this.statement, this.resultSet, this.sqlStatements);
        this.manager.searchRegion("region--subregion", "query");
        verify(this.dataSource, this.connection, this.statement, this.resultSet, this.sqlStatements);
    }
}
