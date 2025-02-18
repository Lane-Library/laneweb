package edu.stanford.irt.laneweb.eresources.browse;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class BrowseLetterTest {

    @Test
    public final void testGetCount() {
        BrowseLetter letter = new BrowseLetter("base", "a", 10);
        assertEquals(10, letter.getCount());
    }

    @Test
    public final void testGetLetter() {
        BrowseLetter letter = new BrowseLetter("base", "a", 10);
        assertEquals("a", letter.getLetter());
    }

    @Test
    public final void testGetUrl() {
        assertEquals("base.html?a=a", (new BrowseLetter("base", "a", 10)).getUrl());
        assertEquals("base.html?a=%23", (new BrowseLetter("base", "1", 10)).getUrl());
    }
}
