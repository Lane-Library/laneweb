package edu.stanford.irt.laneweb.cme;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.laneweb.model.Model;

/**
 * @author ryanmax
 */
public class AbstractCMELinkTransformerTest {

    private Map<String, Object> model;

    private AbstractCMELinkTransformer transformer;

    @BeforeEach
    public void setUp() {
        this.transformer = new AbstractCMELinkTransformer() {
            //
        };
        this.model = new HashMap<>();
    }

    @Test
    public void testIsCMEHost() {
        assertTrue(this.transformer.isCMEHost("http://www.uptodate.com/online"));
    }

    @Test
    public void testIsNotCMEHost() {
        assertFalse(this.transformer.isCMEHost("http://www.google.com/"));
    }

    @Test
    public void testSetModel() {
        this.model.put(Model.BASE_PATH, "/bp");
        this.model.put(Model.EMRID, "emrid");
        this.transformer.setModel(this.model);
        assertEquals("/bp", this.transformer.getBasePath());
        assertEquals("emrid", this.transformer.getEmrid());
    }
}
