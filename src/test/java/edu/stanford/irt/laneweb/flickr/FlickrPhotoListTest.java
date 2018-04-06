package edu.stanford.irt.laneweb.flickr;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

import org.junit.Test;

public class FlickrPhotoListTest {

    @Test
    public void testFlickrPhotoList() throws IOException {
        assertEquals(877, new FlickrPhotoList(getClass().getResourceAsStream("flickr-photos.txt")).size());
    }

    @Test
    public void testFlickrPhotoListIOException() throws IOException {
        InputStream input = mock(InputStream.class);
        expect(input.read(isA(byte[].class), eq(0), eq(8192))).andThrow(new IOException());
        input.close();
        replay(input);
        try {
            new FlickrPhotoList(input);
        } catch (UncheckedIOException e) {
        }
        verify(input);
    }
}
