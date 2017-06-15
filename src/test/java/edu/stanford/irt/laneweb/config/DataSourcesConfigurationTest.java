package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class DataSourcesConfigurationTest {

    private DataSourcesConfiguration configuration;

    @Before
    public void setUp() {
        this.configuration = new DataSourcesConfiguration();
    }

    @Test
    public void testEresourcesDataSource() throws SQLException {
        assertNotNull(this.configuration.eresourcesDataSource(null, null, null, 0, null, false));
    }

    @Test
    public void testGrandroundsDataSource() throws SQLException {
        assertNotNull(this.configuration.catalogDataSource(null, null, null, 0));
    }

    @Test
    public void testVoyagerDataSource() throws SQLException {
        assertNotNull(this.configuration.voyagerLoginDataSource(null, null, null, 0));
    }
}
