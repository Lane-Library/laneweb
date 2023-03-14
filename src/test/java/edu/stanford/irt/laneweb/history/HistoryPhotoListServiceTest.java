package edu.stanford.irt.laneweb.history;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class HistoryPhotoListServiceTest {

    private HistoryPhotoListService service;

    @Before
    public void setUp() throws IOException {
        this.service = new HistoryPhotoListService(
                new HistoryPhotoList(getClass().getResourceAsStream("history-photos.txt")));
    }

    @Test
    public void testGetRandomPhotos() {
        assertEquals(5, this.service.getRandomPhotos(5).size());
    }
}
