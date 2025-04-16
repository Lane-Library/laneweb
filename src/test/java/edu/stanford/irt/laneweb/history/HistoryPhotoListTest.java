package edu.stanford.irt.laneweb.history;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

import org.junit.jupiter.api.Test;

public class HistoryPhotoListTest {

    @Test
    public void testHistoryPhotoList() throws IOException {
        assertEquals(835, new HistoryPhotoList(getClass().getResourceAsStream("history-photos.txt")).size());
    }

    @Test
    public void testHistoryPhotoListIOException() throws IOException {
        InputStream input = mock(InputStream.class);
        expect(input.read(isA(byte[].class), eq(0), eq(8192))).andThrow(new IOException());
        input.close();
        replay(input);
        try {
            new HistoryPhotoList(input);
        } catch (UncheckedIOException e) {
        }
        verify(input);
    }
}
