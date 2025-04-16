package edu.stanford.irt.laneweb.bookmarks;

import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

public class BookmarkExceptionTest {

    @Test
    public void testBookmarkException() {
        Throwable t = new Throwable();
        assertSame(t, new BookmarkException(t).getCause());
    }
}
