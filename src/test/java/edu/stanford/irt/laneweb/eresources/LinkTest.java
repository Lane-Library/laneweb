package edu.stanford.irt.laneweb.eresources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

public class LinkTest {

    private Link link;

    @Before
    public void setUp() {
        int[] itemCount = { 2, 1 };
        this.link = new Link.Builder().setLabel("label").setType(LinkType.NORMAL).setUrl("url").setLinkText("linkText")
                .setAdditionalText("additionalText").setHoldingsAndDates("holdingsAndDates").setPublisher("publisher")
                .setVersionText("versionText").setCallnumber("callnumber").setItemCount(itemCount)
                .setLocationName("locationName").setLocationUrl("locationUrl").build();
    }

    @Test
    public void testGetAdditionalText() {
        assertEquals("additionalText", this.link.getAdditionalText());
    }

    @Test
    public void testGetCallnumber() {
        assertEquals("callnumber", this.link.getCallnumber());
    }

    @Test
    public void testGetHoldingsAndDates() {
        assertEquals("holdingsAndDates", this.link.getHoldingsAndDates());
    }

    @Test
    public void testGetItemCount() {
        assertEquals(2, this.link.getItemCount()[0]);
        assertEquals(1, this.link.getItemCount()[1]);
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
    public void testGetLocationName() {
        assertEquals("locationName", this.link.getLocationName());
    }

    @Test
    public void testGetLocationUrl() {
        assertEquals("locationUrl", this.link.getLocationUrl());
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
