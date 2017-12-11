package edu.stanford.irt.laneweb.cocoon;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.Model;

public class ResponsiveToggleSelectorTest {

    private ResponsiveToggleSelector selector;

    @Before
    public void setUp() {
        this.selector = new ResponsiveToggleSelector();
    }

    @Test
    public void testFalse() {
        assertFalse(this.selector.select(null, Collections.emptyMap(), null));
    }

    @Test
    public void testTrue() {
        assertTrue(this.selector.select(null, Collections.singletonMap(Model.RESPONSIVE, Boolean.TRUE), null));
    }
}
