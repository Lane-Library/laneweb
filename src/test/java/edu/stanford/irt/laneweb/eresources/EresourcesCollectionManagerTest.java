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
import java.util.Properties;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EresourcesCollectionManagerTest {

    private Connection connection;

    private DataSource dataSource;

    private AbstractCollectionManager manager;

    private ResultSet resultSet;

    private PreparedStatement statement;

    private Properties sqlStatements;

    @Before
    public void setUp() throws Exception {
        this.dataSource = createMock(DataSource.class);
        this.sqlStatements = createMock(Properties.class);
        this.manager = new EresourcesCollectionManager(this.dataSource, this.sqlStatements);
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

    @After
    public void tearDown() {
        verify(this.dataSource, this.connection, this.statement, this.resultSet, this.sqlStatements);
    }
}
