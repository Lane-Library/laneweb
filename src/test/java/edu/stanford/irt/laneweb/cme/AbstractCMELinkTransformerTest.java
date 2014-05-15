package edu.stanford.irt.laneweb.cme;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.Model;

/**
 * @author ryanmax
 */
public class AbstractCMELinkTransformerTest {

    private Map<String, Object> model;

    private AbstractCMELinkTransformer transformer;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.transformer = new AbstractCMELinkTransformer() {
            //
        };
        this.model = new HashMap<String, Object>();
    }

    /**
     * Test method for {@link edu.stanford.irt.laneweb.cme.AbstractCMELinkTransformer#isCMEHost(java.lang.String)} .
     */
    @Test
    public void testIsCMEHost() {
        assertTrue(this.transformer.isCMEHost("http://www.uptodate.com/online"));
    }

    /**
     * Test method for {@link edu.stanford.irt.laneweb.cme.AbstractCMELinkTransformer#isCMEHost(java.lang.String)} .
     */
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
