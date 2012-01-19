package edu.stanford.irt.laneweb.cocoon;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ModelAttributeSelectorTest {

    private Map<String, Object> model;

    private ModelAttributeSelector selector;

    @Before
    public void setUp() throws Exception {
        this.selector = new ModelAttributeSelector();
        this.model = new HashMap<String, Object>();
    }

    @Test
    public void testSelectAttributeNotPresent() {
        assertFalse(this.selector.select(null, this.model, null));
    }

    @Test
    public void testSelectAttributePresent() {
        this.model.put("foo", null);
        this.selector.setAttributeName("foo");
        assertTrue(this.selector.select(null, this.model, null));
    }
}
