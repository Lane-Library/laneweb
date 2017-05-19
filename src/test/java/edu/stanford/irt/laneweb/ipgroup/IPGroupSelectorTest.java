package edu.stanford.irt.laneweb.ipgroup;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.Model;

public class IPGroupSelectorTest {

    private Map<String, Object> model;

    private IPGroupSelector selector;

    @Before
    public void setUp() throws Exception {
        this.selector = new IPGroupSelector();
        this.model = new HashMap<>();
    }

    @Test
    public void testSelectNotPAVA() {
        this.model.put(Model.IPGROUP, IPGroup.OTHER);
        assertFalse(this.selector.select("PAVA", this.model, null));
    }

    @Test
    public void testSelectPAVA() {
        this.model.put(Model.IPGROUP, IPGroup.PAVA);
        assertTrue(this.selector.select("PAVA", this.model, null));
    }
}
