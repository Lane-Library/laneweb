package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class QueryMapConfigurationTest {

    @Test
    public void testQueryMapper() {
        assertNotNull(new QueryMapConfiguration().queryMapper(null, null));
    }
}
