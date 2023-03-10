package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class HistoryPhotoConfigurationTest {

    private HistoryPhotoConfiguration configuration;

    @Before
    public void setUp() throws Exception {
        this.configuration = new HistoryPhotoConfiguration();
    }

    @Test
    public void testHistoryPhotoGenerator() throws IOException {
        assertNotNull(this.configuration.historyPhotoGenerator());
    }

    @Test
    public void testHistoryPhotoListService() throws IOException {
        assertNotNull(this.configuration.historyPhotoListService());
    }
}
