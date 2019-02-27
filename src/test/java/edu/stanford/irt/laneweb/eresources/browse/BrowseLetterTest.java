package edu.stanford.irt.laneweb.eresources.browse;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BrowseLetterTest {

    @Test
    public final void testGetCount() {
        BrowseLetter letter = new BrowseLetter("requestUri", "a", 10);
        assertEquals(10, letter.getCount());
    }

    @Test
    public final void testGetLetter() {
        BrowseLetter letter = new BrowseLetter("requestUri", "a", 10);
        assertEquals("a", letter.getLetter());
    }

    @Test
    public final void testGetUrl() {
        assertEquals("requestUri?a=a", (new BrowseLetter("requestUri", "a", 10)).getUrl());
        assertEquals("requestUri?a=%23", (new BrowseLetter("requestUri", "1", 10)).getUrl());
    }
}
