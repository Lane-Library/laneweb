package edu.stanford.irt.laneweb.eresources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.laneweb.eresources.model.Link;
import edu.stanford.irt.laneweb.eresources.model.LinkType;
import edu.stanford.irt.laneweb.eresources.model.Version;

public class VersionAndLinkTest {

    private Link link;

    private Version version;

    @BeforeEach
    public void setUp() {
        int[] itemCount = { 2, 1 };
        this.version = new Version();
        this.version.setAdditionalText("versionText");
        this.version.setCallnumber("callnumber");
        this.version.setHoldingsAndDates("holdingsAndDates");
        this.version.setItemCount(itemCount);
        this.version.setLinks(Collections.singletonList(this.link));
        this.version.setLocationName("locationName");
        this.version.setLocationUrl("locationUrl");
        this.version.setProxy(false);
        this.version.setPublisher("publisher");
        this.version.setSummaryHoldings("summaryHoldings");
        this.link = new Link();
        this.link.setAdditionalText("additionalText");
        this.link.setLabel("label");
        this.link.setLinkText("linkText");
        this.link.setType(LinkType.NORMAL);
        this.link.setUrl("url");
        this.link.setVersion(this.version);
    }

    @Test
    public void testGetAdditionalText() {
        assertEquals("additionalText", this.link.getAdditionalText());
    }

    @Test
    public void testGetCallnumber() {
        assertEquals("callnumber", this.link.getVersion().getCallnumber());
    }

    @Test
    public void testGetHoldingsAndDates() {
        assertEquals("holdingsAndDates", this.link.getVersion().getHoldingsAndDates());
    }

    @Test
    public void testGetItemCount() {
        assertEquals(2, this.link.getVersion().getItemCount()[0]);
        assertEquals(1, this.link.getVersion().getItemCount()[1]);
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
        assertEquals("locationName", this.link.getVersion().getLocationName());
    }

    @Test
    public void testGetLocationUrl() {
        assertEquals("locationUrl", this.link.getVersion().getLocationUrl());
    }

    @Test
    public void testGetPublisher() {
        assertEquals("publisher", this.link.getVersion().getPublisher());
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
        assertEquals("versionText", this.link.getVersion().getAdditionalText());
    }

    @Test
    public void testToString() {
        assertEquals("url:url", this.link.toString());
    }
}
