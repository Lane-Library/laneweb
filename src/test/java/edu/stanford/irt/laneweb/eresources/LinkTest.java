package edu.stanford.irt.laneweb.eresources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

public class LinkTest {

    private Link link;

    @Before
    public void setUp() {
        this.link = new Link("label", LinkType.NORMAL, "url", "linkText", "additionalText", "holdingsAndDates",
                "publisher", "versionText");
    }

    @Test
    public void testGetAdditionalText() {
        assertEquals("additionalText", this.link.getAdditionalText());
    }

    @Test
    public void testGetHoldingsAndDates() {
        assertEquals("holdingsAndDates", this.link.getHoldingsAndDates());
    }

    @Test
    public void testGetLabel() {
        assertEquals("label", this.link.getLabel());
    }

    @Test
    public void testGetLinkText() {
        assertEquals("linkText", this.link.getLinkText());
    }

    @Test
    public void testGetPublisher() {
        assertEquals("publisher", this.link.getPublisher());
    }

    @Test
    public void testGetType() {
        assertSame(LinkType.NORMAL, this.link.getType());
    }

    @Test
    public void testGetUrl() {
        assertEquals("url", this.link.getUrl());
    }

    @Test
    public void testGetVersionText() {
        assertEquals("versionText", this.link.getVersionText());
    }

    @Test
    public void testToString() {
        assertEquals("url:url", this.link.toString());
    }
}
