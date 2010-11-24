package edu.stanford.irt.laneweb.cocoon.source;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class SitemapSourceLocationModifierTest {
    
    private SitemapSourceLocationModifier locationModifier;

    @Before
    public void setUp() throws Exception {
        this.locationModifier = new SitemapSourceLocationModifier();
    }

    @Test
    public void testModifyLocationDoubleSlash() {
        assertEquals("cocoon:/foo/bar", this.locationModifier.modify("cocoon://foo/bar"));
    }
}
