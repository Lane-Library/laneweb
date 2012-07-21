package edu.stanford.irt.laneweb.eresources;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class EresourcesCollectionManagerTest {

    private AbstractCollectionManager manager;

    private ResultSet resultSet;

    @Before
    public void setUp() throws Exception {
        this.manager = new EresourcesCollectionManager(null, null);
        this.resultSet = createMock(ResultSet.class);
    }

    @Test
    public void testParseResultSet() throws SQLException {
        expect(this.resultSet.next()).andReturn(true);
        expect(this.resultSet.getInt("ERESOURCE_ID")).andReturn(1);
        expect(this.resultSet.getInt("RECORD_ID")).andReturn(1);
        expect(this.resultSet.getString("RECORD_TYPE")).andReturn("type");
        expect(this.resultSet.getString("TITLE")).andReturn("title");
        expect(this.resultSet.getString("E_DESCRIPTION")).andReturn("description");
        expect(this.resultSet.getInt("VERSION_ID")).andReturn(1);
        expect(this.resultSet.getString("PUBLISHER")).andReturn("publisher");
        expect(this.resultSet.getString("HOLDINGS")).andReturn("holdings");
        expect(this.resultSet.getString("DATES")).andReturn("dates");
        expect(this.resultSet.getString("V_DESCRIPTION")).andReturn("description");
        expect(this.resultSet.getInt("LINK_ID")).andReturn(1);
        expect(this.resultSet.getString("URL")).andReturn("url");
        expect(this.resultSet.getString("LABEL")).andReturn("label");
        expect(this.resultSet.getString("INSTRUCTION")).andReturn("instruction");
        expect(this.resultSet.next()).andReturn(false);
        replay(this.resultSet);
        this.manager.parseResultSet(this.resultSet, null);
        verify(this.resultSet);
    }
}
