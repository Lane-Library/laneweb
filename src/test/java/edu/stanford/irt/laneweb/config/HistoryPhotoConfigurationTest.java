package edu.stanford.irt.laneweb.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HistoryPhotoConfigurationTest {

    private HistoryPhotoConfiguration configuration;

    @BeforeEach
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
