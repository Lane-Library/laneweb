package edu.stanford.irt.laneweb.config;

import static org.easymock.EasyMock.createMock;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

public class GrandRoundsConfigurationTest {

    private GrandRoundsConfiguration configuration;

    private DataSource dataSource;

    @Before
    public void setUp() {
        this.dataSource = createMock(DataSource.class);
        this.configuration = new GrandRoundsConfiguration(this.dataSource);
    }

    @Test
    public void testGrandRoundsGenerator() throws IOException {
        assertNotNull(this.configuration.grandRoundsGenerator());
    }

    @Test
    public void testGrandRoundsManager() throws IOException {
        assertNotNull(this.configuration.grandRoundsService());
    }

    @Test
    public void testPresentationSAXStrategy() {
        assertNotNull(this.configuration.presentationSAXStrategy());
    }
}
