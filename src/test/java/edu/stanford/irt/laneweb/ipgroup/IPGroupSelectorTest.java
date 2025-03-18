package edu.stanford.irt.laneweb.ipgroup;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.laneweb.model.Model;

public class IPGroupSelectorTest {

    private Map<String, Object> model;

    private IPGroupSelector selector;

    @BeforeEach
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
