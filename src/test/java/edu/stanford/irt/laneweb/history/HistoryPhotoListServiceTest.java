package edu.stanford.irt.laneweb.history;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HistoryPhotoListServiceTest {

    private HistoryPhotoListService service;

    @BeforeEach
    public void setUp() throws IOException {
        this.service = new HistoryPhotoListService(
                new HistoryPhotoList(getClass().getResourceAsStream("history-photos.txt")));
    }

    @Test
    public void testGetRandomPhotos() {
        assertEquals(5, this.service.getRandomPhotos(5).size());
    }
}
