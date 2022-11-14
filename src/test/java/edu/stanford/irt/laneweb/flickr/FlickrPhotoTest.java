package edu.stanford.irt.laneweb.flickr;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class FlickrPhotoTest {

    private FlickrPhoto photo;

    @Before
    public void setUp() {
        this.photo = new FlickrPhoto("page", "thumbnail", "title");
    }

    @Test
    public void testGetPage() {
        assertEquals("page", this.photo.getPage());
    }

    @Test
    public void testGetThumbnail() {
        assertEquals("thumbnail", this.photo.getThumbnail());
    }

    @Test
    public void testGetTitle() {
        assertEquals("title", this.photo.getTitle());
    }
}
