package edu.stanford.irt.laneweb.history;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HistoryPhotoTest {

    private HistoryPhoto photo;

    @BeforeEach
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
