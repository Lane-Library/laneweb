package edu.stanford.irt.laneweb.flickr;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class FlickrPhotoListServiceTest {

    private FlickrPhotoListService service;

    @Before
    public void setUp() {
        this.service = new FlickrPhotoListService(new FlickrPhotoList());
    }

    @Test
    public void testGetRandomPhotos() {
        assertEquals(5, this.service.getRandomPhotos(5).size());
    }
}
