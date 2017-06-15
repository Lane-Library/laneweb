package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class QueryMapConfigurationTest {

    private QueryMapConfiguration configuration;

    @Before
    public void setUp() {
        this.configuration = new QueryMapConfiguration();
    }

    @Test
    public void testQueryMapper() {
        assertNotNull(this.configuration.queryMapper(null));
    }
}
