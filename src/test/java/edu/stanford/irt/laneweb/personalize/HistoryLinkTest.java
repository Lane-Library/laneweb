package edu.stanford.irt.laneweb.personalize;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.LanewebException;

public class HistoryLinkTest {

    private Date date;

    private String label;

    private History link;

    private String url;

    @Before
    public void setUp() throws Exception {
        this.label = "label";
        this.url = "url";
        this.date = new Date(Long.MAX_VALUE);
        this.link = new History(this.label, this.url, this.date);
    }

    @Test
    public void testEquals() {
        assertTrue(this.link.equals(new History(this.label, this.url, new Date(Long.MAX_VALUE))));
    }

    @Test
    public void testEqualsDifferent() {
        assertFalse(this.link.equals(new History(this.label, this.url, new Date())));
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
    public void testHashCode() {
        assertEquals(((this.label.hashCode() ^ this.url.hashCode()) ^ this.date.hashCode()), this.link.hashCode());
    }

    @Test
    public void testHistoryLinkNullDate() {
        try {
            new History(this.label, this.url, null);
            fail();
        } catch (LanewebException e) {
        }
    }
}
