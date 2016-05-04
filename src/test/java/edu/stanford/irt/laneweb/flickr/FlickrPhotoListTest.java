package edu.stanford.irt.laneweb.flickr;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FlickrPhotoListTest {

    @Test
    public void testFlickrPhotoList() {
        assertEquals(877, new FlickrPhotoList().size());
    }
}
