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
    public void testOnPremiseDataSource() throws SQLException {
        assertNotNull(this.configuration.onPremiseDataSource(null, null, null, 0, null, false));
    }
}
