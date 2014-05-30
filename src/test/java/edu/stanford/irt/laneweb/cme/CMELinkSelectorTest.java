package edu.stanford.irt.laneweb.cme;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.Model;

public class CMELinkSelectorTest {

    private CMELinkSelector selector;

    @Before
    public void setUp() {
        this.selector = new CMELinkSelector();
    }

    @Test
    public void testSelect() {
        assertTrue(this.selector
                .select(null, Collections.<String, Object> singletonMap(Model.EMRID, Model.EMRID), null));
    }

    @Test
    public void testSelectNullEmrid() {
        assertFalse(this.selector.select(null, Collections.<String, Object> emptyMap(), null));
    }
}
