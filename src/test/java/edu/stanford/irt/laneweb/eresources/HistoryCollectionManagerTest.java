package edu.stanford.irt.laneweb.eresources;

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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HistoryCollectionManagerTest {
	
	private Connection connection;
	private DataSource dataSource;
	private HistoryCollectionManager manager;
	private ResultSet resultSet;
	private PreparedStatement statement;

	@Before
	public void setUp() throws Exception {
		this.manager = new HistoryCollectionManager();
		this.dataSource = createMock(DataSource.class);
		this.connection = createMock(Connection.class);
		this.statement = createMock(PreparedStatement.class);
		this.resultSet = createMock(ResultSet.class);
		this.manager.setDataSource(this.dataSource);
		expect(this.dataSource.getConnection()).andReturn(this.connection);
		expect(this.connection.prepareStatement(isA(String.class))).andReturn(this.statement);
		expect(this.statement.executeQuery()).andReturn(this.resultSet);
		this.resultSet.close();
		this.statement.close();
		this.connection.close();
		replay(this.dataSource, this.connection);
	}
	
	@After
	public void tearDown() {
		verify(this.dataSource, this.connection, this.statement, this.resultSet);
	}

	@Test
	public void testGetCore() throws SQLException {
		this.statement.setString(1, "type");
		expect(this.resultSet.next()).andReturn(false);
		replay(this.statement, this.resultSet);
		this.manager.getCore("type");
	}

	@Test
	public void testGetCoreResults() throws SQLException {
		this.statement.setString(1, "type");
		expect(this.resultSet.next()).andReturn(true);
		expect(this.resultSet.getInt(isA(String.class))).andReturn(0).times(4);
		expect(this.resultSet.getString(isA(String.class))).andReturn("string").times(9);
		expect(this.resultSet.next()).andReturn(false);
		replay(this.statement, this.resultSet);
		this.manager.getCore("type");
	}

	@Test
	public void testGetMesh() throws SQLException {
		this.statement.setString(1, "mesh");
		this.statement.setString(2, "type");
		expect(this.resultSet.next()).andReturn(false);
		replay(this.statement, this.resultSet);
		this.manager.getMesh("type", "mesh");
	}

	@Test
	public void testGetSubset() throws SQLException {
		this.statement.setString(1, "subset");
		expect(this.resultSet.next()).andReturn(false);
		replay(this.statement, this.resultSet);
		this.manager.getSubset("subset");
	}

	@Test
	public void testGetTypeString() throws SQLException {
		this.statement.setString(1, "type");
		expect(this.resultSet.next()).andReturn(false);
		replay(this.statement, this.resultSet);
		this.manager.getType("type");
	}

	@Test
	public void testGetTypeStringChar() throws SQLException {
		this.statement.setString(1, "string");
		this.statement.setString(2, "c");
		expect(this.resultSet.next()).andReturn(false);
		replay(this.statement, this.resultSet);
		this.manager.getType("string", 'c');
	}

	@Test
	public void testSearch() throws SQLException {
		this.statement.setString(1, "((${query})) ");
		this.statement.setString(2, "((${query})) ");
		this.statement.setString(3, "((${query})) ");
		this.statement.setString(4, "((${query})) ");
		expect(this.resultSet.next()).andReturn(false);
		replay(this.statement, this.resultSet);
		this.manager.search("query");
	}

	@Test
	public void testSearchCountSetOfStringSetOfStringString() throws SQLException {
		this.statement.setString(1, "((${query})) ");
		this.statement.setString(2, "type");
		this.statement.setString(3, "type");
		expect(this.resultSet.next()).andReturn(false);
		replay(this.statement, this.resultSet);
		this.manager.searchCount(Collections.singleton("type"), Collections.singleton("subset"), "query");
	}

	@Test
	public void testSearchCountSetOfStringString() throws SQLException {
		this.statement.setString(1, "((${query})) ");
		this.statement.setString(2, "type");
		this.statement.setString(3, "type");
		expect(this.resultSet.next()).andReturn(false);
		replay(this.statement, this.resultSet);
		this.manager.searchCount(Collections.singleton("type"), "query");
	}

	@Test
	public void testSearchType() throws SQLException {
		this.statement.setString(1, "((${query})) ");
		this.statement.setString(2, "((${query})) ");
		this.statement.setString(3, "((${query})) ");
		this.statement.setString(4, "((${query})) ");
		this.statement.setString(5, "type");
		expect(this.resultSet.next()).andReturn(false);
		replay(this.statement, this.resultSet);
		this.manager.searchType("type", "query");
	}

}
