package edu.stanford.irt.laneweb.bookmarks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.laneweb.LanewebException;

public class BookmarkTest {

    private Bookmark bookmark;

    private String label;

    private String url;

    @BeforeEach
    public void setUp() throws Exception {
        this.label = "label";
        this.url = "url";
        this.bookmark = new Bookmark(this.label, this.url);
    }

    @Test
    public void testEquals() {
        assertEquals(this.bookmark, new Bookmark(this.label, this.url));
    }

    @Test
    public void testEqualsDifferent() {
        assertNotEquals(this.bookmark, new Bookmark("foo", "bar"));
    }

    @Test
    public void testEqualsNotBookmark() {
        assertNotEquals(this.bookmark, new Object());
    }

    @Test
    public void testEqualsNull() {
        assertNotEquals(this.bookmark, null);
    }

    @Test
    public void testEqualsSame() {
        assertEquals(this.bookmark, this.bookmark);
    }

    @Test
    public void testEqualsSameHashDifferentLabel() {
        assertNotEquals(this.bookmark, new Bookmark("newlabel", this.url) {

            private static final long serialVersionUID = 1L;

            @Override
            public int hashCode() {
                return BookmarkTest.this.bookmark.hashCode();
            }
        });
    }

    @Test
    public void testEqualsSameHashDifferentUrl() {
        assertNotEquals(this.bookmark, new Bookmark(this.label, "newurl") {

            private static final long serialVersionUID = 1L;

            @Override
            public int hashCode() {
                return BookmarkTest.this.bookmark.hashCode();
            }
        });
    }

    @Test
    public void testGetLabel() {
        assertEquals(this.label, this.bookmark.getLabel());
    }

    @Test
    public void testGetUrl() {
        assertEquals(this.url, this.bookmark.getUrl());
    }

    @Test
    public void testHashCode() {
        assertEquals(Arrays.hashCode(new String[] { this.label, this.url }), this.bookmark.hashCode());
    }

    @Test
    public void testSavedLinkNullLabel() {
        try {
            new Bookmark(null, this.url);
            fail();
        } catch (NullPointerException e) {
        }
    }

    @Test
    public void testSavedLinkNullUrl() {
        try {
            new Bookmark(this.label, null);
            fail();
        } catch (NullPointerException e) {
        }
    }

    @Test
    public void testSetLabel() {
        Bookmark b = new Bookmark();
        b.setLabel(this.label);
        assertEquals(this.label, b.getLabel());
    }

    @Test()
    public void testSetLabelAlreadySet() {
        assertThrows(LanewebException.class, () -> {
            this.bookmark.setLabel(this.label);
        });
    }

    @Test()
    public void testSetLabelNull() {
        Bookmark b = new Bookmark();
        assertThrows(NullPointerException.class, () -> {
            b.setLabel(null);
        });
    }

    @Test
    public void testSetUrl() {
        Bookmark b = new Bookmark();
        b.setUrl(this.url);
        assertEquals(this.url, b.getUrl());
    }

    @Test()
    public void testSetUrlAlreadySet() {
        assertThrows(LanewebException.class, () -> {
            this.bookmark.setUrl(this.url);
        });
    }

    @Test
    public void testSetUrlNull() {
        Bookmark b = new Bookmark();
        assertThrows(NullPointerException.class, () -> {
            b.setUrl(null);
        });
    }

    @Test
    public void testToString() {
        assertEquals("label=url", this.bookmark.toString());
    }
}
