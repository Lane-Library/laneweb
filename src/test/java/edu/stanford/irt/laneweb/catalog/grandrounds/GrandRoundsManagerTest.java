package edu.stanford.irt.laneweb.catalog.grandrounds;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.LanewebException;

public class GrandRoundsManagerTest {

    private CallableStatement callable;

    private Clob clob;

    private Connection connection;

    private DataSource dataSource;

    private GrandRoundsManager manager;

    @Before
    public void setUp() throws IOException {
        this.dataSource = createMock(DataSource.class);
        InputStream presentationsSQL = getClass()
                .getResourceAsStream("/edu/stanford/irt/grandrounds/getGrandRounds.fnc");
        this.manager = new GrandRoundsManager(this.dataSource, presentationsSQL);
        this.connection = createMock(Connection.class);
        this.callable = createMock(CallableStatement.class);
        this.clob = createMock(Clob.class);
    }

    @Test
    public void testGetGrandRounds() throws SQLException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        expect(this.connection.prepareCall(isA(String.class))).andReturn(this.callable);
        this.callable.setString(1, "MEDICINE");
        this.callable.setString(2, "year");
        this.callable.registerOutParameter(3, Types.CLOB);
        expect(this.callable.execute()).andReturn(true);
        expect(this.callable.getClob(3)).andReturn(this.clob);
        expect(this.clob.getAsciiStream()).andReturn(getClass().getResourceAsStream("presentation.mrc"));
        this.clob.free();
        this.callable.close();
        this.connection.close();
        replay(this.callable, this.clob, this.connection, this.dataSource);
        assertEquals(1, this.manager.getGrandRounds("medicine", "year").size());
        verify(this.callable, this.clob, this.connection, this.dataSource);
    }

    @Test
    public void testGetGrandRoundsThrowsException() throws SQLException {
        expect(this.dataSource.getConnection()).andReturn(this.connection);
        expect(this.connection.prepareCall(isA(String.class))).andReturn(this.callable);
        this.callable.setString(1, "MEDICINE");
        this.callable.setString(2, "year");
        this.callable.registerOutParameter(3, Types.CLOB);
        expect(this.callable.execute()).andReturn(true);
        expect(this.callable.getClob(3)).andReturn(this.clob);
        expect(this.clob.getAsciiStream()).andThrow(new SQLException());
        this.clob.free();
        this.callable.close();
        this.connection.close();
        replay(this.callable, this.clob, this.connection, this.dataSource);
        try {
            this.manager.getGrandRounds("medicine", "year");
        } catch (LanewebException e) {
        }
        verify(this.callable, this.clob, this.connection, this.dataSource);
    }
}
