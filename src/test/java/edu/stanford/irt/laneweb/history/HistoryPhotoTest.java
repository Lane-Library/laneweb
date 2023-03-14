package edu.stanford.irt.laneweb.history;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class HistoryPhotoTest {

    private HistoryPhoto photo;

    @Before
    public void setUp() {
        this.photo = new HistoryPhoto("page", "thumbnail", "title");
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
