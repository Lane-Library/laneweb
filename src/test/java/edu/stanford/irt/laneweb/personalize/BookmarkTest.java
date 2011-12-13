package edu.stanford.irt.laneweb.personalize;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.LanewebException;

public class BookmarkTest {

    private String label;

    private Bookmark link;

    private String url;

    @Before
    public void setUp() throws Exception {
        this.label = "label";
        this.url = "url";
        this.link = new Bookmark(this.label, this.url);
    }

    @Test
    public void testEquals() {
        assertTrue(this.link.equals(new Bookmark(this.label, this.url)));
    }

    @Test
    public void testEqualsDifferent() {
        assertFalse(this.link.equals(new Bookmark(this.url, this.label)));
    }

    @Test
    public void testEqualsNull() {
        assertFalse(this.link.equals(null));
    }

    @Test
    public void testEqualsSame() {
        assertTrue(this.link.equals(this.link));
    }

    @Test
    public void testGetLabel() {
        assertEquals(this.label, this.link.getLabel());
    }

    @Test
    public void testGetUrl() {
        assertEquals(this.url, this.link.getUrl());
    }

    @Test
    public void testHashCode() {
        assertEquals((this.label.hashCode() ^ this.url.hashCode()), this.link.hashCode());
    }

    @Test
    public void testSavedLinkNullLabel() {
        try {
            new Bookmark(null, this.url);
            fail();
        } catch (LanewebException e) {
        }
    }

    @Test
    public void testSavedLinkNullUrl() {
        try {
            new Bookmark(this.label, null);
            fail();
        } catch (LanewebException e) {
        }
    }
}
