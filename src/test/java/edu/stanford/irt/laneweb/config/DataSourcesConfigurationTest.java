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
        assertNotNull(this.configuration.eresourcesDataSource(null, null, null, 1));
    }

    @Test
    public void testGrandroundsDataSource() throws SQLException {
        assertNotNull(this.configuration.grandroundsDataSource(null, null, null, 1));
    }

    @Test
    public void testVoyagerDataSource() throws SQLException {
        assertNotNull(this.configuration.voyagerDataSource(null, null, null, 1));
    }
}
