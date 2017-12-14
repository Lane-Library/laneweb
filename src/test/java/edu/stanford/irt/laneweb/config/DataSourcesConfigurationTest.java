package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import com.zaxxer.hikari.HikariConfig;

public class DataSourcesConfigurationTest {

    private DataSourcesConfiguration configuration;

    @Before
    public void setUp() {
        this.configuration = new DataSourcesConfiguration();
    }

    @Test
    public void testGoogleCloudDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost/default");
        config.setInitializationFailTimeout(-1);
        assertNotNull(this.configuration.googleCloudDataSource(config));
    }

    @Test
    public void testHikariConfig() throws SQLException {
        assertNotNull(this.configuration.hikariConfig("jdbc:postgresql://localhost/default", null, null));
    }

    @Test
    public void testOnPremiseDataSource() throws SQLException {
        assertNotNull(this.configuration.onPremiseDataSource(null, null, null, 0, null, false));
    }
}
