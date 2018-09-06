package edu.stanford.irt.laneweb.bookmarks;

import static org.junit.Assert.assertSame;

import org.junit.Test;

public class BookmarkExceptionTest {

    @Test
    public void testBookmarkException() {
        Throwable t = new Throwable();
        assertSame(t, new BookmarkException(t).getCause());
    }
}
