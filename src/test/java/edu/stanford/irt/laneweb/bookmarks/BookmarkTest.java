package edu.stanford.irt.laneweb.bookmarks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.LanewebException;

public class BookmarkTest {

    private Bookmark bookmark;

    private String label;

    private String url;

    @Before
    public void setUp() throws Exception {
        this.label = "label";
        this.url = "url";
        this.bookmark = new Bookmark(this.label, this.url);
    }

    @Test
    public void testEquals() {
        assertTrue(this.bookmark.equals(new Bookmark(this.label, this.url)));
    }

    @Test
    public void testEqualsDifferent() {
        assertFalse(this.bookmark.equals(new Bookmark("foo", "bar")));
    }

    @Test
    public void testEqualsNotBookmark() {
        assertFalse(this.bookmark.equals(new Object()));
    }

    @Test
    public void testEqualsNull() {
        assertFalse(this.bookmark.equals(null));
    }

    @Test
    public void testEqualsSame() {
        assertTrue(this.bookmark.equals(this.bookmark));
    }

    @SuppressWarnings("serial")
    @Test
    public void testEqualsSameHashDifferentLabel() {
        assertFalse(this.bookmark.equals(new Bookmark("newlabel", this.url) {

            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public int hashCode() {
                return BookmarkTest.this.bookmark.hashCode();
            }
        }));
    }

    @SuppressWarnings("serial")
    @Test
    public void testEqualsSameHashDifferentUrl() {
        assertFalse(this.bookmark.equals(new Bookmark(this.label, "newurl") {

            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public int hashCode() {
                return BookmarkTest.this.bookmark.hashCode();
            }
        }));
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

    @Test(expected = LanewebException.class)
    public void testSetLabelAlreadySet() {
        this.bookmark.setLabel(this.label);
    }

    @Test(expected = NullPointerException.class)
    public void testSetLabelNull() {
        Bookmark b = new Bookmark();
        b.setLabel(null);
    }

    @Test
    public void testSetUrl() {
        Bookmark b = new Bookmark();
        b.setUrl(this.url);
        assertEquals(this.url, b.getUrl());
    }

    @Test(expected = LanewebException.class)
    public void testSetUrlAlreadySet() {
        this.bookmark.setUrl(this.url);
    }

    @Test(expected = NullPointerException.class)
    public void testSetUrlNull() {
        Bookmark b = new Bookmark();
        b.setUrl(null);
    }

    @Test
    public void testToString() {
        assertEquals("label=url", this.bookmark.toString());
    }
}
