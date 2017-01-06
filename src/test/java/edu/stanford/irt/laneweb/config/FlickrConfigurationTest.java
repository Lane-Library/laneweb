package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class FlickrConfigurationTest {

    private FlickrConfiguration configuration;

    @Before
    public void setUp() throws Exception {
        this.configuration = new FlickrConfiguration();
    }

    @Test
    public void testFlickrPhotoGenerator() throws IOException {
        assertNotNull(this.configuration.flickrPhotoGenerator());
    }

    @Test
    public void testFlickrPhotoListService() throws IOException {
        assertNotNull(this.configuration.flickrPhotoListService());
    }
}
